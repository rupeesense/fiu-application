package com.rupeesense.fi.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.AccountAggregatorIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequest {

    @JsonProperty("userVpa")
    private String userVpa;

    private AccountAggregatorIdentifier accountAggId = AccountAggregatorIdentifier.ONEMONEY;

}
