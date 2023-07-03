package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, String> {

}
