package com.webclara.pruebaspring.infraestructure.repositories;

import com.webclara.pruebaspring.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}