package com.lab1.isthesiteup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lab1.isthesiteup.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLogin(String login);
}