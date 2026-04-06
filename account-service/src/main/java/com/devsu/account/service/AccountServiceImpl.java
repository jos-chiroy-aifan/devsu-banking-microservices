package com.devsu.account.service;

import com.devsu.account.dto.AccountRequestDto;
import com.devsu.account.dto.AccountResponseDto;
import com.devsu.account.entity.Account;
import com.devsu.account.entity.AccountType;
import com.devsu.account.exception.ErrorMessages;
import com.devsu.account.exception.ResourceNotFoundException;
import com.devsu.account.mapper.AccountMapper;
import com.devsu.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findAll() {
        log.info("[Account] FindAll - Fetching all accounts");
        List<AccountResponseDto> accounts = accountRepository.findAll().stream()
                .map(accountMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("[Account] FindAll - Found: {}", accounts.size());
        return accounts;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto findById(Long id) {
        log.info("[Account] FindById - Id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Account] FindById - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_ID + id);
                });
        return accountMapper.toResponseDto(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto findByAccountNumber(String accountNumber) {
        log.info("[Account] FindByAccountNumber - AccountNumber: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.error("[Account] FindByAccountNumber - Not found: {}", accountNumber);
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_NUMBER + accountNumber);
                });
        return accountMapper.toResponseDto(account);
    }

    @Override
    public AccountResponseDto create(AccountRequestDto dto) {
        log.info("[Account] Create - AccountNumber: {}, ClientId: {}", dto.getAccountNumber(), dto.getClientId());
        Account account = accountMapper.toEntity(dto);
        Account saved = accountRepository.save(account);
        log.info("[Account] Created - Id: {}, AccountNumber: {}", saved.getAccountId(), saved.getAccountNumber());
        return accountMapper.toResponseDto(saved);
    }

    @Override
    public AccountResponseDto update(Long id, AccountRequestDto dto) {
        log.info("[Account] Update - Id: {}", id);
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Account] Update - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_ID + id);
                });

        existing.setAccountNumber(dto.getAccountNumber());
        existing.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));
        existing.setInitialBalance(dto.getInitialBalance());
        existing.setStatus(dto.getStatus());
        existing.setClientId(dto.getClientId());

        Account saved = accountRepository.save(existing);
        log.info("[Account] Updated - Id: {}, AccountNumber: {}", saved.getAccountId(), saved.getAccountNumber());
        return accountMapper.toResponseDto(saved);
    }

    @Override
    public AccountResponseDto patch(Long id, Map<String, Object> fields) {
        log.info("[Account] Patch - Id: {}, Fields: {}", id, fields.keySet());
        Account existing = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Account] Patch - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_ID + id);
                });

        fields.forEach((key, value) -> {
            switch (key) {
                case "accountNumber" -> existing.setAccountNumber((String) value);
                case "accountType" -> existing.setAccountType(AccountType.valueOf(((String) value).toUpperCase()));
                case "initialBalance" -> existing.setInitialBalance(new BigDecimal(value.toString()));
                case "status" -> existing.setStatus((Boolean) value);
                case "clientId" -> existing.setClientId((String) value);
            }
        });

        Account saved = accountRepository.save(existing);
        log.info("[Account] Patched - Id: {}, AccountNumber: {}", saved.getAccountId(), saved.getAccountNumber());
        return accountMapper.toResponseDto(saved);
    }

    @Override
    public void delete(Long id) {
        log.info("[Account] Delete - Id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Account] Delete - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND_ID + id);
                });
        accountRepository.delete(account);
        log.info("[Account] Deleted - Id: {}, AccountNumber: {}", id, account.getAccountNumber());
    }
}