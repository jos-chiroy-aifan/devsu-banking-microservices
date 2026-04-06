package com.devsu.account.mapper;

import com.devsu.account.dto.AccountRequestDto;
import com.devsu.account.dto.AccountResponseDto;
import com.devsu.account.entity.Account;
import com.devsu.account.entity.AccountType;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponseDto toResponseDto(Account account) {
        return AccountResponseDto.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .clientId(account.getClientId())
                .build();
    }

    public Account toEntity(AccountRequestDto dto) {
        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setAccountType(AccountType.valueOf(dto.getAccountType().toUpperCase()));
        account.setInitialBalance(dto.getInitialBalance());
        account.setStatus(dto.getStatus());
        account.setClientId(dto.getClientId());
        return account;
    }
}