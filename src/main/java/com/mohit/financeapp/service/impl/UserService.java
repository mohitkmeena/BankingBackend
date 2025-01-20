package com.mohit.financeapp.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mohit.financeapp.dto.BankResponse;
import com.mohit.financeapp.dto.EnquiryRequest;
import com.mohit.financeapp.dto.LoginDto;
import com.mohit.financeapp.dto.UserRequest;
import com.mohit.financeapp.entity.User;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService {

   BankResponse createAccount(UserRequest userRequest);
   BankResponse balanceEnquiry(String accountNumber,HttpServletRequest request);
   BankResponse deposit(String accountNumber, BigDecimal amount,HttpServletRequest request);
   BankResponse withdraw(String accountNumber, BigDecimal amount,HttpServletRequest request);
   BankResponse transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount,HttpServletRequest request);
   BankResponse login(LoginDto loginDto,HttpServletRequest request);
   ResponseEntity<List<User>> getAllAccounts(HttpServletRequest request);
   BankResponse logout(HttpServletRequest request);

}
