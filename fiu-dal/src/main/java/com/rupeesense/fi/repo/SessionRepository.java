package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.aa.Session;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, String> {

    Session findBySessionId(String sessionId);

}
