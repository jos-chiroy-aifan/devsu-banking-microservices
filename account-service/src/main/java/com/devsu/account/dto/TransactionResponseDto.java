package com.devsu.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private Long transactionId;
    private LocalDateTime date;
    private String transactionType;
    private BigDecimal value;
    private BigDecimal balance;
    private String accountNumber;
}