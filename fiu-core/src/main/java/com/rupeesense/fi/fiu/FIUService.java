package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.onemoney.OneMoneyUtils.writeValueAsStringSilently;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class FIUService {

  private final RepositoryFacade repositoryFacade;

  private final SetuFIUService setuFIUService;

  private final ObjectMapper objectMapper;

  private final SetuRequestGenerator setuRequestGenerator;

  @Autowired
  public FIUService(RepositoryFacade repositoryFacade,
      SetuFIUService setuFIUService,
      ObjectMapper objectMapper,
      SetuRequestGenerator setuRequestGenerator) {
    this.repositoryFacade = repositoryFacade;
    this.setuFIUService = setuFIUService;
    this.objectMapper = objectMapper;
    this.setuRequestGenerator = setuRequestGenerator;
  }


  public ConsentResponse createConsent(ConsentRequest consentRequest) {
    SetuConsentAPIRequest consentAPIRequest = setuRequestGenerator.generateConsentRequest(consentRequest.getUserVpa());
    log.debug("consent API Request: {}", consentAPIRequest);
    SetuConsentInitiateResponse consentAPIResponse = setuFIUService.initiateConsent(consentAPIRequest);
    log.debug("consent API Response: {}", consentAPIResponse);
    Consent consent = new Consent();
    consent.setConsentId(consentAPIResponse.getId());
    consent.setConsentArtifact(writeValueAsStringSilently(objectMapper, consentAPIRequest.getConsentDetail()));
    consent.setAccountAggregator(AAIdentifier.ONEMONEY);
    consent.setStatus(consentAPIResponse.getStatus());
    consent.setUserId(consentRequest.getUserVpa());
    repositoryFacade.save(consent);
    return new ConsentResponse(consent.getUserId(), consent.getAccountAggregator(),
        consent.getConsentId(), consent.getStatus(), consentAPIResponse.getUrl());
  }


  public Session createDataRequest(DataRequest dataRequest) {
    Consent consent = repositoryFacade.findActiveConsentByUserId(dataRequest.getUserVpa());
    if (consent == null) {
      throw new IllegalArgumentException("No active consent found for the given user id: " + dataRequest.getUserVpa());
    }
    SetuDataRequest setuDataRequest = setuRequestGenerator.generateDataRequest(consent.getConsentId(), dataRequest.getFrom(), dataRequest.getTo());
    SetuSessionResponse response = setuFIUService.createDataRequest(setuDataRequest);
    Session session = new Session();
    session.setSessionId(response.getId());
    session.setRequestedAt(LocalDateTime.now());
    session.setConsent(consent);
    session.setAccountAggregator(consent.getAccountAggregator());
    session.setStatus(response.getStatus());
    session.setUserId(dataRequest.getUserVpa());
    repositoryFacade.save(session);
    return session;
  }

  public void receiveSessionNotification(SessionNotificationEvent sessionNotificationEvent) {
    Session session = repositoryFacade.getSession(sessionNotificationEvent.getDataSessionId());
    if (session == null) {
      throw new IllegalArgumentException("No session found for the given session id: " + sessionNotificationEvent.getDataSessionId());
    }
    switch (sessionNotificationEvent.getData().getStatus()) {
      case COMPLETED:
        //fetch and save all data
        getAndSaveData(session);
        session.setStatus(SessionStatus.COMPLETED);
        break;
      case FAILED:
        session.setStatus(SessionStatus.FAILED);
        break;
      case EXPIRED:
        session.setStatus(SessionStatus.EXPIRED);
        break;
      case PARTIAL:
        //fetch partial data
        //TODO: not implemented
        session.setStatus(SessionStatus.PARTIAL);
        break;
      default:
        throw new IllegalArgumentException("Invalid session status: " + sessionNotificationEvent.getData().getStatus());
    }
    repositoryFacade.save(session);
  }

  public void getAndSaveData(Session session) {
    SetuDataResponse setuDataResponse = setuFIUService.getData(session.getSessionId());
    // Iterate through each data payload
    for (SetuDataResponse.DataPayload dataPayload : setuDataResponse.getDataPayload()) {
      // For each account-level data
      for (SetuDataResponse.AccountLevelData accountLevelData : dataPayload.getData()) {
        // Create account object
        Account account = repositoryFacade.getAccountIfItExists(dataPayload.getFipId(), session.getUserId(), accountLevelData.getLinkRefNumber())
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
        account.setUserId(session.getUserId());
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

        repositoryFacade.saveAccount(account);
        // Create and fill transaction details
        Set<Transaction> transactions = new HashSet<>();
//        if (StringUtils.hasLength(account.getAccountId())) {
//          transactions.addAll(repositoryFacade.getTransactionsForAccountAndUser(account.getAccountId(), session.getUserId()));
//        }
        for (SetuDataResponse.Transactions.Transaction transactionData : accountData.getTransactions().getTransaction()) {
          Transaction transaction = new Transaction();
          transaction.setAccount(account);
          transaction.setFipTransactionId(transactionData.getTxnId());
          transaction.setAmount(Float.parseFloat(transactionData.getAmount()));
          transaction.setNarration(transactionData.getNarration());
          // Assuming TransactionType and PaymentMethod enums exist in Transaction class
          transaction.setTransactionType(TransactionType.valueOf(transactionData.getType()));
          transaction.setPaymentMethod(PaymentMethod.valueOf(transactionData.getMode()));
          transaction.setCurrentBalance(Float.parseFloat(transactionData.getCurrentBalance()));
          transaction.setTransactionTimeStamp(LocalDateTime.parse(transactionData.getTransactionTimestamp(), formatter));
          transaction.setValueDate(LocalDateTime.parse(transactionData.getValueDate(), formatter));
          transaction.setReferenceNumber(transactionData.getReference());
          transaction.setUserId(session.getUserId());
          transaction.setFipID(account.getFipID());
          transactions.add(transaction);
        }
        repositoryFacade.saveTransactions(transactions);
      }
    }
  }


  public void updateConsent(ConsentNotificationEvent notificationEvent) {
    Consent consent = repositoryFacade.findByConsentId(notificationEvent.getConsentId());
    if (consent == null) {
      throw new IllegalArgumentException("No consent found for the given consent id: "
          + notificationEvent.getConsentId());
    }

    switch (notificationEvent.getData().getStatus()) {
      case ACTIVE:
        consent.setStatus(ConsentStatus.ACTIVE);
        break;
      case PAUSED:
        if (consent.getStatus() != ConsentStatus.ACTIVE) {
          throw new IllegalArgumentException("Consent can be paused only if it is active");
        }
        consent.setStatus(ConsentStatus.PAUSED);
        break;
      case REJECTED:
        consent.setStatus(ConsentStatus.REJECTED);
        break;
      case EXPIRED:
        consent.setStatus(ConsentStatus.EXPIRED);
        break;
      case REVOKED:
        if (consent.getStatus() != ConsentStatus.ACTIVE && consent.getStatus() != ConsentStatus.PAUSED) {
          throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
        }
        consent.setStatus(ConsentStatus.REVOKED);
        break;
    }
    repositoryFacade.save(consent);
  }
}
