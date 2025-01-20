package com.mohit.financeapp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountInfo {
    private String accountName;
    private String accountNumber;
    private BigDecimal balance;
}
