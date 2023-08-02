package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.onemoney.OneMoneyUtils.writeValueAsStringSilently;
import static com.rupeesense.fi.model.ConsentStatus.ACTIVE;
import static com.rupeesense.fi.model.ConsentStatus.PAUSED;
import static com.rupeesense.fi.model.ConsentStatus.REJECTED;
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


  public void updateConsent(ConsentNotificationRequest consentNotificationRequest) {
        Consent consent = repositoryFacade.findByConsentId(consentNotificationRequest.getConsentId());
        if (consent == null) {
          throw new IllegalArgumentException("No consent found for the given consent id: "
              + consentNotificationRequest.getConsentId());
        }

        switch (consentNotificationRequest.getData().getConsentStatus()) {
          case ACTIVE:
            consent.setStatus(ACTIVE);
            break;
          case PAUSED:
            if (consent.getStatus() != ACTIVE) {
              throw new IllegalArgumentException("Consent can be paused only if it is active");
            }
            consent.setStatus(PAUSED);
            break;
          case REJECTED:
            consent.setStatus(REJECTED);
            break;
          case REVOKED:
            if (consent.getStatus() != ACTIVE && consent.getStatus() != PAUSED) {
              throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
            }
            consent.setStatus(REVOKED);
            break;
          default:
            throw new IllegalArgumentException("Invalid consent status: "
                + consentNotificationRequest.getData().getConsentStatus());
        }
        repositoryFacade.save(consent);
  }

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
