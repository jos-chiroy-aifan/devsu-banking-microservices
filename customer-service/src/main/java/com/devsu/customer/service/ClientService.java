package com.devsu.customer.service;

import com.devsu.customer.dto.ClientRequestDto;
import com.devsu.customer.dto.ClientResponseDto;

import java.util.List;
import java.util.Map;

public interface ClientService {

    List<ClientResponseDto> findAll();

    ClientResponseDto findById(Long id);

    ClientResponseDto findByClientId(String clientId);

    ClientResponseDto create(ClientRequestDto requestDto);

    ClientResponseDto update(Long id, ClientRequestDto requestDto);

    ClientResponseDto patch(Long id, Map<String, Object> fields);

    void delete(Long id);
}