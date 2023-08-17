package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.commons.FIDataRange;
import com.rupeesense.fi.model.aa.SessionStatus;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionNotificationEvent extends NotificationEvent {

  @NotBlank
  @JsonProperty("dataSessionId")
  private String dataSessionId;

  @Valid
  @NotNull
  @JsonProperty("data")
  private SessionNotificationEventData data;

  public SessionNotificationEvent(@JsonProperty("timestamp") String timestamp,
      @JsonProperty("notificationId") String notificationId,
      @JsonProperty("dataSessionId") String dataSessionId,
      @JsonProperty("data") SessionNotificationEventData data) {
    super(timestamp, notificationId, NotificationType.SESSION_STATUS_UPDATE);
    this.dataSessionId = dataSessionId;
    this.data = data;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class SessionNotificationEventData {

    @NotNull
    @JsonProperty("status")
    private SessionStatus status;

    @NotNull
    @JsonProperty("DataRange")
    private FIDataRange fiDataRange;

    @NotNull
    @JsonProperty("fips")
    private List<Fip> fips;
  }


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Account {

    @NotBlank
    @JsonProperty("linkRefNumber")
    private String linkRefNumber;

    @NotNull
    @JsonProperty("FIStatus")
    private FIStatus FIStatus;

    @JsonProperty("description")
    private String description;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Fip {

    @NotNull
    @JsonProperty("Accounts")
    private List<Account> accounts;

    @NotBlank
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
