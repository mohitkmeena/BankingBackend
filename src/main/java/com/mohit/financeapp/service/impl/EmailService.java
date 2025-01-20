package com.mohit.financeapp.service.impl;

import com.mohit.financeapp.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendEmailAttachment(EmailDetails emailDetails);
    

}
