package com.rupeesense.fi;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("app.fiu")
public class FIUServiceConfig {

  @NotBlank
  private String fiuId;

  @NotBlank
  private String setuURI;

  @NotBlank
  private String setuClientId;

  @Value("${sm://setu-client-secret}")
  @NotBlank
  private String setuClientSecret;

}
