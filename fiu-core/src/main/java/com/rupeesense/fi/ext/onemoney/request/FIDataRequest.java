package com.rupeesense.fi.ext.onemoney.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.onemoney.OneMoneyUtils;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.FIDataRange;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//TODO: Try to reduce the lombok annotations, looks too much
@Setter
@NoArgsConstructor
@Builder
@Getter
@AllArgsConstructor
public class FIDataRequest extends OneMoneyRequest {

  @JsonProperty("FIDataRange")
  private FIDataRange fidataRange;

  @JsonProperty("Consent")
  private Consent consent;

  @JsonProperty("KeyMaterial")
  private KeyMaterial keyMaterial;

  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  public static class Consent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("digitalSignature")
    private String digitalSignature;
  }

  @Setter
  @NoArgsConstructor
  @Builder
  @Getter
  @AllArgsConstructor
  public static class KeyMaterial {

    @JsonProperty("cryptoAlg")
    private String cryptoAlg;

    @JsonProperty("curve")
    private String curve;

    @JsonProperty("params")
    private String params;

    @JsonProperty("DHPublicKey")
    private DHPublicKey dhPublicKey;

    @JsonProperty("Nonce")
    private String nonce;

    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class DHPublicKey {

      @JsonProperty("expiry")
      private String expiry;

      @JsonProperty("Parameters")
      private String parameters;

      @JsonProperty("KeyValue")
      private String keyValue;
    }
  }
}
