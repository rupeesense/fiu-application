package com.rupeesense.fi.ext.setu.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.model.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetuSessionResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private SessionStatus status;

  //More fields are sent by Setu, but we ignore as we don't need them.
}
