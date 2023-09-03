package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.Utils.writeValueAsStringSilently;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.ConsentNotificationEvent;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.request.DataRequest;
import com.rupeesense.fi.api.request.SessionNotificationEvent;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.setu.SetuFIUService;
import com.rupeesense.fi.ext.setu.request.SetuConsentAPIRequest;
import com.rupeesense.fi.ext.setu.request.SetuDataRequest;
import com.rupeesense.fi.ext.setu.request.SetuRequestGenerator;
import com.rupeesense.fi.ext.setu.response.SetuConsentInitiateResponse;
import com.rupeesense.fi.ext.setu.response.SetuDataResponse;
import com.rupeesense.fi.ext.setu.response.SetuSessionResponse;
import com.rupeesense.fi.model.aa.AAIdentifier;
import com.rupeesense.fi.model.aa.Consent;
import com.rupeesense.fi.model.aa.Consent.DataFrequencyUnit;
import com.rupeesense.fi.model.aa.ConsentStatus;
import com.rupeesense.fi.model.aa.Session;
import com.rupeesense.fi.model.aa.SessionStatus;
import com.rupeesense.fi.model.data.Account;
import com.rupeesense.fi.model.data.AccountHolder;
import com.rupeesense.fi.model.data.PaymentMethod;
import com.rupeesense.fi.model.data.Transaction;
import com.rupeesense.fi.model.data.TransactionType;
import com.rupeesense.fi.repo.RepositoryFacade;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FIUService {

  private final RepositoryFacade repository;

  private final SetuFIUService setuFIUService;

  private final ObjectMapper objectMapper;

  private final SetuRequestGenerator setuRequestGenerator;

  @Autowired
  public FIUService(RepositoryFacade repository,
      SetuFIUService setuFIUService,
      ObjectMapper objectMapper,
      SetuRequestGenerator setuRequestGenerator) {
    this.repository = repository;
    this.setuFIUService = setuFIUService;
    this.objectMapper = objectMapper;
    this.setuRequestGenerator = setuRequestGenerator;
  }


  public ConsentResponse createConsent(ConsentRequest consentRequest) {
    SetuConsentAPIRequest consentAPIRequest = setuRequestGenerator.generateConsentRequest(consentRequest.getUserVpa());
    log.debug("calling setu api to create consent with request: {}", consentAPIRequest);
    SetuConsentInitiateResponse consentAPIResponse = setuFIUService.initiateConsent(consentAPIRequest);
    log.debug("consent API Response from Setu: {}", consentAPIResponse);
    Consent consent = new Consent();
    consent.setConsentId(consentAPIResponse.getId());
    consent.setConsentArtifact(writeValueAsStringSilently(objectMapper, consentAPIRequest.getConsentDetail()));
    consent.setAccountAggregator(AAIdentifier.ONEMONEY);
    consent.setStatus(consentAPIResponse.getStatus());
    consent.setDataFetchFreqUnit(DataFrequencyUnit.valueOf(consentAPIRequest.getConsentDetail().getFrequency().getUnit()));
    consent.setDataFetchFreqValue(consentAPIRequest.getConsentDetail().getFrequency().getValue());
    consent.setUserId(consentRequest.getUserVpa());
    repository.save(consent);
    log.debug("Consent saved to database: {}", consent);
    return new ConsentResponse(consent.getUserId(), consent.getAccountAggregator(),
        consent.getConsentId(), consent.getStatus(), consentAPIResponse.getUrl());
  }


  public Session createDataRequest(DataRequest dataRequest) {
    Consent consent = repository.findActiveConsentByUserId(dataRequest.getUserVpa());
    if (consent == null) {
      log.error("No active consent found for the given user id: {}", dataRequest.getUserVpa());
      throw new IllegalArgumentException("No active consent found for the given user id: " + dataRequest.getUserVpa());
    }

    //get non-fulfilled sessions attached to this consent
    Set<Session> sessions = repository.getPendingAndPartialSessionsByConsentId(consent.getConsentId());
    if (sessions.size() > 0) {
      log.error("There are already non-fulfilled {} sessions attached to this consent: {}", sessions.size(), consent.getConsentId());
      throw new IllegalArgumentException("There are already " + sessions.size() + " sessions attached to this consent: " + consent.getConsentId());
    }

    //check if data-request violates consent frequency
    //get latest session attached to this consent
    Session latestSession = repository.getLatestSessionByConsentId(consent.getConsentId());
    if (latestSession != null) {
      LocalDateTime nextDataFetchTime = latestSession.getCreatedAt()
          .plus(consent.getDataFetchFreqValue(), consent.getDataFetchFreqUnit().getChronoUnit());
      if (nextDataFetchTime.isAfter(LocalDateTime.now())) {
        log.error("Data request violates consent frequency. Next data fetch time: {}", nextDataFetchTime);
        throw new IllegalArgumentException("Data request violates consent frequency. Next data fetch time: " + nextDataFetchTime);
      }
    }

    SetuDataRequest setuDataRequest = setuRequestGenerator.generateDataRequest(consent.getConsentId(), dataRequest.getFrom(), dataRequest.getTo());
    log.debug("calling setu api to create data request with request: {}", setuDataRequest);
    SetuSessionResponse response = setuFIUService.createDataRequest(setuDataRequest);
    Session session = new Session();
    session.setSessionId(response.getId());
    session.setRequestedAt(LocalDateTime.now());
    session.setConsent(consent);
    session.setAccountAggregator(consent.getAccountAggregator());
    session.setStatus(response.getStatus());
    session.setUserId(dataRequest.getUserVpa());
    repository.save(session);
    log.debug("Session saved to database: {}", session);
    return session;
  }

  public void receiveSessionNotification(SessionNotificationEvent sessionNotificationEvent) {
    log.debug("Received session notification: {}", sessionNotificationEvent);
    Session session = repository.getSession(sessionNotificationEvent.getDataSessionId());
    if (session == null) {
      log.error("No session found for the given session id: {}", sessionNotificationEvent.getDataSessionId());
      throw new IllegalArgumentException("No session found for the given session id: " + sessionNotificationEvent.getDataSessionId());
    }
    switch (sessionNotificationEvent.getData().getStatus()) {
      case COMPLETED:
        SetuDataResponse setuDataResponse = setuFIUService.getData(session.getSessionId());
        saveData(setuDataResponse, session.getUserId());
        session.setStatus(SessionStatus.COMPLETED);
        log.debug("Session data fetch completed: {}", session.getId());
        break;
      case FAILED:
        session.setStatus(SessionStatus.FAILED);
        log.debug("Session failed: {}", session.getId());
        break;
      case EXPIRED:
        session.setStatus(SessionStatus.EXPIRED);
        log.debug("Session expired: {}", session.getId());
        break;
      case PARTIAL:
        //fetch partial data
        //TODO: not implemented
        session.setStatus(SessionStatus.PARTIAL);
        break;
      default:
        throw new IllegalArgumentException("Invalid session status: " + sessionNotificationEvent.getData().getStatus());
    }
    repository.save(session);
  }

  public void saveData(SetuDataResponse setuDataResponse, String userId) {
    // Iterate through each data payload
    for (SetuDataResponse.DataPayload dataPayload : setuDataResponse.getDataPayload()) {
      // For each account-level data
      for (SetuDataResponse.AccountLevelData accountLevelData : dataPayload.getData()) {
        // Create account object
        Account account = repository.getAccountIfItExists(dataPayload.getFipId(), userId, accountLevelData.getLinkRefNumber())
            .orElseGet(Account::new);

        account.setFipID(dataPayload.getFipId());
        account.setMaskedAccountNumber(accountLevelData.getMaskedAccNumber());
        account.setLinkRefNumber(accountLevelData.getLinkRefNumber());

        // Fill in account details from Financial Information Payload
        SetuDataResponse.AccountData accountData = accountLevelData.getInformationPayload().getAccount();
        account.setBranch(accountData.getSummary().getBranch());
        account.setCurrentODLimit(Float.parseFloat(accountData.getSummary().getCurrentODLimit()));
        account.setDrawingLimit(Float.parseFloat(accountData.getSummary().getDrawingLimit()));
        account.setIfscCode(accountData.getSummary().getIfscCode());
        account.setMicrCode(accountData.getSummary().getMicrCode());
        account.setCurrency(accountData.getSummary().getCurrency());
        account.setBalance(Float.parseFloat(accountData.getSummary().getCurrentBalance()));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        account.setOpeningDate(LocalDateTime.parse(accountData.getSummary().getOpeningDate(), formatter));
        account.setBalanceDateTime(LocalDateTime.parse(accountData.getSummary().getBalanceDateTime(), formatter));
        account.setStatus(Account.Status.valueOf(accountData.getSummary().getStatus()));
        account.setHolding(Account.Holding.valueOf(accountData.getProfile().getHolders().getType()));
        account.setType(Account.AccountType.valueOf(accountData.getType().toUpperCase()));
        account.setTxnRefreshedAt(LocalDateTime.now());
        account.setUserId(userId);
        // Create and fill holder details
        Set<AccountHolder> holders = new HashSet<>();
        for (SetuDataResponse.Profile.Holder holderData : accountData.getProfile().getHolders().getHolder()) {
          AccountHolder holder = new AccountHolder();
          holder.setAddress(holderData.getAddress());
          holder.setCkycCompliance(holderData.getCkycCompliance());
          holder.setDob(holderData.getDob());
          holder.setEmail(holderData.getEmail());
          holder.setMobile(holderData.getMobile());
          holder.setName(holderData.getName());
          holder.setNominee(holderData.getNominee());
          holder.setPan(holderData.getPan());
          holder.setAccount(account); // Set account of holder
          holders.add(holder);
        }
        account.getHolders().addAll(holders);

        repository.saveAccount(account);
        // TODO: Create and fill transaction details
        Set<Transaction> transactions = new HashSet<>();
        //TODO: eliminate the duplicate transactions
//        if (StringUtils.hasLength(account.getAccountId())) {
//          transactions.addAll(repositoryFacade.getTransactionsForAccountAndUser(account.getAccountId(), userId));
//        }
        for (SetuDataResponse.Transactions.Transaction transactionData : accountData.getTransactions().getTransaction()) {
          Transaction transaction = new Transaction();
          transaction.setAccount(account);
          transaction.setFipTransactionId(transactionData.getTxnId());
          transaction.setAmount(Float.parseFloat(transactionData.getAmount()));
          transaction.setNarration(transactionData.getNarration());
          transaction.setTransactionType(TransactionType.valueOf(transactionData.getType()));
          transaction.setPaymentMethod(PaymentMethod.valueOf(transactionData.getMode()));
          transaction.setCurrentBalance(Float.parseFloat(transactionData.getCurrentBalance()));
          transaction.setTransactionTimeStamp(LocalDateTime.parse(transactionData.getTransactionTimestamp(), formatter));
          transaction.setValueDate(LocalDateTime.parse(transactionData.getValueDate(), formatter));
          transaction.setReferenceNumber(transactionData.getReference());
          transaction.setUserId(userId);
          transaction.setFipID(account.getFipID());
          transactions.add(transaction);
        }
        repository.saveTransactions(transactions);
      }
    }
  }


  public void updateConsent(ConsentNotificationEvent notificationEvent) {
    log.info("Received consent notification: {}", notificationEvent);
    Consent consent = repository.findByConsentId(notificationEvent.getConsentId());
    if (consent == null) {
      log.error("No consent found for the given consent id: {}", notificationEvent.getConsentId());
      throw new IllegalArgumentException("No consent found for the given consent id: "
          + notificationEvent.getConsentId());
    }

    switch (notificationEvent.getData().getStatus()) {
      case ACTIVE:
        consent.setStatus(ConsentStatus.ACTIVE);
        log.debug("Consent activated: {}", consent);
        break;
      case PAUSED:
        if (consent.getStatus() != ConsentStatus.ACTIVE) {
          log.error("Consent couldn't be paused as it is not active: {}", consent);
          throw new IllegalArgumentException("Consent can be paused only if it is active");
        }
        consent.setStatus(ConsentStatus.PAUSED);
        log.debug("Consent paused: {}", consent);
        break;
      case REJECTED:
        consent.setStatus(ConsentStatus.REJECTED);
        log.debug("Consent rejected: {}", consent);
        break;
      case EXPIRED:
        consent.setStatus(ConsentStatus.EXPIRED);
        log.debug("Consent expired: {}", consent);
        break;
      case REVOKED:
        if (consent.getStatus() != ConsentStatus.ACTIVE && consent.getStatus() != ConsentStatus.PAUSED) {
          log.error("Consent couldn't be revoked as it is not active or paused: {}", consent);
          throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
        }
        consent.setStatus(ConsentStatus.REVOKED);
        log.debug("Consent revoked: {}", consent);
        break;
    }
    repository.save(consent);
  }
}
