package com.rupeesense.fi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class AAOrchestrationControllerTest {

  @Autowired
  private MockMvc mockMvc;

//  @MockBean
//  private AccountAggregatorOrchestratorService accountAggregatorOrchestratorService;

  @Autowired
  private WebApplicationContext webApplicationContext;

//  @BeforeEach
//  public void setup() {
//    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//  }
//
////  @Test
//  public void testInitiatePeriodicConsentRequest() throws Exception {
//    ConsentRequest consentRequest = new ConsentRequest("test@onemoney", AAIdentifier.ONEMONEY);
//    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
//        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);
//
//    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
//        .thenReturn(consentResponse);
//
//    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
//            .content(asJsonString(consentRequest))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        .andExpect(content().json(asJsonString(consentResponse)));
//  }
//
//  @Test
//  public void testInitiatePeriodicConsentRequestWithInvalidRequest() throws Exception {
//    ConsentRequest consentRequest = new ConsentRequest();
//    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
//        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);
//
//    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
//        .thenReturn(consentResponse);
//
//    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
//            .content(asJsonString(consentRequest))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest());
//  }

//  @Test
//  public void testPlaceDataRequestForUser_Success() throws Exception {
//    String userId = "123";
//
//    mockMvc.perform(MockMvcRequestBuilders.post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.PLACE_DATA_REQUEST_PATH, userId)
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk());
//
//    verify(accountAggregatorOrchestratorService).placeDataRequest(userId);
//  }
//  @Mock
//  private AccountAggregatorOrchestratorService accountAggregatorOrchestratorService;
//
//  @BeforeEach
//  public void setup() {
//    MockitoAnnotations.openMocks(this);
//    AAOrchestrationController controller = new AAOrchestrationController(accountAggregatorOrchestratorService);
//    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//  }
//
//  @Test
//  public void testInitiatePeriodicConsentRequest() throws Exception {
//    ConsentRequest consentRequest = new ConsentRequest("test@onemoney", AAIdentifier.ONEMONEY);
//    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
//        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);
//
//    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
//        .thenReturn(consentResponse);
//
//    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
//            .content(asJsonString(consentRequest))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        .andExpect(content().json(asJsonString(consentResponse)));
//  }
//
//  @Test
//  public void testInitiatePeriodicConsentRequestWithInvalidRequest() throws Exception {
//    ConsentRequest consentRequest = new ConsentRequest();
//    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
//        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);
//
//    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
//        .thenReturn(consentResponse);
//
//    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
//            .content(asJsonString(consentRequest))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isBadRequest());
//  }
//
//  @Test
//  public void testPlaceDataRequestForUser_Success() throws Exception {
//    String userId = "123";
//
//    mockMvc.perform(MockMvcRequestBuilders.post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.PLACE_DATA_REQUEST_PATH, userId)
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk());
//
//    verify(accountAggregatorOrchestratorService).placeDataRequest(userId);
//  }
//>>>>>>> main


  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
