package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.commons.FIDataRange;
import com.rupeesense.fi.model.SessionStatus;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionNotificationEvent extends NotificationEvent {

  @JsonProperty("dataSessionId")
  private String sessionId;

  @JsonProperty("data")
  private SessionNotificationEventData data;

  public SessionNotificationEvent(@JsonProperty("timestamp") String timestamp,
      @JsonProperty("notificationId") String notificationId,
      @JsonProperty("sessionId") String sessionId,
      @JsonProperty("data") SessionNotificationEventData data) {
    super(timestamp, notificationId, NotificationType.SESSION_STATUS_UPDATE);
    this.sessionId = sessionId;
    this.data = data;
  }

  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SessionNotificationEventData {

    @JsonProperty("status")
    private SessionStatus status;

    @JsonProperty("DataRange")
    private FIDataRange fiDataRange;

    @JsonProperty("fips")
    private List<Fip> fips;
  }


  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  public static class Account {

    @JsonProperty("linkRefNumber")
    private String linkRefNumber;

    @JsonProperty("FIStatus")
    private FIStatus FIStatus;

    @JsonProperty("description")
    private String description;
  }


  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  public static class Fip {

    @JsonProperty("accounts")
    private List<Account> accounts;

    @JsonProperty("fipID")
    private String fipID;
  }

  public enum FIStatus {
    READY,
    DENIED,
    PENDING,
    DELIVERED,
    TIMEOUT
  }

}
