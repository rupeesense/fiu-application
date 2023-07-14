package com.rupeesense.fi.ext.onemoney.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.ConsentDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class OneMoneyConsentAPIRequest extends OneMoneyRequest {

  @JsonProperty("ConsentDetail")
  private ConsentDetail consentDetail;

}
