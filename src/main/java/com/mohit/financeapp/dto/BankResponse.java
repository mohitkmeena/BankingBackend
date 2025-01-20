package com.mohit.financeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BankResponse {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
