package com.rupeesense.fi.ext.setu.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.commons.FIDataRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SetuDataRequest {

  @JsonProperty("consentId")
  private String consentId;

  @JsonProperty("format")
  private String format = "json";

  @JsonProperty("DataRange")
  private FIDataRange dataRange;

}
