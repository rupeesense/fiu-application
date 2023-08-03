package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentHandle;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFacade {

  private final ConsentRepository consentRepository;
  private final ConsentHandleRepository consentHandleRepository;

  private final SessionRepository sessionRepository;

  @Autowired
  public RepositoryFacade(ConsentRepository consentRepository, ConsentHandleRepository consentHandleRepository, SessionRepository sessionRepository) {
    this.consentRepository = consentRepository;
    this.consentHandleRepository = consentHandleRepository;
    this.sessionRepository = sessionRepository;
  }

  public Consent findActiveConsentByUserId(String userId) {
    return consentRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId, ConsentStatus.ACTIVE);
  }

  public ConsentHandle getConsentHandle(String consentHandleId, AAIdentifier aaIdentifier) {
    return consentHandleRepository.findByConsentHandleIdAndAccountAggregator(consentHandleId, aaIdentifier);
  }

  public Consent findByConsentId(String consentId) {
    return consentRepository.findByConsentId(consentId);
  }

  public void save(Consent consent) {
    consentRepository.save(consent);
  }

  public void save(Session session) {
    sessionRepository.save(session);
  }

}
