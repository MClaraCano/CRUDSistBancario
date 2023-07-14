package com.webclara.pruebaspring.infraestructure.repositories;

import com.webclara.pruebaspring.domain.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {


}