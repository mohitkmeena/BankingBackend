package com.mohit.financeapp.utils;

import java.time.Year;

public class AccountUtils {
    /*
     * generate a account number
     * 2023 + phonenumber[3:10]
     */
   public static final String ACCOUNT_EXISTS_CODE="004";
   public static final String ACCOUNT_EXISTS_MESSAGE="Account already exists";

   public static final String ACCOUNT_CREATED_CODE="001";
   public static final String ACCOUNT_CREATED_MESSAGE="Account created Successfully";
   public static final String ACCOUNT_CREATED_SUBJECT = "Account have been created";
   public static final String ACCOUNT_NOT_FOUND_CODE=" 008";
   public static final String ACCOUNT_NOT_FOUND=" user account not found";
   public static final String RECIEVER_ACCOUNT_NOT_FOUND=" reciever account not found";
   public static final String ACCOUNT_BALANCE_ENQUIRY_CODE="005";
   public static final String ACCOUNT_BALANCE_FECTH_MESSAGE=" balance fetched successfully";
   public static final String ACCOUNT_AMOUNT_CREDITED_CODE="009";
   public static final String ACCOUNT_AMOUNT_CREDITED_MESSAGE=" amount credited successfully";
   public static final String ACCOUNT_AMOUNT_DEBITED_CODE="010";
   public static final String ACCOUNT_AMOUNT_DEBITED_MESSAGE=" amount debited successfully";
   public static final String ACCOUNT_AMOUNT_TRANSFERED_CODE="011";
   public static final String ACCOUNT_AMOUNT_TRANSFERED_MESSAGE=" transfered successfully";
   public static final String ACCOUNT_INSUFFICIENT_BALANCE_CODE="002";
   public static final String ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE=" Your balance is low";
public static final String ACCOUNT_BANK_STATEMENT_MESSAGE = "Bank statement generated successfully";
   
    
   private static Year currentYear=Year.now();
    public static String generateAccountNumber(String phoneNumber){

        return currentYear.getValue()+phoneNumber.substring(3,10);
    }
}
