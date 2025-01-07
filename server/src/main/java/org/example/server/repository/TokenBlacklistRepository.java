package org.example.server.repository;

import org.example.server.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {
    boolean existsByToken(String token);
}
