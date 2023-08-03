package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.ConsentStatus;
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
public class ConsentNotificationEvent extends NotificationEvent {

  @JsonProperty("consentId")
  private String consentId;

  @JsonProperty("data")
  private ConsentNotificationEventData data;


  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConsentNotificationEventData {

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
