package com.rupeesense.fi.ext.setu.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.ConsentDetail;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class SetuConsentAPIRequest {


  @JsonProperty("redirectUrl")
  private String redirectUrl;

  @JsonProperty("context")
  private List<Context> context;

  @JsonProperty("Detail")
  private ConsentDetail consentDetail;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Context {

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;
  }
}
