package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.data.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

  List<Transaction> getTransactionByAccountAndUserId(String accountId, String userId);


  List<Transaction> getTransactionsByNarrationLikeIgnoreCase(String narrationPattern);

  default List<Transaction> getTransactionsByNarrationLike(String narration) {
    String narrationPattern = "%" + narration + "%";
    return getTransactionsByNarrationLikeIgnoreCase(narrationPattern);
  }

}
