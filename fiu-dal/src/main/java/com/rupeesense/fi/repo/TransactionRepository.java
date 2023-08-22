package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.data.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

  List<Transaction> getTransactionByAccountIdAndUserId(String accountId, String userId);

}
