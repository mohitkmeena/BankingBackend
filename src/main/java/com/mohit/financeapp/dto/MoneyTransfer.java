package com.mohit.financeapp.dto;

import java.math.BigDecimal;

import org.springframework.data.convert.ReadingConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class MoneyTransfer {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}
