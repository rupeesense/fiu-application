package com.rupeesense.fi.fiu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.aa.AccountAggregatorOrchestratorService;
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

  private AccountAggregatorOrchestratorService accountAggregatorOrchestratorService;

  private FIUService fiuService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    fiuService = new FIUService(repositoryFacade, setuFIUService, objectMapper, setuRequestGenerator);
  }
  
  @Test
  public void testCreateConsent() {
    // Prepare the input and mocked objects
    ConsentRequest consentRequest = new ConsentRequest();
    consentRequest.setUserVpa("userVpa");
    SetuConsentAPIRequest consentAPIRequest = new SetuConsentAPIRequest();
    SetuConsentInitiateResponse consentAPIResponse = new SetuConsentInitiateResponse();
    consentAPIResponse.setId("consentId");
    consentAPIResponse.setStatus(ConsentStatus.ACTIVE);
  
    // Set up the mocked interactions
    when(setuRequestGenerator.generateConsentRequest(anyString())).thenReturn(consentAPIRequest);
    when(setuFIUService.initiateConsent(any(SetuConsentAPIRequest.class))).thenReturn(consentAPIResponse);
  
    // Call the method under test
    ConsentResponse consentResponse = fiuService.createConsent(consentRequest);
  
    // Assert the expected outcome
    assertEquals("userVpa", consentResponse.getUserId());
    assertEquals(AAIdentifier.ONEMONEY, consentResponse.getAccountAggregator());
    assertEquals("consentId", consentResponse.getConsentId());
    assertEquals(ConsentStatus.ACTIVE, consentResponse.getStatus());
  
    // Verify the interactions
    verify(setuRequestGenerator).generateConsentRequest(anyString());
    verify(setuFIUService).initiateConsent(any(SetuConsentAPIRequest.class));
    verify(repositoryFacade).save(any(Consent.class));
  }
  
  @Test
  public void testCreateDataRequest() {
    // Prepare the input and mocked objects
    DataRequest dataRequest = new DataRequest();
    dataRequest.setUserVpa("userVpa");
    Consent consent = new Consent();
    consent.setConsentId("consentId");
    consent.setAccountAggregator(AAIdentifier.ONEMONEY);
    SetuDataRequest setuDataRequest = new SetuDataRequest();
    SetuSessionResponse response = new SetuSessionResponse();
    response.setId("sessionId");
    response.setStatus(SessionStatus.ACTIVE);
  
    // Set up the mocked interactions
    when(repositoryFacade.findActiveConsentByUserId(anyString())).thenReturn(consent);
    when(setuRequestGenerator.generateDataRequest(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(setuDataRequest);
    when(setuFIUService.createDataRequest(any(SetuDataRequest.class))).thenReturn(response);
  
    // Call the method under test
    Session session = fiuService.createDataRequest(dataRequest);
  
    // Assert the expected outcome
    assertEquals("sessionId", session.getSessionId());
    assertEquals(consent, session.getConsent());
    assertEquals(AAIdentifier.ONEMONEY, session.getAccountAggregator());
    assertEquals(SessionStatus.ACTIVE, session.getStatus());
    assertEquals("userVpa", session.getUserId());
  
    // Verify the interactions
    verify(repositoryFacade).findActiveConsentByUserId(anyString());
    verify(setuRequestGenerator).generateDataRequest(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    verify(setuFIUService).createDataRequest(any(SetuDataRequest.class));
    verify(repositoryFacade).save(any(Session.class));
  }
  
  @Test
  public void testReceiveSessionNotification() {
    // Prepare the input and mocked objects
    SessionNotificationEvent sessionNotificationEvent = new SessionNotificationEvent();
    sessionNotificationEvent.setDataSessionId("sessionId");
    SessionNotificationEvent.Data data = new SessionNotificationEvent.Data();
    data.setStatus(SessionStatus.COMPLETED);
    sessionNotificationEvent.setData(data);
    Session session = new Session();
  
    // Set up the mocked interactions
    when(repositoryFacade.getSession(anyString())).thenReturn(session);
  
    // Call the method under test
    fiuService.receiveSessionNotification(sessionNotificationEvent);
  
    // Assert the expected outcome
    assertEquals(SessionStatus.COMPLETED, session.getStatus());
  
    // Verify the interactions
    verify(repositoryFacade).getSession(anyString());
    verify(repositoryFacade).save(any(Session.class));
  }
  
  @Test
  public void testGetAndSaveData() {
    // Prepare the input and mocked objects
    Session session = new Session();
    session.setSessionId("sessionId");
    session.setUserId("userVpa");
    SetuDataResponse setuDataResponse = new SetuDataResponse();
    List<SetuDataResponse.DataPayload> dataPayloads = new ArrayList<>();
    SetuDataResponse.DataPayload dataPayload = new SetuDataResponse.DataPayload();
    dataPayload.setFipId("fipId");
    List<SetuDataResponse.AccountLevelData> accountLevelDataList = new ArrayList<>();
    SetuDataResponse.AccountLevelData accountLevelData = new SetuDataResponse.AccountLevelData();
    accountLevelData.setLinkRefNumber("linkRefNumber");
    accountLevelData.setMaskedAccNumber("maskedAccNumber");
    SetuDataResponse.AccountData accountData = new SetuDataResponse.AccountData();
    SetuDataResponse.Summary summary = new SetuDataResponse.Summary();
    summary.setBranch("branch");
    summary.setCurrentODLimit("currentODLimit");
    summary.setDrawingLimit("drawingLimit");
    summary.setIfscCode("ifscCode");
    summary.setMicrCode("micrCode");
    summary.setCurrency("currency");
    summary.setCurrentBalance("currentBalance");
    summary.setOpeningDate("2022-01-01T00:00:00Z");
    summary.setBalanceDateTime("2022-01-01T00:00:00Z");
    summary.setStatus("ACTIVE");
    accountData.setSummary(summary);
    SetuDataResponse.Profile profile = new SetuDataResponse.Profile();
    SetuDataResponse.Holders holders = new SetuDataResponse.Holders();
    holders.setType("type");
    List<SetuDataResponse.Profile.Holder> holderList = new ArrayList<>();
    SetuDataResponse.Profile.Holder holder = new SetuDataResponse.Profile.Holder();
    holder.setAddress("address");
    holder.setCkycCompliance("ckycCompliance");
    holder.setDob("dob");
    holder.setEmail("email");
    holder.setMobile("mobile");
    holder.setName("name");
    holder.setNominee("nominee");
    holder.setPan("pan");
    holderList.add(holder);
    holders.setHolder(holderList);
    profile.setHolders(holders);
    accountData.setProfile(profile);
    accountData.setType("type");
    SetuDataResponse.Transactions transactions = new SetuDataResponse.Transactions();
    List<SetuDataResponse.Transactions.Transaction> transactionList = new ArrayList<>();
    SetuDataResponse.Transactions.Transaction transaction = new SetuDataResponse.Transactions.Transaction();
    transaction.setTxnId("txnId");
    transaction.setAmount("amount");
    transaction.setNarration("narration");
    transaction.setType("type");
    transaction.setMode("mode");
    transaction.setCurrentBalance("currentBalance");
    transaction.setTransactionTimestamp("2022-01-01T00:00:00Z");
    transaction.setValueDate("2022-01-01T00:00:00Z");
    transaction.setReference("reference");
    transactionList.add(transaction);
    transactions.setTransaction(transactionList);
    accountData.setTransactions(transactions);
    accountLevelData.setInformationPayload(accountData);
    accountLevelDataList.add(accountLevelData);
    dataPayload.setData(accountLevelDataList);
    dataPayloads.add(dataPayload);
    setuDataResponse.setDataPayload(dataPayloads);
  
    // Set up the mocked interactions
    when(setuFIUService.getData(anyString())).thenReturn(setuDataResponse);
    when(repositoryFacade.getAccountIfItExists(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
  
    // Call the method under test
    fiuService.getAndSaveData(session);
  
    // Verify the interactions
    verify(setuFIUService).getData(anyString());
    verify(repositoryFacade, times(dataPayloads.size())).getAccountIfItExists(anyString(), anyString(), anyString());
    verify(repositoryFacade, times(dataPayloads.size())).saveAccount(any(Account.class));
    verify(repositoryFacade, times(dataPayloads.size())).saveTransactions(anySet());
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