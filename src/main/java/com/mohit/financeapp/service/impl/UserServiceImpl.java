package com.mohit.financeapp.service.impl;

import java.lang.module.ModuleDescriptor.Builder;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mohit.financeapp.dto.AccountInfo;
import com.mohit.financeapp.dto.BankResponse;
import com.mohit.financeapp.dto.EmailDetails;
import com.mohit.financeapp.dto.EnquiryRequest;
import com.mohit.financeapp.dto.LoginDto;

import com.mohit.financeapp.dto.TransactionDto;
import com.mohit.financeapp.dto.UserRequest;
import com.mohit.financeapp.entity.User;
import com.mohit.financeapp.entity.User.Role;
import com.mohit.financeapp.repository.UserRepository;
import com.mohit.financeapp.security.JwtTokenProvider;
import com.mohit.financeapp.utils.AccountUtils;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Service
public class UserServiceImpl implements UserService{
     
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
   
    private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder(12);
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
      /*
       * creating an account -save a user in the database 
       * generate a new account number
       * check if user already exists
       * return the bank response
       */
      if(userRepository.existsByEmail(userRequest.getEmail())){
        User savedUser =userRepository.findByEmail(userRequest.getEmail()).orElse(null);
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
        .accountInfo(AccountInfo.builder()
                                .accountName(savedUser.getFirstName()+" "+savedUser.getSecondName()+" "+savedUser.getOtherName())
                                .accountNumber(savedUser.getAccountNumber())
                                .balance(savedUser.getBalance())
                                .build())
        .build();
      }
      User newUser = User.builder()
                  .firstName(userRequest.getFirstName())
                  .secondName(userRequest.getSecondName())
                  .otherName(userRequest.getOtherName())
                  .email(userRequest.getEmail())
                  .gender(userRequest.getGender())
                  .phoneNumber(userRequest.getPhoneNumber())
                  .address(userRequest.getAddress())
                  .password(passwordEncoder.encode( userRequest.getPassword()))
                  .status("active")
                  .role(Role.ROLE_USER)
                  .accountNumber(AccountUtils.generateAccountNumber(userRequest.getPhoneNumber()))
                  .balance(BigDecimal.ZERO)
                  .build();
        User savedUser = userRepository.save(newUser);
        /*
         * send email notification for account creation 
         */
        EmailDetails emailDetails = EmailDetails.builder().build();
        emailDetails.setRecipient(userRequest.getEmail());
        emailDetails.setMessage("Account created successfully \n Account Name: "+savedUser.getFirstName()+" "+savedUser.getSecondName()+" "+savedUser.getOtherName()+"\n Account Number:" +savedUser.getAccountNumber());
        emailDetails.setSubject(AccountUtils.ACCOUNT_CREATED_SUBJECT );
        emailService.sendEmail(emailDetails);
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
        .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
        .accountInfo(AccountInfo.builder()
                                .accountName(savedUser.getFirstName()+" "+savedUser.getSecondName()+" "+savedUser.getOtherName())
                                .accountNumber(savedUser.getAccountNumber())
                                .balance(savedUser.getBalance())
                                .build())
        .build();
    }
    @Override
    public ResponseEntity<List<User>> getAllAccounts(HttpServletRequest request) {
      HttpSession session=request.getSession(false);
      User loggedUser=(User)session.getAttribute("user");
      if(loggedUser.getRole()!=Role.ROLE_ADMIN) return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
      return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }
    /*
     * credit debit transfer balance enquiry 
     */
    @Override
    public BankResponse balanceEnquiry(String accountNumber,HttpServletRequest request) {
      HttpSession session=request.getSession(false);
      User loggedUser=(User)session.getAttribute("user");
      if(!loggedUser.getAccountNumber().equals(accountNumber)){ return BankResponse.builder()
      .accountInfo(null)
      .responseCode("301")
      .responseMessage("you dont have access to this account")
      .build();
    }
        boolean isExist=userRepository.existsByAccountNumber(accountNumber);
         if(!isExist){
           //account not found
           return BankResponse.builder()
           .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
           .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND +" with a/c number "+accountNumber)
           .accountInfo(null)
           .build();
         }
         User user = userRepository.findByAccountNumber(accountNumber);
         EmailDetails emailDetails=EmailDetails.builder().build();
          emailDetails.setRecipient(user.getEmail());
          emailDetails.setMessage( "Dear "+user.getFirstName()+" your account balance is "+user.getBalance());
          emailDetails.setSubject(AccountUtils.ACCOUNT_BALANCE_FECTH_MESSAGE);
          emailService.sendEmail(emailDetails);
         return BankResponse.builder()
         .responseCode(AccountUtils.ACCOUNT_BALANCE_ENQUIRY_CODE)
         .responseMessage(AccountUtils.ACCOUNT_BALANCE_FECTH_MESSAGE+ " \nDear "+user.getFirstName()+" your account balance is "+user.getBalance())
         .accountInfo(AccountInfo.builder()
                               .accountName(user.getFirstName()+" "+user.getSecondName()+" "+user.getOtherName())
                               .accountNumber(user.getAccountNumber())
                               .balance(user.getBalance())
                               .build())
                               .build();
    }
    @Override
    public BankResponse deposit(String accountNumber, BigDecimal amount,HttpServletRequest request) {
      HttpSession session=request.getSession(false);
      User loggedUser=(User)session.getAttribute("user");
      if(!loggedUser.getAccountNumber().equals(accountNumber)){ return BankResponse.builder()
        .accountInfo(null)
        .responseCode("301")
        .responseMessage("you dont have access to this account")
        .build();
      }
      boolean isExist=userRepository.existsByAccountNumber(accountNumber);
      if(!isExist){
        //account not found
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
        .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND +" with a/c number "+accountNumber)
        .accountInfo(null)
        .build();
      }
      User user = userRepository.findByAccountNumber(accountNumber);
         user.setBalance(amount.add(user.getBalance()));
         userRepository.save(user);
         EmailDetails emailDetails=EmailDetails.builder().build();
          emailDetails.setRecipient(user.getEmail());
          emailDetails.setMessage( "Dear "+user.getFirstName()+"\n your account balance has been updated "+amount +" rs deposited to your account \n your account balance is "+user.getBalance());
          emailDetails.setSubject(AccountUtils.ACCOUNT_AMOUNT_CREDITED_MESSAGE);
          emailService.sendEmail(emailDetails);



          TransactionDto transactionDto=TransactionDto.builder()
          .accountNumber(user.getAccountNumber())
          .amount(amount)
          .transactionType("CREDIT")
           .build();
       transactionService.saveTransaction(transactionDto);

         return BankResponse.builder()
         .responseCode(AccountUtils.ACCOUNT_AMOUNT_CREDITED_CODE)
         .responseMessage(AccountUtils.ACCOUNT_AMOUNT_CREDITED_MESSAGE +"\n"+amount +" rs deposited to your account")
         .accountInfo(AccountInfo.builder()
                               .accountName(user.getFirstName()+" "+user.getSecondName()+" "+user.getOtherName())
                               .accountNumber(user.getAccountNumber())
                               .balance(user.getBalance())
                               .build())
                               .build();
    
    }
    @Override
    public BankResponse withdraw(String accountNumber, BigDecimal amount,HttpServletRequest request) {
      HttpSession session=request.getSession(false);
      User loggedUser=(User)session.getAttribute("user");
      if(!loggedUser.getAccountNumber().equals(accountNumber)){ return BankResponse.builder()
        .accountInfo(null)
        .responseCode("301")
        .responseMessage("you dont have access to this account")
        .build();
      }
      boolean isExist=userRepository.existsByAccountNumber(accountNumber);
      if(!isExist){
        //account not found
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
        .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND +" with a/c number "+accountNumber)
        .accountInfo(null)
        .build();
      }
      User user = userRepository.findByAccountNumber(accountNumber);
       if(user.getBalance().compareTo(amount)<0){
        EmailDetails emailDetails=EmailDetails.builder().build();
         emailDetails.setRecipient(user.getEmail());
         emailDetails.setMessage( "Dear "+user.getFirstName()+"\n your account balance is low to withdraw \n your account balance is "+user.getBalance());
         emailDetails.setSubject(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE);
         emailService.sendEmail(emailDetails);
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
        .accountInfo(null)
        .build();
      }
      user.setBalance(user.getBalance().subtract(amount));
      userRepository.save(user);
      EmailDetails emailDetails=EmailDetails.builder().build();
       emailDetails.setRecipient(user.getEmail());
       emailDetails.setMessage( "Dear "+user.getFirstName()+"\n your account balance has been updated "+amount +" rs has been withdrawn from your account \n your account balance is "+user.getBalance());
       emailDetails.setSubject(AccountUtils.ACCOUNT_AMOUNT_DEBITED_MESSAGE);
       emailService.sendEmail(emailDetails);
       TransactionDto transactionDto=TransactionDto.builder()
          .accountNumber(user.getAccountNumber())
          .amount(amount)
          .transactionType("DEBIT")
           .build();
       transactionService.saveTransaction(transactionDto);
      return BankResponse.builder()
      .responseCode(AccountUtils.ACCOUNT_AMOUNT_DEBITED_CODE)
      .responseMessage(AccountUtils.ACCOUNT_AMOUNT_DEBITED_MESSAGE +"\n"+amount +" rs has been withdrawn from your account")
      .accountInfo(AccountInfo.builder()
                            .accountName(user.getFirstName()+" "+user.getSecondName()+" "+user.getOtherName())
                            .accountNumber(user.getAccountNumber())
                            .balance(user.getBalance())
                            .build())
                            .build();
 
 }
    @Override
    public BankResponse transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount,HttpServletRequest request) {
      HttpSession session=request.getSession(false);
      User loggedUser=(User)session.getAttribute("user");
      if(!loggedUser.getAccountNumber().equals(senderAccountNumber)){ return BankResponse.builder()
        .accountInfo(null)
        .responseCode("301")
        .responseMessage("you dont have access to this account")
        .build();
      }
      boolean isSenderExist=userRepository.existsByAccountNumber(senderAccountNumber);
      if(!isSenderExist){
        //account not found
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
        .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND)
        .accountInfo(null)
        .build();
      }
      boolean isRecieverExist=userRepository.existsByAccountNumber(receiverAccountNumber);
      if(!isRecieverExist){
      
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_NOT_FOUND)
        .responseMessage(AccountUtils.RECIEVER_ACCOUNT_NOT_FOUND)
        .accountInfo(null)
        .build();
      }
      User sender = userRepository.findByAccountNumber(senderAccountNumber);
      User reciever=userRepository.findByAccountNumber(receiverAccountNumber);

      if(sender.getBalance().compareTo(amount)<0){
        EmailDetails emailDetails=EmailDetails.builder().build();
         emailDetails.setRecipient(sender.getEmail());
         emailDetails.setMessage( "Dear "+sender.getFirstName()+"\n your account balance is low to Transfer \n your account balance is "+sender.getBalance());
         emailDetails.setSubject(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE);
         emailService.sendEmail(emailDetails);
        return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
        .accountInfo(null)
        .build();
      }
      sender.setBalance(sender.getBalance().subtract(amount));
      reciever.setBalance(reciever.getBalance().add(amount));
       userRepository.save(reciever);
       userRepository.save(sender);
       EmailDetails emailDetails=EmailDetails.builder().build();
         emailDetails.setRecipient(sender.getEmail());
         emailDetails.setMessage( "Dear "+sender.getFirstName()+"\n your account balance is "+sender.getBalance() +" rs ."+amount +" rs has transfered to "+reciever.getFirstName()+" with a/c number "+"*******"+receiverAccountNumber.substring(7,10) );
         emailDetails.setSubject(AccountUtils.ACCOUNT_AMOUNT_TRANSFERED_MESSAGE);
         emailService.sendEmail(emailDetails);
         TransactionDto transactionDto=TransactionDto.builder()
         .accountNumber(reciever.getAccountNumber())
         .amount(amount)
         .transactionType("CREDIT")
          .build();
      transactionService.saveTransaction(transactionDto);
      TransactionDto transactionDto2=TransactionDto.builder()
         .accountNumber(sender.getAccountNumber())
         .amount(amount)
         .transactionType("DEBIT")
          .build();
      transactionService.saveTransaction(transactionDto2);
       return BankResponse.builder()
             .responseCode(AccountUtils.ACCOUNT_AMOUNT_TRANSFERED_CODE)
             .responseMessage(AccountUtils.ACCOUNT_AMOUNT_TRANSFERED_MESSAGE)
             .accountInfo(AccountInfo.builder()
                           .accountName(sender.getFirstName())
                           .accountNumber(senderAccountNumber)
                           .balance(sender.getBalance())
                           .build())
              .build();
    }
    
   
      
   @Autowired
   private AuthenticationManager manager;
   @Autowired
   private JwtTokenProvider jwtService;

    public BankResponse login(LoginDto loginDto,HttpServletRequest req){
       Authentication authentication=manager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
          if(authentication.isAuthenticated()){
            User user=userRepository.findByEmail(loginDto.getUsername()).get();
            HttpSession session=req.getSession();
            session.setAttribute("user", user);
            String token = jwtTokenProvider.generateToken(authentication);
            System.out.println(token);
            EmailDetails loginAlert=EmailDetails.builder()
                .subject("you logged in")
                .recipient(loginDto.getUsername())
                .message("you are logged in")
                .build();
            emailService.sendEmail(loginAlert);
            
            return BankResponse.builder()
            .accountInfo( AccountInfo.builder()
                               .accountName(user.getFirstName())
                               .accountNumber(user.getAccountNumber())
                               .balance(user.getBalance())
                               .build())
            .responseCode("200")
            .responseMessage("logged in successfully")
            .build();
          }else{
            System.out.println("in failure");
           return BankResponse.builder()
            .accountInfo( AccountInfo.builder()
                               .accountName(null)
                               .accountNumber(null)
                               .balance(null)
                               .build())
            .responseCode("404")
            .responseMessage("logged in failed")
            .build();
          }
    }
    @Override
    public BankResponse logout(HttpServletRequest request) {
      request.getSession(false).invalidate();
    if((User)request.getSession().getAttribute("user")==null){
      return BankResponse.builder()
      .accountInfo(null)
      .responseCode("200")
      .responseMessage("logged out successful")
      .build();
    }
    return BankResponse.builder()
      .accountInfo(null)
      .responseCode("401")
      .responseMessage("logged out failed")
      .build();
    }


    
}
