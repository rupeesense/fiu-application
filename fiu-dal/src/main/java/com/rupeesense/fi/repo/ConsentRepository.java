package com.rupeesense.fi.repo;

import com.rupeesense.fi.model.ConsentHandle;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsentRepository extends JpaRepository<ConsentHandle, Long> {

}
