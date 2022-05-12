package com.marcode.ebanking.repositories;

import com.marcode.ebanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
