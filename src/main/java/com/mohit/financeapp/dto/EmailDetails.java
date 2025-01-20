package com.mohit.financeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

public class EmailDetails {
    private String recipient;
    private String message;
    private String subject;
    private String attachment; 
}
