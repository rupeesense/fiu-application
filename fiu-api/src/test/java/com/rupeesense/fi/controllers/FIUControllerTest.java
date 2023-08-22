package com.rupeesense.fi.controllers;

import static com.rupeesense.fi.APIConstants.AA_NOTIFICATION;
import static com.rupeesense.fi.APIConstants.FIU_CONSENT_CREATE;
import static com.rupeesense.fi.APIConstants.FIU_DATA_REQUEST_CREATE;
import static com.rupeesense.fi.controllers.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rupeesense.fi.api.request.ConsentNotificationEvent;
import com.rupeesense.fi.api.request.ConsentNotificationEvent.ConsentNotificationEventData;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.request.DataRequest;
import com.rupeesense.fi.api.request.SessionNotificationEvent;
import com.rupeesense.fi.api.request.SessionNotificationEvent.Account;
import com.rupeesense.fi.api.request.SessionNotificationEvent.Fip;
import com.rupeesense.fi.api.request.SessionNotificationEvent.SessionNotificationEventData;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.commons.FIDataRange;
import com.rupeesense.fi.fiu.FIUService;
import com.rupeesense.fi.model.aa.AAIdentifier;
import com.rupeesense.fi.model.aa.ConsentStatus;
import com.rupeesense.fi.model.aa.Session;
import com.rupeesense.fi.model.aa.SessionStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

  @Test
  public void testCreateConsent() throws Exception {
    //create a dummy object of ConsentRequest with using allArgsconstructor
    ConsentRequest request = new ConsentRequest("userVpa", AAIdentifier.ONEMONEY);
    //create a dummy object of ConsentResponse with using allArgsconstructor
    ConsentResponse response = new ConsentResponse("userVpa", AAIdentifier.ONEMONEY, "requestId", ConsentStatus.PENDING, "url");
    when(fiuService.createConsent(any(ConsentRequest.class))).thenReturn(response);

    mockMvc.perform(post("/v1/fiu" + FIU_CONSENT_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isOk())
        .andExpect(content().json(asJsonString(response)));

    verify(fiuService).createConsent(request);
  }

  @Test
  public void testCreateConsentWithBlankUserVpa() throws Exception {
    ConsentRequest request = new ConsentRequest("", AAIdentifier.ONEMONEY);

    mockMvc.perform(post("/v1/fiu" + FIU_CONSENT_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request status

    verify(fiuService, never()).createConsent(any()); // The service method should never be called
  }

  @Test
  public void testCreateConsentWithNullUserVpa() throws Exception {
    ConsentRequest request = new ConsentRequest(null, AAIdentifier.ONEMONEY);

    mockMvc.perform(post("/v1/fiu" + FIU_CONSENT_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request status

    verify(fiuService, never()).createConsent(any()); // The service method should never be called
  }

  @Test
  public void testReceiveSessionNotification() throws Exception {
    //create a dummy object of SessionNotificationEvent with using Constructor
    SessionNotificationEvent event = new SessionNotificationEvent("dummyTimestamp", "dummyNotificationId", "dummyDataSessionId",
        new SessionNotificationEventData(SessionStatus.COMPLETED, new FIDataRange(), List.of(new Fip(List.of(new Account()), "dummyFipId"))));
    doNothing().when(fiuService).receiveSessionNotification(event);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isOk());

    verify(fiuService).receiveSessionNotification(event);
  }

  @Test
  public void testReceiveSessionNotificationWithBlankDataSessionId() throws Exception {
    SessionNotificationEvent event = new SessionNotificationEvent("dummyTimestamp", "dummyNotificationId", "", null);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).receiveSessionNotification(any());
  }

  @Test
  public void testReceiveSessionNotificationWithNullData() throws Exception {
    SessionNotificationEvent event = new SessionNotificationEvent("dummyTimestamp", "dummyNotificationId", "dummyDataSessionId", null);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).receiveSessionNotification(any());
  }

  @Test
  public void testReceiveSessionNotificationWithNullStatusInData() throws Exception {
    SessionNotificationEvent event = new SessionNotificationEvent("dummyTimestamp", "dummyNotificationId", "dummyDataSessionId",
        new SessionNotificationEventData(null, new FIDataRange(), List.of(new Fip(List.of(new Account()), "dummyFipId"))));

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).receiveSessionNotification(any());
  }


  @Test
  public void testReceiveConsentNotification() throws Exception {
    //create a dummy object of ConsentNotificationEvent.java with using Constructor
    ConsentNotificationEvent event = new ConsentNotificationEvent("timestamp", "notificationId", "consentId", new ConsentNotificationEventData(ConsentStatus.PAUSED));
    doNothing().when(fiuService).updateConsent(event);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isOk());

    verify(fiuService).updateConsent(event);
  }


  @Test
  public void testReceiveConsentNotificationWithBlankConsentId() throws Exception {
    ConsentNotificationEvent event = new ConsentNotificationEvent("dummyTimestamp", "dummyNotificationId", "", null);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).updateConsent(any());
  }

  @Test
  public void testReceiveConsentNotificationWithNullData() throws Exception {
    ConsentNotificationEvent event = new ConsentNotificationEvent("dummyTimestamp", "dummyNotificationId", "dummyConsentId", null);

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).updateConsent(any());
  }

  @Test
  public void testReceiveConsentNotificationWithNullStatusInData() throws Exception {
    ConsentNotificationEvent event = new ConsentNotificationEvent("dummyTimestamp", "dummyNotificationId", "dummyConsentId",
        new ConsentNotificationEventData(null));

    mockMvc.perform(post("/v1/fiu" + AA_NOTIFICATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(event)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).updateConsent(any());
  }

  @Test
  public void testRaiseDataRequest() throws Exception {
    //create a dummy object of DataRequest.java with using Constructor
    DataRequest request = new DataRequest("userVpa", LocalDateTime.of(2023, 5, 3, 0,0, 0),
        LocalDateTime.of(2023, 6, 3, 0,0, 0));
    Session session = new Session(); // Populate with appropriate data
    when(fiuService.createDataRequest(any(DataRequest.class))).thenReturn(session);

    mockMvc.perform(post("/v1/fiu" + FIU_DATA_REQUEST_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isOk())
        .andExpect(content().json(asJsonString(session)));

    verify(fiuService).createDataRequest(request);
  }

  @Test
  public void testRaiseDataRequestWithBlankUserVpa() throws Exception {
    DataRequest request = new DataRequest("", LocalDateTime.now(), LocalDateTime.now());

    mockMvc.perform(post("/v1/fiu" + FIU_DATA_REQUEST_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).createDataRequest(any());
  }

  @Test
  public void testRaiseDataRequestWithNullFrom() throws Exception {
    DataRequest request = new DataRequest("dummyVpa@provider", null, LocalDateTime.now());

    mockMvc.perform(post("/v1/fiu" + FIU_DATA_REQUEST_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).createDataRequest(any());
  }

  @Test
  public void testRaiseDataRequestWithNullTo() throws Exception {
    DataRequest request = new DataRequest("dummyVpa@provider", LocalDateTime.now(), null);

    mockMvc.perform(post("/v1/fiu" + FIU_DATA_REQUEST_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).createDataRequest(any());
  }

  // Optionally, you can test the correct date format too
  @Test
  public void testRaiseDataRequestWithIncorrectDateFormat() throws Exception {
    String incorrectDateFormatJson = "{\"userVpa\":\"dummyVpa@provider\", \"from\":\"2023-08-10\", \"to\":\"2023-08-11\"}";

    mockMvc.perform(post("/v1/fiu" + FIU_DATA_REQUEST_CREATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(incorrectDateFormatJson))
        .andExpect(status().isBadRequest());

    verify(fiuService, never()).createDataRequest(any());
  }

}
