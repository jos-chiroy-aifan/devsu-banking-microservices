package com.devsu.account.controller;

import com.devsu.account.dto.AccountStatementDto;
import com.devsu.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<AccountStatementDto>> getAccountStatement(
            @RequestParam String clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return ResponseEntity.ok(transactionService.getAccountStatement(clientId, startDate, endDate));
    }
}