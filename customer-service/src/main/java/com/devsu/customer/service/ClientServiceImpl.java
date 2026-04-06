package com.devsu.customer.service;

import com.devsu.customer.dto.ClientRequestDto;
import com.devsu.customer.dto.ClientResponseDto;
import com.devsu.customer.entity.Client;
import com.devsu.customer.exception.DuplicateResourceException;
import com.devsu.customer.exception.ErrorMessages;
import com.devsu.customer.exception.ResourceNotFoundException;
import com.devsu.customer.mapper.ClientMapper;
import com.devsu.customer.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> findAll() {
        log.info("[Client] FindAll - Fetching all clients");
        List<ClientResponseDto> clients = clientRepository.findAll().stream()
                .map(clientMapper::toResponseDto)
                .collect(Collectors.toList());
        log.info("[Client] FindAll - Found: {}", clients.size());
        return clients;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto findById(Long id) {
        log.info("[Client] FindById - Id: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Client] FindById - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND_ID + id);
                });
        return clientMapper.toResponseDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto findByClientId(String clientId) {
        log.info("[Client] FindByClientId - ClientId: {}", clientId);
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> {
                    log.error("[Client] FindByClientId - Not found, ClientId: {}", clientId);
                    return new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND_CLIENT_ID + clientId);
                });
        return clientMapper.toResponseDto(client);
    }

    @Override
    public ClientResponseDto create(ClientRequestDto dto) {
        log.info("[Client] Create - Identification: {}", dto.getIdentification());

        if (clientRepository.existsByIdentification(dto.getIdentification())) {
            log.warn("[Client] Create - Duplicate identification: {}", dto.getIdentification());
            throw new DuplicateResourceException(ErrorMessages.DUPLICATE_IDENTIFICATION + dto.getIdentification());
        }

        Client client = clientMapper.toEntity(dto);
        if (client.getClientId() == null || client.getClientId().isBlank()) {
            client.setClientId(UUID.randomUUID().toString().substring(0, 8));
        }

        Client saved = clientRepository.save(client);
        log.info("[Client] Created - PersonId: {}, ClientId: {}", saved.getPersonId(), saved.getClientId());
        return clientMapper.toResponseDto(saved);
    }

    @Override
    public ClientResponseDto update(Long id, ClientRequestDto dto) {
        log.info("[Client] Update - Id: {}", id);
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Client] Update - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND_ID + id);
                });

        existing.setName(dto.getName());
        existing.setGender(dto.getGender());
        existing.setAge(dto.getAge());
        existing.setIdentification(dto.getIdentification());
        existing.setAddress(dto.getAddress());
        existing.setPhone(dto.getPhone());
        existing.setPassword(dto.getPassword());
        existing.setStatus(dto.getStatus());

        Client saved = clientRepository.save(existing);
        log.info("[Client] Updated - Id: {}, ClientId: {}", saved.getPersonId(), saved.getClientId());
        return clientMapper.toResponseDto(saved);
    }

    @Override
    public ClientResponseDto patch(Long id, Map<String, Object> fields) {
        log.info("[Client] Patch - Id: {}, Fields: {}", id, fields.keySet());
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Client] Patch - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND_ID + id);
                });

        fields.forEach((key, value) -> {
            switch (key) {
                case "name" -> existing.setName((String) value);
                case "gender" -> existing.setGender((String) value);
                case "age" -> existing.setAge((Integer) value);
                case "identification" -> existing.setIdentification((String) value);
                case "address" -> existing.setAddress((String) value);
                case "phone" -> existing.setPhone((String) value);
                case "password" -> existing.setPassword((String) value);
                case "status" -> existing.setStatus((Boolean) value);
            }
        });

        Client saved = clientRepository.save(existing);
        log.info("[Client] Patched - Id: {}, ClientId: {}", saved.getPersonId(), saved.getClientId());
        return clientMapper.toResponseDto(saved);
    }

    @Override
    public void delete(Long id) {
        log.info("[Client] Delete - Id: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[Client] Delete - Not found, Id: {}", id);
                    return new ResourceNotFoundException(ErrorMessages.CLIENT_NOT_FOUND_ID + id);
                });
        clientRepository.delete(client);
        log.info("[Client] Deleted - Id: {}, ClientId: {}", id, client.getClientId());
    }
}