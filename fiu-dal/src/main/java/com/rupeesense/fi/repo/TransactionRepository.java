package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
