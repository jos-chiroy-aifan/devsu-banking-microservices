package com.devsu.account.repository;

import com.devsu.account.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountAccountIdOrderByDateDesc(Long accountId);

    List<Transaction> findByAccount_ClientIdAndDateBetween(
            String clientId, LocalDateTime startDate, LocalDateTime endDate);
}