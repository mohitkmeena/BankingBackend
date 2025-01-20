package com.mohit.financeapp.controller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.mohit.financeapp.entity.Transaction;
import com.mohit.financeapp.service.impl.BankStatement;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/statements")
public class TransactionController {
     @Autowired
     private BankStatement bankStatement;
     @GetMapping("/bankStatement")
    public ResponseEntity<List<Transaction>> getMethodName(@RequestParam String accountNumber,@RequestParam String startDate,@RequestParam String endDate,HttpServletRequest request) throws FileNotFoundException, DocumentException{ 
        //System.out.println("in transaction controller");
        
        return bankStatement.getBankStatement(accountNumber,startDate,endDate,request);
        
}
    
}
