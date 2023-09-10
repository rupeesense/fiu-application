package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.data.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

  List<Transaction> getTransactionByAccountAndUserId(String accountId, String userId);

  Optional<Transaction> findByFipIDAndFipTransactionId(String fipId, String fipTxnId);

  List<Transaction> getTransactionsByNarrationLikeIgnoreCase(String narrationPattern);

  default List<Transaction> getTransactionsByNarrationLike(String narration) {
    String narrationPattern = "%" + narration + "%";
    return getTransactionsByNarrationLikeIgnoreCase(narrationPattern);
  }

}
