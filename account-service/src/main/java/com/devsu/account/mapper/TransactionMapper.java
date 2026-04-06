package com.devsu.account.mapper;

import com.devsu.account.dto.AccountStatementDto;
import com.devsu.account.dto.TransactionResponseDto;
import com.devsu.account.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDto toResponseDto(Transaction transaction) {
        return TransactionResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .date(transaction.getDate())
                .transactionType(transaction.getTransactionType().name())
                .value(transaction.getValue())
                .balance(transaction.getBalance())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .build();
    }

    public AccountStatementDto toStatementDto(Transaction transaction) {
        return AccountStatementDto.builder()
                .date(transaction.getDate())
                .client(transaction.getAccount().getClientId())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .accountType(transaction.getAccount().getAccountType().name())
                .initialBalance(transaction.getAccount().getInitialBalance())
                .status(transaction.getAccount().getStatus())
                .transaction(transaction.getValue())
                .availableBalance(transaction.getBalance())
                .build();
    }
}