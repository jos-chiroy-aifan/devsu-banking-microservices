package com.devsu.account.service;

import com.devsu.account.dto.AccountStatementDto;
import com.devsu.account.dto.TransactionRequestDto;
import com.devsu.account.dto.TransactionResponseDto;
import com.devsu.account.entity.Account;
import com.devsu.account.entity.Transaction;
import com.devsu.account.entity.TransactionType;
import com.devsu.account.exception.ErrorMessages;
import com.devsu.account.exception.InsufficientBalanceException;
import com.devsu.account.exception.ResourceNotFoundException;
import com.devsu.account.mapper.TransactionMapper;
import com.devsu.account.repository.AccountRepository;
import com.devsu.account.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> findAll() {
        log.info("[Transaction] FindAll - Fetching all transactions");
        List<TransactionResponseDto> transactions = transactionRepository.findAll().stream()
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("[Transaction] FindAll - Found: {}", transactions.size());
        return transactions;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto findById(Long id) {
        log.info("[Transaction] FindById - Id: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Transaction] FindById - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.TRANSACTION_NOT_FOUND_ID + id);
                });
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    public TransactionResponseDto create(TransactionRequestDto dto) {
        log.info("[Transaction] Create - AccountNumber: {}, Value: {}", dto.getAccountNumber(), dto.getValue());

        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber())
                .orElseThrow(() -> {
                    log.error("[Transaction] Create - Account not found: {}", dto.getAccountNumber());
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_NUMBER + dto.getAccountNumber());
                });

        BigDecimal currentBalance = getCurrentBalance(account);
        BigDecimal newBalance = currentBalance.add(dto.getValue());

        log.info("[Transaction] Create - CurrentBalance: {}, NewBalance: {}", currentBalance, newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[Transaction] Create - Insufficient balance, AccountNumber: {}, Attempted: {}", dto.getAccountNumber(), dto.getValue());
            throw new InsufficientBalanceException(ErrorMessages.INSUFFICIENT_BALANCE);
        }

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType(dto.getValue().compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.DEPOSIT : TransactionType.WITHDRAWAL);
        transaction.setValue(dto.getValue());
        transaction.setBalance(newBalance);
        transaction.setAccount(account);

        Transaction saved = transactionRepository.save(transaction);
        log.info("[Transaction] Created - Id: {}, Type: {}, Balance: {}", saved.getTransactionId(), saved.getTransactionType(), saved.getBalance());
        return transactionMapper.toResponseDto(saved);
    }

    @Override
    public TransactionResponseDto update(Long id, TransactionRequestDto dto) {
        log.info("[Transaction] Update - Id: {}", id);
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Transaction] Update - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.TRANSACTION_NOT_FOUND_ID + id);
                });

        existing.setValue(dto.getValue());

        Transaction saved = transactionRepository.save(existing);
        log.info("[Transaction] Updated - Id: {}, Type: {}", saved.getTransactionId(), saved.getTransactionType());
        return transactionMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountStatementDto> getAccountStatement(String clientId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("[Transaction] AccountStatement - ClientId: {}, From: {}, To: {}", clientId, startDate, endDate);

        List<Transaction> transactions = transactionRepository.findByAccount_ClientIdAndDateBetween(clientId, startDate, endDate);
        log.info("[Transaction] AccountStatement - Found: {} transactions", transactions.size());

        return transactions.stream()
                .map(transactionMapper::toStatementDto)
                .collect(Collectors.toList());
    }

    private BigDecimal getCurrentBalance(Account account) {
        List<Transaction> transactions = transactionRepository
                .findByAccountAccountIdOrderByDateDesc(account.getAccountId());

        if (transactions.isEmpty()) {
            return account.getInitialBalance();
        }
        return transactions.get(0).getBalance();
    }
}