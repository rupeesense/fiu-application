package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.onemoney.OneMoneyUtils.writeValueAsStringSilently;
import static com.rupeesense.fi.model.ConsentStatus.ACTIVE;
import static com.rupeesense.fi.model.ConsentStatus.PAUSED;
import static com.rupeesense.fi.model.ConsentStatus.REVOKED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.ConsentNotificationRequest;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentArtifactAPIResponse;
import com.rupeesense.fi.ext.setu.SetuFIUService;
import com.rupeesense.fi.ext.setu.request.SetuConsentAPIRequest;
import com.rupeesense.fi.ext.setu.request.SetuRequestGenerator;
import com.rupeesense.fi.ext.setu.response.SetuConsentInitiateResponse;
import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.repo.RepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    try {
      System.out.println(objectMapper.writeValueAsString(consentAPIRequest));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    SetuConsentInitiateResponse consentAPIResponse = setuFIUService.initiateConsent(consentAPIRequest);
    Consent consent = new Consent();
    consent.setConsentId(consentAPIResponse.getId());
    consent.setConsentArtifact(writeValueAsStringSilently(objectMapper, consentAPIRequest.getConsentDetail()));
    consent.setAccountAggregator(AAIdentifier.ONEMONEY);
    consent.setStatus(consentAPIResponse.getStatus());
    consent.setUserId(consentRequest.getUserVpa());
    repositoryFacade.save(consent);
    return new ConsentResponse(consent.getUserId(), consent.getAccountAggregator(),
        consent.getConsentId(), consent.getStatus());
  }

  public void getData(String sessionId) {

  }

//  public void updateConsentAndHandleFromNotification(ConsentNotificationRequest consentNotificationRequest) {
//    ConsentHandle consentHandle = repositoryFacade.getConsentHandle(
//        consentNotificationRequest.getConsentStatusNotification().getConsentHandle(),
//        AAIdentifier.ONEMONEY);
//    if (consentHandle == null) {
//      throw new IllegalArgumentException("No consent handle found for the given consent handle id: "
//          + consentNotificationRequest.getConsentStatusNotification().getConsentHandle());
//    }
//
//    Consent consent = repositoryFacade
//        .findByConsentId(consentNotificationRequest.getConsentStatusNotification()
//            .getConsentId());
//
//    switch (consentNotificationRequest.getConsentStatusNotification().getConsentStatus()) {
//      case ACTIVE:
//        // if consent already exists and is not active, update it
//        // this can be the case when user marks the consent as paused and then again marks it as active
//        if (consent != null) {
//          consent.setStatus(ACTIVE);
//          repositoryFacade.save(consent);
//        } else {
//          //consent is not present, fetch it from AA and save it
//          String consentId = consentNotificationRequest.getConsentStatusNotification().getConsentId();
//          OneMoneyConsentArtifactAPIResponse response = accountAggregatorOrchestratorService.fetchConsentArtifact(consentId);
//          consent = createAndSaveConsent(consentId, response);
//          consentHandle.setConsent(consent);
//          consentHandle.setStatus(ConsentHandleStatus.APPROVED);
//          repositoryFacade.save(consentHandle);
//        }
//        break;
//      case REJECTED:
//        consentHandle.setStatus(ConsentHandleStatus.REJECTED);
//        repositoryFacade.save(consentHandle);
//        break;
//      case PAUSED:
//        if (consent.getStatus() != ACTIVE) {
//          throw new IllegalArgumentException("Consent can be paused only if it is active");
//        }
//        consent.setStatus(PAUSED);
//        repositoryFacade.save(consent);
//        break;
//      case REVOKED:
//        if (consent.getStatus() != ACTIVE && consent.getStatus() != PAUSED) {
//          throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
//        }
//        consent.setStatus(REVOKED);
//        repositoryFacade.save(consent);
//        break;
//    }
//  }

  private Consent createAndSaveConsent(String consentId, OneMoneyConsentArtifactAPIResponse response) {
    Consent consent = new Consent();
    consent.setConsentId(consentId);
    consent.setUserId(response.getConsentDetail().getCustomer().getId());
    consent.setAccountAggregator(AAIdentifier.ONEMONEY);
    consent.setStatus(ACTIVE);
    consent.setConsentArtifact(writeValueAsStringSilently(objectMapper, response.getConsentDetail()));
    consent.setDigitalSignature(response.getConsentDetailDigitalSignature());
    repositoryFacade.save(consent);
    return consent;
  }

}
