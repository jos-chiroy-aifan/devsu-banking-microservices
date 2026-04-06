package com.devsu.account.service;

import com.devsu.account.dto.AccountStatementDto;
import com.devsu.account.dto.TransactionRequestDto;
import com.devsu.account.dto.TransactionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    List<TransactionResponseDto> findAll();

    TransactionResponseDto findById(Long id);

    TransactionResponseDto create(TransactionRequestDto requestDto);

    TransactionResponseDto update(Long id, TransactionRequestDto requestDto);

    List<AccountStatementDto> getAccountStatement(String clientId, LocalDateTime startDate, LocalDateTime endDate);
}