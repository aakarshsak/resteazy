package com.sinha.resteazy.daos;

import com.sinha.resteazy.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {
    List<Token> findAllByUserEmail(String email);
}
