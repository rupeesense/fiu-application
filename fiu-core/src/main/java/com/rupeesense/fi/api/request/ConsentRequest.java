package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.aa.AAIdentifier;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequest {

    @NotBlank
    @JsonProperty("userVpa")
    private String userVpa;

    private AAIdentifier accountAggId = AAIdentifier.ONEMONEY;

}
