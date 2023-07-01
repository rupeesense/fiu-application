package com.rupeesense.fi;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("app.fiu")
public class FIUServiceConfig {

  @NotBlank
  private String fiuId;

}
