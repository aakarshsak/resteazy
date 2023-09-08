package com.sinha.resteazy.daos;

import com.sinha.resteazy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
