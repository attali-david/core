package com.example.app.core.account.repository;

import java.util.List;
import java.util.Optional;

import com.example.app.core.account.model.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join user u\s
      on t.user.id = u.id\s
      where u.id = :id\s
      """)
    List<Token> findAllValidTokenByUser(Long id);

    @Transactional
    @Modifying
    @Query(value = """
      DELETE FROM Token t WHERE t.user.id = :id
      """)
    void deleteAllByUser(Long id);

    Optional<Token> findByToken(String token);
}