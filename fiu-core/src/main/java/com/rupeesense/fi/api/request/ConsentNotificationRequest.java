package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.ConsentDetail;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsentNotificationRequest {

  @NotBlank
  @JsonProperty("timestamp")
  private String timestamp;

  @NotBlank
  @JsonProperty("consentId")
  private String consentId;

  @NotNull
  @JsonProperty("data")
  private EventData data;


  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class EventData {

//    @JsonProperty("Detail")
//    public ConsentDetail consentDetail;

    @NotNull
    @JsonProperty("status")
    private ConsentStatus consentStatus;
  }

}
