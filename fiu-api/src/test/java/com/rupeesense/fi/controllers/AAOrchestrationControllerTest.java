package com.rupeesense.fi.controllers;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
    ConsentRequest consentRequest = new ConsentRequest();
    ConsentResponse consentResponse = new ConsentResponse("test@onemoney",
        AAIdentifier.ONEMONEY, "requestId", ConsentHandleStatus.PENDING);

    when(accountAggregatorOrchestratorService.initiateConsent(eq(consentRequest))).thenReturn(consentResponse);

    mockMvc.perform(post("/v1/account_aggregator/consent/periodic")
            .content(asJsonString(consentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(asJsonString(consentResponse)));
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
