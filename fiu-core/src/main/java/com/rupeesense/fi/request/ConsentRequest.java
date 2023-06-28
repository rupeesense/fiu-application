package com.rupeesense.fi.request;

import com.rupeesense.fi.aa.AccountAggregator;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ConsentRequest {

    private String userVpa;

    private AccountAggregator accountAggregator;
//
//    private

}
