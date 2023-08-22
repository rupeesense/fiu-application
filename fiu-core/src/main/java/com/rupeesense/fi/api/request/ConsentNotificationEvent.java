package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.aa.ConsentStatus;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsentNotificationEvent extends NotificationEvent {

  @NotBlank
  @JsonProperty("consentId")
  private String consentId;

  @NotNull
  @Valid
  @JsonProperty("data")
  private ConsentNotificationEventData data;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConsentNotificationEventData {

    @NotNull
    @JsonProperty("status")
    private ConsentStatus status;
  }

  @JsonCreator
  public ConsentNotificationEvent(@JsonProperty("timestamp") String timestamp,
      @JsonProperty("notificationId") String notificationId,
      @JsonProperty("consentId") String consentId,
      @JsonProperty("data") ConsentNotificationEventData data) {
    super(timestamp, notificationId, NotificationType.CONSENT_STATUS_UPDATE);
    this.consentId = consentId;
    this.data = data;
  }
}
