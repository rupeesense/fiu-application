package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.aa.Consent;
import com.rupeesense.fi.model.aa.ConsentStatus;
import com.rupeesense.fi.model.aa.Session;
import com.rupeesense.fi.model.data.Account;
import com.rupeesense.fi.model.data.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFacade {

  private final ConsentRepository consentRepository;
  private final SessionRepository sessionRepository;

  private final AccountRepository accountRepository;

  private final TransactionRepository transactionRepository;

  @Autowired
  public RepositoryFacade(ConsentRepository consentRepository, SessionRepository sessionRepository,
      AccountRepository accountRepository, TransactionRepository transactionRepository) {
    this.consentRepository = consentRepository;
    this.sessionRepository = sessionRepository;
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public Consent findActiveConsentByUserId(String userId) {
    return consentRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId, ConsentStatus.ACTIVE);
  }

  public Optional<Account> getAccountIfItExists(String fipID, String userId, String linkRefNumber) {
    return accountRepository.findAccountByFipIDAndUserIdAndLinkRefNumber(fipID, userId, linkRefNumber);
  }

  public List<Transaction> getTransactionsForAccountAndUser(String accountId, String userId) {
    return transactionRepository.getTransactionByAccountIdAndUserId(accountId, userId);
  }


  public Session getSession(String sessionId) {
    return sessionRepository.findBySessionId(sessionId);
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

  public void saveAccounts(List<Account> accounts) {
    accountRepository.saveAll(accounts);
  }

  public void saveAccount(Account account) {
    accountRepository.save(account);
  }

  public void saveTransactions(Set<Transaction> transactions) {
    transactionRepository.saveAll(transactions);
  }
}
