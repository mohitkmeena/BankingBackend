package com.mohit.financeapp.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mohit.financeapp.dto.BankResponse;
import com.mohit.financeapp.dto.CreditRequest;
import com.mohit.financeapp.dto.DebitRequest;
import com.mohit.financeapp.dto.EnquiryRequest;
import com.mohit.financeapp.dto.LoginDto;
import com.mohit.financeapp.dto.MoneyTransfer;
import com.mohit.financeapp.dto.UserRequest;
import com.mohit.financeapp.entity.Transaction;
import com.mohit.financeapp.entity.User;
import com.mohit.financeapp.service.impl.BankStatement;
import com.mohit.financeapp.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User account management APIs")
public class UserController {
   
    @Autowired
    private UserService userService;
        @Operation(summary = "Create a new user account",
           description = "Create a new user account"         
   )
       @ApiResponse(responseCode = "200", 
  description = "Account created successfully")
    @PostMapping(value = "/create", consumes = "application/json")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        System.out.println(userRequest.toString());
          //return userRequest.toString();
       return userService.createAccount(userRequest);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllAccounts(HttpServletRequest request){
        return userService.getAllAccounts(request);
    }
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest req,HttpServletRequest request){
        
        return userService.balanceEnquiry(req.getAccountNumber(),request);
    }
    @PostMapping("/deposit")
    public BankResponse deposit( @RequestBody CreditRequest req,HttpServletRequest request){
        return userService.deposit(req.getAccountNumber(), req.getAmount(),request);
    }
    @PostMapping("/withdraw")
    public BankResponse withdraw( @RequestBody DebitRequest req,HttpServletRequest request){
        return userService.withdraw(req.getAccountNumber(), req.getAmount(),request);
    }
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody MoneyTransfer transferReq,HttpServletRequest request){
        return userService.transfer(transferReq.getSenderAccountNumber(),transferReq.getReceiverAccountNumber(),transferReq.getAmount(),request);
    }
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto,HttpServletRequest request){
        return userService.login(loginDto,request);
    }
    @PostMapping("/logout")
    public BankResponse logout(HttpServletRequest request){
        return userService.logout(request);
    }
   
}