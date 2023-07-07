package com.rupeesense.fi.ext.onemoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest.KeyMaterial;

public class KeyMaterialGenerator {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static KeyMaterial generate() {
    try {
      return objectMapper.readValue(
          "{\"cryptoAlg\":\"ECDH\",\"curve\":\"Curve25519\",\"params\":\"string\",\"DHPublicKey\":{\"expiry\":\"2023-07-05T21:53:39.701Z\",\"Parameters\":\"string\",\"KeyValue\":\"-----BEGIN PUBLIC KEY-----MIIBMTCB6gYHKoZIzj0CATCB3gIBATArBgcqhkjOPQEBAiB/////////////////////////////////////////7TBEBCAqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqYSRShRAQge0Je0Je0Je0Je0Je0Je0Je0Je0Je0Je0JgtenHcQyGQEQQQqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq0kWiCuGaG4oIa04B7dLHdI0UySPU1+bXxhsinpxaJ+ztPZAiAQAAAAAAAAAAAAAAAAAAAAFN753qL3nNZYEmMaXPXT7QIBCANCAARXDhD4L9wYikmlHHybnW28Df57nuJkYNGiLvWbF/GsxlS0SkLsDVo7mdT0mYzygYlck5Sd9eJPhTRE2u9OABDS-----END PUBLIC KEY-----\"},\"Nonce\":\"NWJQaTFpcEc0SVN1RExNc1ZZT09WVnpiZFZZNU5Pd1I=\"}",
          KeyMaterial.class);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}