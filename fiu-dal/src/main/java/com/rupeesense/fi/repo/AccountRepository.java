package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.model.data.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, String> {

}
