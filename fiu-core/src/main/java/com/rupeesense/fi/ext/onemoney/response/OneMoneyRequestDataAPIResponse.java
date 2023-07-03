package com.rupeesense.fi.ext.onemoney.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest.ConsentDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class OneMoneyRequestDataAPIResponse {

  @JsonProperty("ver")
  private String version;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("txnid")
  private String transactionId;

  @JsonProperty("consentId")
  private String consentId;

  @JsonProperty("sessionId")
  private String sessionId;

}
