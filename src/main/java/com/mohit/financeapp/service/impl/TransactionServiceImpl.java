package com.mohit.financeapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohit.financeapp.dto.TransactionDto;
import com.mohit.financeapp.entity.Transaction;
import com.mohit.financeapp.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
      @Autowired
      private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction=Transaction.builder()
        .transactionType(transactionDto.getTransactionType())
        .amount(transactionDto.getAmount())
        .accountNumber(transactionDto.getAccountNumber())
        .status("SUCCESS")
        .build();
        transactionRepository.save(transaction);
        
    }
    
}
