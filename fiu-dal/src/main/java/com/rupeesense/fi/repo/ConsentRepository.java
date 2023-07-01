package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.AccountAggregatorIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentHandle;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsentRepository extends JpaRepository<Consent, String> {

  Consent findByConsentId(String consentId);
}
