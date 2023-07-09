package com.ehgus973.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehgus973.security1.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
