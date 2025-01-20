package com.mohit.financeapp.service.impl;

import com.mohit.financeapp.dto.TransactionDto;


public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
