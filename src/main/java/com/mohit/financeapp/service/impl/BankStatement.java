package com.mohit.financeapp.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.mohit.financeapp.dto.EmailDetails;

import com.mohit.financeapp.entity.Transaction;
import com.mohit.financeapp.entity.User;
import com.mohit.financeapp.repository.TransactionRepository;
import com.mohit.financeapp.repository.UserRepository;
import com.mohit.financeapp.utils.AccountUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankStatement {
    @Autowired

    private TransactionRepository transactionRepository;
  
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

     
    private static final String FILE_PATH="../financeapp/bankstatement.pdf";
   public ResponseEntity<List<Transaction> >getBankStatement(String accountNumber,String startDate,String endDate,HttpServletRequest request) throws FileNotFoundException, DocumentException{
       
    HttpSession session=request.getSession(false);
    User savedUser=(User) session.getAttribute("user");
   
       if(!savedUser.getAccountNumber().equals(accountNumber)) {
        
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);}
     
    LocalDate start=LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
    LocalDate end=LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
    List<Transaction> transactions= transactionRepository.findByAccountNumber(accountNumber)
    .stream()
    .filter(transaction -> transaction.getTransactionDate().toLocalDate().isAfter(start)
    &&  transaction.getTransactionDate().toLocalDate().isBefore(end))
    .toList();
    /*
     * pdf statement
     */
     
     User user=userRepository.findByAccountNumber(accountNumber);
     /*
     * customer info
     */
    String customerName=user.getFirstName()+" "+user.getSecondName()+" "+user.getOtherName();
    String customerAddress=user.getAddress();
    String customerPhone=user.getPhoneNumber();
    String customerEmail=user.getEmail();
    String customerAccountNumber=user.getAccountNumber();
    String customerGender=user.getGender();



     Rectangle rect=new Rectangle(PageSize.A4);
     Document document=new Document(rect);
     log.info("statement created size");
     OutputStream outputStream=new FileOutputStream(FILE_PATH);
     PdfWriter pdfWriter=PdfWriter.getInstance(document, outputStream);
     document.open();
     PdfPTable bankInfoTable=new PdfPTable(1);
     PdfPCell bankName=new PdfPCell(new Phrase("Engineering Bank"));
     bankName.setBorder(0);
     bankName.setBackgroundColor(BaseColor.BLUE);
     bankName.setPadding(5f);
     PdfPCell bankAddress=new PdfPCell(new Phrase("307,\nChitagni hostel,\nIIIT una \nhimachal pradesh 177209")); 
     bankAddress.setBorder(0);
     bankAddress.setBackgroundColor(BaseColor.BLUE);
     bankAddress.setPadding(20f);
     bankInfoTable.addCell(bankName);
     bankInfoTable.addCell(bankAddress);
     PdfPTable statementInfo=new PdfPTable(2);
     PdfPCell customerInfo=new PdfPCell(new Phrase("start date : "+startDate+"\n end date : "+endDate));
     customerInfo.setBorder(0);
     customerInfo.setBackgroundColor(BaseColor.WHITE);
     customerInfo.setPadding(5f);

     /*
      * account info 
      
      */
     PdfPCell accountInfo=new PdfPCell(new Phrase("account number : "+accountNumber));
     accountInfo.setBorder(0);
     accountInfo.setBackgroundColor(BaseColor.WHITE);
     accountInfo.setPadding(5f);
     PdfPCell customerNameInfo=new PdfPCell(new Phrase("customer name : "+customerName));
     customerNameInfo.setBorder(0);
     customerNameInfo.setBackgroundColor(BaseColor.WHITE);
     customerNameInfo.setPadding(05f);
     PdfPCell customerAddressInfo=new PdfPCell(new Phrase("customer address : "+customerAddress));
     customerAddressInfo.setBorder(0);
     customerAddressInfo.setBackgroundColor(BaseColor.WHITE);
     customerAddressInfo.setPadding(05f);
     PdfPCell customerPhoneInfo=new PdfPCell(new Phrase("customer phone : "+customerPhone));
     customerPhoneInfo.setBorder(0);
     customerPhoneInfo.setBackgroundColor(BaseColor.WHITE);
     customerPhoneInfo.setPadding(05f);
     PdfPCell customerEmailInfo=new PdfPCell(new Phrase("customer email : "+customerEmail));
     customerEmailInfo.setBorder(0);
     customerEmailInfo.setBackgroundColor(BaseColor.WHITE);
     customerEmailInfo.setPadding(05f);
     PdfPCell customerGenderInfo=new PdfPCell(new Phrase("customer gender : "+customerGender));
     customerGenderInfo.setBorder(0);
     customerGenderInfo.setBackgroundColor(BaseColor.WHITE);
     customerGenderInfo.setPadding(10f);
     PdfPCell space=new PdfPCell(new Phrase(" "));
     space.setBorder(0);
     space.setBackgroundColor(BaseColor.WHITE);
     space.setPadding(10f);
     
     statementInfo.addCell(customerInfo);
     statementInfo.addCell(accountInfo);
     statementInfo.addCell(customerNameInfo);
     statementInfo.addCell(customerAddressInfo);
     statementInfo.addCell(customerPhoneInfo);
     statementInfo.addCell(customerGenderInfo);
     statementInfo.addCell(customerEmailInfo);
     statementInfo.addCell(space);
     
     
     PdfPTable transactionTable=new PdfPTable(5);
     transactionTable.setWidthPercentage(100f);
     PdfPCell transactionId=new PdfPCell(new Phrase("Transaction ID"));
     transactionId.setBackgroundColor(BaseColor.BLUE);
     transactionTable.addCell(transactionId);

     PdfPCell transactionDate=new PdfPCell(new Phrase("Transaction Date"));
     transactionDate.setBackgroundColor(BaseColor.BLUE);
     PdfPCell transactionType=new PdfPCell(new Phrase("Transaction Type"));
     transactionType.setBackgroundColor(BaseColor.BLUE);
     PdfPCell transactionAmount=new PdfPCell(new Phrase("Transaction Amount"));
     transactionAmount.setBackgroundColor(BaseColor.BLUE);
     PdfPCell transactionStatus=new PdfPCell(new Phrase("Transaction Status"));
    transactionStatus.setBackgroundColor(BaseColor.BLUE);
     transactionTable.addCell(transactionId);
     transactionTable.addCell(transactionDate);
     transactionTable.addCell(transactionType);
     transactionTable.addCell(transactionAmount);
     transactionTable.addCell(transactionStatus);
      transactions.forEach(transaction -> {
          transactionTable.addCell(transaction.getTransactionId());
          transactionTable.addCell(transaction.getTransactionDate().toString());
          transactionTable.addCell(transaction.getTransactionType());
          transactionTable.addCell(transaction.getAmount().toString());
          transactionTable.addCell(transaction.getStatus());
      });

       
     document.add(bankInfoTable);
     document.add(statementInfo);
     document.add(transactionTable);
     document.close();
     log.info("statement created");
     /*
      * sending mails
      */
     EmailDetails emailDetails=EmailDetails.builder().build();
      emailDetails.setRecipient(user.getEmail());
      emailDetails.setMessage( "Dear "+user.getFirstName()+"\n your bank statement has been generated \n your account balance is "+user.getBalance());
      emailDetails.setSubject(AccountUtils.ACCOUNT_BANK_STATEMENT_MESSAGE);
      emailDetails.setAttachment(FILE_PATH);
      emailService.sendEmailAttachment(emailDetails);
      
     


    return new ResponseEntity<>(transactions,HttpStatus.OK);
   }
  
}
