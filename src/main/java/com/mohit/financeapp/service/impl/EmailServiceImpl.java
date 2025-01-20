package com.mohit.financeapp.service.impl;

import java.nio.file.FileSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mohit.financeapp.dto.EmailDetails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
     @Autowired
     private JavaMailSender javaMailSender;
     @Value("${spring.mail.username}")
     private String senderEMail;
     
    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEMail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessage());
            mailMessage.setSubject(emailDetails.getSubject());
            javaMailSender.send(mailMessage);
            System.out.println("mail sent successfully");
        }catch(Exception e){
            throw new RuntimeException(e);
            
        }
      
    }
   @Override
   public void sendEmailAttachment(EmailDetails emailDetails) {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper;
      try{
          mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
          mimeMessageHelper.setFrom(senderEMail);
          mimeMessageHelper.setTo(emailDetails.getRecipient());
          mimeMessageHelper.setText(emailDetails.getMessage());
          FileSystemResource fileSystemResource = new FileSystemResource(emailDetails.getAttachment());
          mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
          mimeMessageHelper.setSubject(emailDetails.getSubject());
          javaMailSender.send(mimeMessage);
          System.out.println("mail sent successfully");
          
      }catch(MessagingException e){
          throw new RuntimeException(e);
      }
       
   }

    
}
