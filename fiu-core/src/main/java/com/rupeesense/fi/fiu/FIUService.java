package com.rupeesense.fi.fiu;

import static com.rupeesense.fi.ext.onemoney.OneMoneyUtils.writeValueAsStringSilently;
import static com.rupeesense.fi.model.ConsentStatus.ACTIVE;
import static com.rupeesense.fi.model.ConsentStatus.EXPIRED;
import static com.rupeesense.fi.model.ConsentStatus.PAUSED;
import static com.rupeesense.fi.model.ConsentStatus.REJECTED;
import static com.rupeesense.fi.model.ConsentStatus.REVOKED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.NotificationEvent;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.request.DataRequest;
import com.rupeesense.fi.api.request.SessionNotificationEvent;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.setu.SetuFIUService;
import com.rupeesense.fi.ext.setu.request.SetuConsentAPIRequest;
import com.rupeesense.fi.ext.setu.request.SetuDataRequest;
import com.rupeesense.fi.ext.setu.request.SetuRequestGenerator;
import com.rupeesense.fi.ext.setu.response.SetuConsentInitiateResponse;
import com.rupeesense.fi.ext.setu.response.SetuSessionResponse;
import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.Session;
import com.rupeesense.fi.repo.RepositoryFacade;
import java.time.LocalDateTime;
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

//  public void getData(String sessionId) {
//
//  }

  public Session createDataRequest(DataRequest dataRequest) {
    Consent consent = repositoryFacade.findActiveConsentByUserId(dataRequest.getUserVpa());
    if (consent == null) {
      throw new IllegalArgumentException("No active consent found for the given user id: " + dataRequest.getUserVpa());
    }
    SetuDataRequest setuDataRequest = setuRequestGenerator.generateDataRequest(consent.getConsentId(), dataRequest.getFrom(), dataRequest.getTo());
    SetuSessionResponse response = setuFIUService.createDataRequest(setuDataRequest);
    Session session = new Session();
    session.setId(response.getId());
    session.setRequestedAt(LocalDateTime.now());
    session.setConsent(consent);
    session.setStatus(response.getStatus());
    session.setUserId(dataRequest.getUserVpa());
    repositoryFacade.save(session);
    return session;
  }

  public void receiveSessionNotification(SessionNotificationEvent sessionNotificationEvent) {
    sessionNotificationEvent.get
  }


  public void updateConsent(NotificationEvent notificationEvent) {
        Consent consent = repositoryFacade.findByConsentId(notificationEvent.getConsentId());
        if (consent == null) {
          throw new IllegalArgumentException("No consent found for the given consent id: "
              + notificationEvent.getConsentId());
        }

        switch (notificationEvent.getData().getConsentStatus()) {
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
          case EXPIRED:
            consent.setStatus(EXPIRED);
            break;
          case REVOKED:
            if (consent.getStatus() != ACTIVE && consent.getStatus() != PAUSED) {
              throw new IllegalArgumentException("Consent can be revoked only if it is active or paused");
            }
            consent.setStatus(REVOKED);
            break;
          default:
            throw new IllegalArgumentException("Invalid consent status: "
                + notificationEvent.getData().getConsentStatus());
        }
        repositoryFacade.save(consent);
  }
}
