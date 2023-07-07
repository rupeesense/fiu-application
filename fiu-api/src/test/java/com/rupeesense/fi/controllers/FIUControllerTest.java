package com.rupeesense.fi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.ConsentNotificationRequest;
import com.rupeesense.fi.fiu.FIUService;
import com.rupeesense.fi.model.ConsentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FIUControllerTest {

  private MockMvc mockMvc;

  @Mock
  private FIUService fiuService;

  private FIUController fiuController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    fiuController = new FIUController(fiuService);
    mockMvc = MockMvcBuilders.standaloneSetup(fiuController).build();
  }

  @Test
  public void testReceiveConsentNotificationWithInvalidRequest() throws Exception {
    ConsentNotificationRequest notificationRequest = new ConsentNotificationRequest();

    mockMvc.perform(MockMvcRequestBuilders.post("/aa/Consent/Notification")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(notificationRequest)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).updateConsentAndHandleFromNotification(any(ConsentNotificationRequest.class));
  }

  @Test
  public void testReceiveConsentNotification_Success() throws Exception {
    ConsentNotificationRequest dummyRequest = new ConsentNotificationRequest();

    dummyRequest.setVer("1.0");
    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
    dummyRequest.setTxnid("123456");

    ConsentNotificationRequest.Notifier notifier = new ConsentNotificationRequest.Notifier("email", "example@example.com");
    dummyRequest.setNotifier(notifier);

    ConsentNotificationRequest.ConsentStatusNotification consentStatusNotification = new ConsentNotificationRequest.ConsentStatusNotification("consent123", "handle123",
        ConsentStatus.ACTIVE);
    dummyRequest.setConsentStatusNotification(consentStatusNotification);

    mockMvc.perform(MockMvcRequestBuilders.post("/aa/Consent/Notification")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dummyRequest)))
        .andExpect(status().isOk());

    verify(fiuService, times(1)).updateConsentAndHandleFromNotification(any(ConsentNotificationRequest.class));
  }

  // Helper method to convert object to JSON string
  private static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
