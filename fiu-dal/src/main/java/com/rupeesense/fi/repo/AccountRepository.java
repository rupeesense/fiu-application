package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.data.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, String> {

  Optional<Account> findAccountByFipIDAndUserIdAndLinkRefNumber(String fipID, String userId, String linkedRefNumber);

}
