package com.devsu.customer.mapper;

import com.devsu.customer.dto.ClientRequestDto;
import com.devsu.customer.dto.ClientResponseDto;
import com.devsu.customer.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponseDto toResponseDto(Client client) {
        return ClientResponseDto.builder()
                .personId(client.getPersonId())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phone(client.getPhone())
                .clientId(client.getClientId())
                .status(client.getStatus())
                .build();
    }

    public Client toEntity(ClientRequestDto dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setClientId(dto.getClientId());
        client.setPassword(dto.getPassword());
        client.setStatus(dto.getStatus());
        return client;
    }
}