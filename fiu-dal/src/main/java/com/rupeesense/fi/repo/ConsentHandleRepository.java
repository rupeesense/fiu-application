package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.ConsentHandle;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsentHandleRepository extends JpaRepository<ConsentHandle, String> {

  ConsentHandle findByConsentHandleIdAndAccountAggregator(String consentHandleId, AAIdentifier accountAgg);

}
