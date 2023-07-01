package com.rupeesense.fi.ext.onemoney.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.onemoney.OneMoneyUtils;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.FIDataRange;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FIDataRequest {

  @JsonProperty("ver")
  private String ver;

  @JsonProperty("timestamp")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime timestamp;

  @JsonProperty("txnid")
  private String txnid;

  @JsonProperty("FIDataRange")
  private FIDataRange fidataRange;

  @JsonProperty("Consent")
  private Consent consent;

  @JsonProperty("KeyMaterial")
  private KeyMaterial keyMaterial;

  @Getter
  @AllArgsConstructor
  public static class Consent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("digitalSignature")
    private String digitalSignature;
  }

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

  public static FIDataRequest createHourlyFIDataRequestForConsent(String consentId) {
    return new FIDataRequest(
        OneMoneyUtils.getOneMoneyApiVersion(),
        OneMoneyUtils.getOneMoneyApiTimestamp(),
        OneMoneyUtils.generateUUID(),
        new FIDataRange(
            LocalDateTime.now().minusHours(1), //TODO: revisit
            LocalDateTime.now()
        ),
        new Consent(
            consentId,
            "digitalSignature"
        ),
        new KeyMaterial(
            "cryptoAlg",
            "curve",
            "params",
            new KeyMaterial.DHPublicKey(
                "expiry",
                "parameters",
                "keyValue"
            ),
            "nonce"
        ));
  }
}
