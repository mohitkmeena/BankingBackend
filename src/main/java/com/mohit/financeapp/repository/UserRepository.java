package com.mohit.financeapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mohit.financeapp.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
  User save(User user);
  Optional<User> findByEmail(String email);
  User findByAccountNumber(String accountNumber);
  boolean existsByAccountNumber(String accountNumber);
    
}
