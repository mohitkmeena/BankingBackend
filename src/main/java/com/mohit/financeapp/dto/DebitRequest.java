package com.mohit.financeapp.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DebitRequest {
    private String accountNumber;
    
    private BigDecimal amount;
}
