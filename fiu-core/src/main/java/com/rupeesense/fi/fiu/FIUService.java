package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.onemoney.OneMoneyUtils.writeValueAsStringSilently;
import static com.rupeesense.fi.model.ConsentStatus.ACTIVE;
import static com.rupeesense.fi.model.ConsentStatus.PAUSED;
import static com.rupeesense.fi.model.ConsentStatus.REVOKED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.aa.AccountAggregatorService;
import com.rupeesense.fi.api.request.ConsentNotificationRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentArtifactAPIResponse;
import com.rupeesense.fi.model.AccountAggregatorIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentHandle;
import com.rupeesense.fi.model.ConsentHandleStatus;
import com.rupeesense.fi.repo.ConsentHandleRepository;
import com.rupeesense.fi.repo.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FIUService {

  private final ConsentHandleRepository consentHandleRepository;

  private final ConsentRepository consentRepository;

  private final AccountAggregatorService accountAggregatorService;

  private final ObjectMapper objectMapper;

  @Autowired
  public FIUService(ConsentHandleRepository consentHandleRepository,
      ConsentRepository consentRepository, AccountAggregatorService accountAggregatorService,
      ObjectMapper objectMapper) {
    this.consentHandleRepository = consentHandleRepository;
    this.consentRepository = consentRepository;
    this.accountAggregatorService = accountAggregatorService;
    this.objectMapper = objectMapper;
  }

  public void receiveConsentNotification(ConsentNotificationRequest consentNotificationRequest) {
    ConsentHandle consentHandle = consentHandleRepository.findByConsentHandleIdAndAccountAggregator(
            consentNotificationRequest.getConsentStatusNotification().getConsentHandle(),
            AccountAggregatorIdentifier.ONEMONEY);
    if (consentHandle == null) {
      throw new IllegalArgumentException("No consent handle found for the given consent handle id: "
          + consentNotificationRequest.getConsentStatusNotification().getConsentHandle());
    }

    Consent consent = consentRepository
        .findByConsentId(consentNotificationRequest.getConsentStatusNotification()
            .getConsentId());

    switch (consentNotificationRequest.getConsentStatusNotification().getConsentStatus()) {
      case ACTIVE:
        // if consent already exists and is not active, update it
        // this can be the case when user marks the consent as paused and then again marks it as active
        if (consent != null) {
          consent.setStatus(ACTIVE);
          consentRepository.save(consent);
        } else {
          //consent is not present, fetch it from AA and save it
          String consentId = consentNotificationRequest.getConsentStatusNotification().getConsentId();
          OneMoneyConsentArtifactAPIResponse response = accountAggregatorService.fetchConsentArtifact(consentId);
          consent = createAndSaveConsent(consentId, response);
          consentHandle.setConsent(consent);
          consentHandle.setStatus(ConsentHandleStatus.APPROVED);
          consentHandleRepository.save(consentHandle);
        }
        break;
      case REJECTED:
        consentHandle.setStatus(ConsentHandleStatus.REJECTED);
        consentHandleRepository.save(consentHandle);
        break;
      case PAUSED:
        if (consent.getStatus()!=ACTIVE) {
          throw new IllegalArgumentException("Consent can be paused only if it is active");
        }
        consent.setStatus(PAUSED);
        consentRepository.save(consent);
        break;
      case REVOKED:
        if (consent.getStatus()!=ACTIVE && consent.getStatus()!=PAUSED) {
          throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
        }
        consent.setStatus(REVOKED);
        consentRepository.save(consent);
        break;
    }
  }

  private Consent createAndSaveConsent(String consentId, OneMoneyConsentArtifactAPIResponse response) {
    Consent consent = new Consent();
    consent.setConsentId(consentId);
    consent.setUserId(response.getConsentDetail().getCustomer().getId());
    consent.setAccountAggregator(AccountAggregatorIdentifier.ONEMONEY);
    consent.setStatus(ACTIVE);
    consent.setConsentArtifact(writeValueAsStringSilently(objectMapper, response.getConsentDetail()));
    consent.setDigitalSignature(response.getConsentDetailDigitalSignature());
    consentRepository.save(consent);
    return consent;
  }

}
