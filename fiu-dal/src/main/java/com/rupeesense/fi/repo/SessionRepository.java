package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.aa.Session;
import com.rupeesense.fi.model.aa.SessionStatus;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, String> {

  Session findBySessionId(String sessionId);

  Set<Session> findByConsentIdAndStatusIn(String consentId, Set<SessionStatus> status);

  Session findFirstByConsentIdOrderByCreatedAtDesc(String consentId);
}
