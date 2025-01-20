package com.mohit.financeapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mohit.financeapp.entity.User;
import com.mohit.financeapp.entity.UserPrincipal;
import com.mohit.financeapp.repository.UserRepository;
@Service
public class CustomerUserDetailsService implements UserDetailsService {
   @Autowired
   private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByEmail(username).orElse(null);
        if(user==null) throw new UsernameNotFoundException("User not found");
         return new UserPrincipal(user);
    }
    
}
