package com.rupeesense.fi.aa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountAggregatorOrchestratorService {

//  private final RepositoryFacade repositoryFacade;
//  private final OneMoneyAccountAggregator oneMoneyAccountAggregator;
//  private final OneMoneyRequestGenerator oneMoneyRequestGenerator;
//  private final ObjectMapper objectMapper;
//
//  @Autowired
//  public AccountAggregatorOrchestratorService(OneMoneyAccountAggregator oneMoneyAccountAggregator,
//      RepositoryFacade repositoryFacade, OneMoneyRequestGenerator oneMoneyRequestGenerator,
//      ObjectMapper objectMapper) {
//    this.oneMoneyAccountAggregator = oneMoneyAccountAggregator;
//    this.repositoryFacade = repositoryFacade;
//    this.oneMoneyRequestGenerator = oneMoneyRequestGenerator;
//    this.objectMapper = objectMapper;
//  }
//
//
//  public ConsentResponse initiateConsent(ConsentRequest consentRequest) {
//    OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest = oneMoneyRequestGenerator
//        .generatePeriodicConsentRequestForUser(consentRequest.getUserVpa());
//    OneMoneyConsentInitiateAPIResponse consentAPIResponse = oneMoneyAccountAggregator
//        .initiateConsent(oneMoneyConsentAPIRequest);
//    ConsentHandle consentHandle = createAndSaveConsentHandle(oneMoneyConsentAPIRequest, consentAPIResponse);
//    return new ConsentResponse(consentHandle.getUserId(), consentRequest.getAccountAggId(),
//        consentHandle.getConsentHandleId(), consentHandle.getStatus());
//  }
//
//  public OneMoneyConsentArtifactAPIResponse fetchConsentArtifact(String consentId) {
//    return oneMoneyAccountAggregator.getConsentArtifact(consentId);
//  }
//
//  public Session placeDataRequest(String userId) {
//    //get the latest Active consent for the user
//    Consent consent = repositoryFacade.findActiveConsentByUserId(userId);
//    log.debug("Active consent for user: {} is: {}", userId, consent);
//    if (consent == null) {
//      //TODO: throw a custom exception and map it using error code
//      //error code framework
//      log.error("No active consent found for user: {}", userId);
//      throw new IllegalStateException("No active consent found for user: " + userId);
//    }
//    FIDataRequest dataRequest = oneMoneyRequestGenerator.generateDataRequest(consent);
//    OneMoneyRequestDataAPIResponse response = oneMoneyAccountAggregator.placeDataRequest(dataRequest);
//    log.debug("Data request placed for user: {} with response: {}", userId, response);
//    //create entity session in a different class and save it
//    Session session = new Session();
//    session.setId(response.getSessionId());
//    session.setRequestedAt(LocalDateTime.now());
//    session.setStatus(SessionStatus.AWAITED);
//    session.setConsent(consent);
//    session.setUserId(userId);
//    repositoryFacade.save(session);
//    return session;
//
//  }
//
//  private ConsentHandle createAndSaveConsentHandle(OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest,
//      OneMoneyConsentInitiateAPIResponse consentAPIResponse) {
//    ConsentHandle consentHandle = new ConsentHandle();
//    consentHandle.setConsentHandleId(consentAPIResponse.getHandle());
//    consentHandle.setUserId(consentAPIResponse.getCustomer().getId());
//    consentHandle.setAccountAggregator(AAIdentifier.ONEMONEY);
//    try {
//      consentHandle.setConsentRequest(objectMapper.writeValueAsString(oneMoneyConsentAPIRequest));
//    } catch (JsonProcessingException ex) {
//      log.error("Error while serializing consent request: {}", oneMoneyConsentAPIRequest, ex);
//    }
//    consentHandle.setStatus(ConsentHandleStatus.PENDING);
//    repositoryFacade.save(consentHandle);
//    return consentHandle;
//  }
}
