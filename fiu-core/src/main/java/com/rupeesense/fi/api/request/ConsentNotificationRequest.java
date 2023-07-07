package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.ConsentStatus;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ConsentNotificationRequest {

  @NotBlank
  @JsonProperty("ver")
  private String ver;

  @NotBlank
  @JsonProperty("timestamp")
  private String timestamp;

  @NotBlank
  @JsonProperty("txnid")
  private String txnid;

  @Valid
  @JsonProperty("Notifier")
  private Notifier notifier;

  @Valid
  @JsonProperty("ConsentStatusNotification")
  private ConsentStatusNotification consentStatusNotification;

  @NoArgsConstructor
  @Setter
  @Getter
  @AllArgsConstructor
  public static class Notifier {

    @NotBlank
    private String type;

    @NotBlank
    private String id;
  }

  @NoArgsConstructor
  @Setter
  @Getter
  @AllArgsConstructor
  public static class ConsentStatusNotification {

    @NotBlank
    private String consentId;

    @NotBlank
    private String consentHandle;

    @NotNull
    private ConsentStatus consentStatus;
  }

}
