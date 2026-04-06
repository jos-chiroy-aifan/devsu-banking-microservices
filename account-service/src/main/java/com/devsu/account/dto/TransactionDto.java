package com.devsu.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransactionDto {

    private Long transactionId;

    private LocalDateTime date;

    @NotBlank(message = "Transaction type is required")
    private String transactionType;

    @NotNull(message = "Value is required")
    private BigDecimal value;

    private BigDecimal balance;

    @NotBlank(message = "Account number is required")
    private String accountNumber;
}