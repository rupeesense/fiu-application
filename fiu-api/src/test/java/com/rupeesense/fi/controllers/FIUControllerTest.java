package com.rupeesense.fi.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.APIConstants;
import com.rupeesense.fi.api.request.NotificationEvent;
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

//  @Test
//  public void testReceiveConsentNotificationWithInvalidRequest() throws Exception {
//    NotificationEvent notificationRequest = new NotificationEvent();
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/aa/Consent/Notification")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(asJsonString(notificationRequest)))
//        .andExpect(status().isBadRequest());
//
//    verify(fiuService, never()).updateConsentAndHandleFromNotification(any(NotificationEvent.class));
//  }
//
//  @Test
//  public void testReceiveConsentNotification_Success() throws Exception {
//    NotificationEvent dummyRequest = new NotificationEvent();
//
//    dummyRequest.setVer("1.0");
//    dummyRequest.setTimestamp("2023-07-07T10:00:00Z");
//    dummyRequest.setTxnid("123456");
//
//    NotificationEvent.Notifier notifier = new NotificationEvent.Notifier("email", "example@example.com");
//    dummyRequest.setNotifier(notifier);
//
//    NotificationEvent.ConsentStatusNotification consentStatusNotification = new NotificationEvent.ConsentStatusNotification("consent123", "handle123",
//        ConsentStatus.ACTIVE);
//    dummyRequest.setConsentStatusNotification(consentStatusNotification);
//
//    mockMvc.perform(MockMvcRequestBuilders.post(APIConstants.AA_CONSENT_NOTIFICATION)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(asJsonString(dummyRequest)))
//        .andExpect(status().isOk());
//
//    verify(fiuService, times(1)).updateConsentAndHandleFromNotification(any(NotificationEvent.class));
//  }

  // Helper method to convert object to JSON string
  private static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
