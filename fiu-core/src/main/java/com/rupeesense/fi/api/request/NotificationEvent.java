package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SessionNotificationEvent.class, name = "SESSION_STATUS_UPDATE"),
    @JsonSubTypes.Type(value = ConsentNotificationEvent.class, name = "CONSENT_STATUS_UPDATE")
})
@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class NotificationEvent {

  @NotBlank
  @JsonProperty("timestamp")
  private String timestamp;

  @NotBlank
  @JsonProperty("notificationId")
  private String notificationId;

  @NotNull
  @JsonProperty("type")
  private NotificationType type;


  public enum NotificationType {
    SESSION_STATUS_UPDATE,
    CONSENT_STATUS_UPDATE
  }
}
