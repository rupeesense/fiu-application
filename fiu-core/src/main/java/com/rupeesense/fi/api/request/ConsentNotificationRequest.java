package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConsentNotificationRequest {

  @JsonProperty("ver")
  private String ver;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("txnid")
  private String txnid;

  @JsonProperty("Notifier")
  private Notifier notifier;

  @JsonProperty("ConsentStatusNotification")
  private ConsentStatusNotification consentStatusNotification;

  //TODO: Not sure what do of this field.
  @Getter
  @AllArgsConstructor
  public static class Notifier {

    private String type;
    private String id;
  }

  @Getter
  @AllArgsConstructor
  public static class ConsentStatusNotification {

    private String consentId;
    private String consentHandleId;
    private String consentStatus;
  }

}
