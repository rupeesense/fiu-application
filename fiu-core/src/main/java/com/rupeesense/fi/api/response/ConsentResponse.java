package com.rupeesense.fi.api.response;

import com.rupeesense.fi.model.aa.AAIdentifier;
import com.rupeesense.fi.model.aa.ConsentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentResponse {

    private String userVpa;

    private AAIdentifier accountAggId;

    private String requestId;

    private ConsentStatus status;

    private String redirectUrl;

}
