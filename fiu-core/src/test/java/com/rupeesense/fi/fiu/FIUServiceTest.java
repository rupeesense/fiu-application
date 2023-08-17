package com.rupeesense.fi.fiu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.ext.setu.SetuFIUService;
import com.rupeesense.fi.ext.setu.request.SetuRequestGenerator;
import com.rupeesense.fi.repo.RepositoryFacade;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FIUServiceTest {

  @Mock
  private RepositoryFacade repositoryFacade;
  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private SetuRequestGenerator setuRequestGenerator;

  @Mock
  private SetuFIUService setuFIUService;


  private FIUService fiuService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    fiuService = new FIUService(repositoryFacade, setuFIUService, objectMapper, setuRequestGenerator);
  }

//  @Test
//  public void testUpdateConsentAndHandleFromNotification_ActiveConsent_NotExisting() throws JsonProcessingException {
//    ConsentNotificationRequest dummyRequest = new ConsentNotificationRequest();
//
//    dummyRequest.setVer("1.0");
//    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
//    dummyRequest.setTxnid("123456");
//
//    ConsentNotificationRequest.Notifier notifier = new ConsentNotificationRequest.Notifier("email", "example@example.com");
//    dummyRequest.setNotifier(notifier);
//
//    ConsentNotificationRequest.ConsentStatusNotification consentStatusNotification = new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//        ConsentStatus.ACTIVE);
//    dummyRequest.setConsentStatusNotification(consentStatusNotification);
//    // Set up notificationRequest with required data
//
//    // Prepare the mocked objects
//    OneMoneyConsentArtifactAPIResponse consentArtifactResponse = new OneMoneyConsentArtifactAPIResponse();
//    consentArtifactResponse.setConsentDetail(new ConsentDetail());
//    consentArtifactResponse.setConsentDetailDigitalSignature("signature123");
//
//    ConsentHandle consentHandle = new ConsentHandle();
//    //consentHandle.setConsent("existingConsentId");
//
//    Consent consent = new Consent();
//    consent.setConsentId("existingConsentId");
//    consent.setStatus(ConsentStatus.PAUSED);
//
//    // Set up the mocked interactions
//    when(repositoryFacade.getConsentHandle(anyString(), any(AAIdentifier.class)))
//        .thenReturn(null);
//    when(accountAggregatorOrchestratorService.fetchConsentArtifact(anyString()))
//        .thenReturn(consentArtifactResponse);
//    when(repositoryFacade.findByConsentId(anyString()))
//        .thenReturn(null);
//    when(objectMapper.writeValueAsString(any()))
//        .thenReturn("serializedConsent");
//
//    // Perform the update from notification
//    assertThrows(IllegalArgumentException.class, () -> fiuService.updateConsentAndHandleFromNotification(dummyRequest));
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_ActiveConsent_Existing() throws JsonProcessingException {
//    // Prepare the input
//    ConsentNotificationRequest dummyRequest = new ConsentNotificationRequest();
//
//    dummyRequest.setVer("1.0");
//    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
//    dummyRequest.setTxnid("123456");
//
//    ConsentNotificationRequest.Notifier notifier = new ConsentNotificationRequest.Notifier("email", "example@example.com");
//    dummyRequest.setNotifier(notifier);
//
//    ConsentNotificationRequest.ConsentStatusNotification consentStatusNotification = new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//        ConsentStatus.ACTIVE);
//    dummyRequest.setConsentStatusNotification(consentStatusNotification);
//    // Set up notificationRequest with required data
//    // Set up notificationRequest with required data
//
//    // Prepare the mocked objects
//    OneMoneyConsentArtifactAPIResponse consentArtifactResponse = new OneMoneyConsentArtifactAPIResponse();
//    consentArtifactResponse.setConsentDetail(new ConsentDetail());
//    consentArtifactResponse.setConsentDetailDigitalSignature("signature123");
//
//    ConsentHandle consentHandle = new ConsentHandle();
//    //consentHandle.setConsent("existingConsentId");
//
//    Consent consent = new Consent();
//    consent.setConsentId("existingConsentId");
//    consent.setStatus(ConsentStatus.PAUSED);
//
//    // Set up the mocked interactions
//    when(repositoryFacade.getConsentHandle(anyString(), any(AAIdentifier.class)))
//        .thenReturn(consentHandle);
//    when(repositoryFacade.findByConsentId(anyString()))
//        .thenReturn(consent);
//    when(objectMapper.writeValueAsString(any()))
//        .thenReturn("serializedConsent");
//
//    // Perform the update from notification
//    fiuService.updateConsentAndHandleFromNotification(dummyRequest);
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verify(repositoryFacade).findByConsentId(anyString());
//    verify(repositoryFacade).save(any(Consent.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_RejectedConsent() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.REJECTED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//
//    // Perform the update from notification
//    assertThrows(IllegalArgumentException.class, () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_PausedConsent_NotActive() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.PAUSED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//    Consent consent = new Consent();
//    consent.setStatus(ConsentStatus.REVOKED);
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_RevokedConsent_NotActiveOrPaused() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.REVOKED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//    Consent consent = new Consent();
//    consent.setStatus(ConsentStatus.REVOKED);
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_NoConsentHandleFound() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.ACTIVE);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = null;
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_ActiveConsent_NotExisting() throws JsonProcessingException {
//    ConsentNotificationRequest dummyRequest = new ConsentNotificationRequest();
//
//    dummyRequest.setVer("1.0");
//    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
//    dummyRequest.setTxnid("123456");
//
//    ConsentNotificationRequest.Notifier notifier = new ConsentNotificationRequest.Notifier("email", "example@example.com");
//    dummyRequest.setNotifier(notifier);
//
//    ConsentNotificationRequest.ConsentStatusNotification consentStatusNotification = new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//        ConsentStatus.ACTIVE);
//    dummyRequest.setConsentStatusNotification(consentStatusNotification);
//    // Set up notificationRequest with required data
//
//    // Prepare the mocked objects
//    OneMoneyConsentArtifactAPIResponse consentArtifactResponse = new OneMoneyConsentArtifactAPIResponse();
//    consentArtifactResponse.setConsentDetail(new ConsentDetail());
//    consentArtifactResponse.setConsentDetailDigitalSignature("signature123");
//
//    ConsentHandle consentHandle = new ConsentHandle();
//    //consentHandle.setConsent("existingConsentId");
//
//    Consent consent = new Consent();
//    consent.setConsentId("existingConsentId");
//    consent.setStatus(ConsentStatus.PAUSED);
//
//    // Set up the mocked interactions
//    when(repositoryFacade.getConsentHandle(anyString(), any(AAIdentifier.class)))
//        .thenReturn(null);
//    when(accountAggregatorOrchestratorService.fetchConsentArtifact(anyString()))
//        .thenReturn(consentArtifactResponse);
//    when(repositoryFacade.findByConsentId(anyString()))
//        .thenReturn(null);
//    when(objectMapper.writeValueAsString(any()))
//        .thenReturn("serializedConsent");
//
//    // Perform the update from notification
//    assertThrows(IllegalArgumentException.class, () -> fiuService.updateConsentAndHandleFromNotification(dummyRequest));
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_ActiveConsent_Existing() throws JsonProcessingException {
//    // Prepare the input
//    ConsentNotificationRequest dummyRequest = new ConsentNotificationRequest();
//
//    dummyRequest.setVer("1.0");
//    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
//    dummyRequest.setTxnid("123456");
//
//    ConsentNotificationRequest.Notifier notifier = new ConsentNotificationRequest.Notifier("email", "example@example.com");
//    dummyRequest.setNotifier(notifier);
//
//    ConsentNotificationRequest.ConsentStatusNotification consentStatusNotification = new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//        ConsentStatus.ACTIVE);
//    dummyRequest.setConsentStatusNotification(consentStatusNotification);
//    // Set up notificationRequest with required data
//    // Set up notificationRequest with required data
//
//    // Prepare the mocked objects
//    OneMoneyConsentArtifactAPIResponse consentArtifactResponse = new OneMoneyConsentArtifactAPIResponse();
//    consentArtifactResponse.setConsentDetail(new ConsentDetail());
//    consentArtifactResponse.setConsentDetailDigitalSignature("signature123");
//
//    ConsentHandle consentHandle = new ConsentHandle();
//    //consentHandle.setConsent("existingConsentId");
//
//    Consent consent = new Consent();
//    consent.setConsentId("existingConsentId");
//    consent.setStatus(ConsentStatus.PAUSED);
//
//    // Set up the mocked interactions
//    when(repositoryFacade.getConsentHandle(anyString(), any(AAIdentifier.class)))
//        .thenReturn(consentHandle);
//    when(repositoryFacade.findByConsentId(anyString()))
//        .thenReturn(consent);
//    when(objectMapper.writeValueAsString(any()))
//        .thenReturn("serializedConsent");
//
//    // Perform the update from notification
//    fiuService.updateConsentAndHandleFromNotification(dummyRequest);
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verify(repositoryFacade).findByConsentId(anyString());
//    verify(repositoryFacade).save(any(Consent.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_RejectedConsent() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.REJECTED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//
//    // Perform the update from notification
//    assertThrows(IllegalArgumentException.class, () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions and assertions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_PausedConsent_NotActive() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.PAUSED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//    Consent consent = new Consent();
//    consent.setStatus(ConsentStatus.REVOKED);
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_RevokedConsent_NotActiveOrPaused() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.REVOKED);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = new ConsentHandle();
//    Consent consent = new Consent();
//    consent.setStatus(ConsentStatus.REVOKED);
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }
//
//  @Test
//  public void testUpdateConsentAndHandleFromNotification_NoConsentHandleFound() {
//    // Prepare the input
//    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();
//    ConsentNotificationRequest.ConsentStatusNotification statusNotification =
//        new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
//            ConsentStatus.ACTIVE);
//    notificationRequest.setConsentStatusNotification(statusNotification);
//
//    // Prepare the mocked objects
//    ConsentHandle consentHandle = null;
//
//    // Perform the update from notification and assert an error is thrown
//    assertThrows(IllegalArgumentException.class,
//        () -> fiuService.updateConsentAndHandleFromNotification(notificationRequest));
//
//    // Verify the interactions
//    verify(repositoryFacade).getConsentHandle(anyString(), any(AAIdentifier.class));
//    verifyNoMoreInteractions(repositoryFacade, accountAggregatorOrchestratorService, objectMapper);
//  }


}
