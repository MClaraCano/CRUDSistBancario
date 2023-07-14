package com.webclara.pruebaspring.infraestructure.repositories;

import com.webclara.pruebaspring.domain.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransfersRepository extends JpaRepository<Transfer, Long> {
}