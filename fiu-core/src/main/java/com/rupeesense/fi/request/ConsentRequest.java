package com.rupeesense.fi.request;

import com.rupeesense.fi.aa.AccountAggregatorIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequest {

    private String userVpa;

    private AccountAggregatorIdentifier accountAggId = AccountAggregatorIdentifier.ONEMONEY;

}
