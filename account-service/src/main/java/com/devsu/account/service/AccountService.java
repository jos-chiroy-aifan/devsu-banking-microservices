package com.devsu.account.service;

import com.devsu.account.dto.AccountRequestDto;
import com.devsu.account.dto.AccountResponseDto;

import java.util.List;
import java.util.Map;

public interface AccountService {

    List<AccountResponseDto> findAll();

    AccountResponseDto findById(Long id);

    AccountResponseDto findByAccountNumber(String accountNumber);

    AccountResponseDto create(AccountRequestDto requestDto);

    AccountResponseDto update(Long id, AccountRequestDto requestDto);

    AccountResponseDto patch(Long id, Map<String, Object> fields);

    void delete(Long id);
}