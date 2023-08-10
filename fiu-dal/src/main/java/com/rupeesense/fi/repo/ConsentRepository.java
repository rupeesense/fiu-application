package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.aa.Consent;
import com.rupeesense.fi.model.aa.ConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsentRepository extends JpaRepository<Consent, String> {

  Consent findByConsentId(String consentId);

  Consent findFirstByUserIdAndStatusOrderByCreatedAtDesc(String userId, ConsentStatus status);
}
