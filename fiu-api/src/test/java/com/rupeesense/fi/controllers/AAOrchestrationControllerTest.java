package com.rupeesense.fi.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.aa.AccountAggregatorOrchestratorService;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.ConsentHandleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AAOrchestrationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountAggregatorOrchestratorService accountAggregatorOrchestratorService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testInitiatePeriodicConsentRequest() throws Exception {
    ConsentRequest consentRequest = new ConsentRequest("test@onemoney", AAIdentifier.ONEMONEY);
    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);

    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
        .thenReturn(consentResponse);

    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
            .content(asJsonString(consentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(asJsonString(consentResponse)));
  }

  @Test
  public void testInitiatePeriodicConsentRequestWithInvalidRequest() throws Exception {
    ConsentRequest consentRequest = new ConsentRequest();
    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);

    when(accountAggregatorOrchestratorService.initiateConsent(any(ConsentRequest.class)))
        .thenReturn(consentResponse);

    mockMvc.perform(post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.RAISE_PERIODIC_CONSENT_PATH)
            .content(asJsonString(consentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testPlaceDataRequestForUser_Success() throws Exception {
    String userId = "123";

    mockMvc.perform(MockMvcRequestBuilders.post(APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH + APIConstants.PLACE_DATA_REQUEST_PATH, userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(accountAggregatorOrchestratorService).placeDataRequest(userId);
  }


  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
