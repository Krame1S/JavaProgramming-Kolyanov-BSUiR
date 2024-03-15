package com.lab1.isthesiteup.services;

import org.springframework.stereotype.Service;

import com.lab1.isthesiteup.entities.UserEntity;
import com.lab1.isthesiteup.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public boolean createUser(String login, String password) {
        if (login == null || password == null) {
            return false;
        }
        UserEntity user = userRepository.findByLogin(login);
        if (user != null) {
            return false;
        }
        user = new UserEntity();
        user.setUsername(login);
        user.setPassword(password);
        userRepository.save(user);

        return true;
    }

    public Long checkUser(String login, String password) {
        if (login == null || password == null) {
            return null;
        }
        UserEntity user = userRepository.findByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user.getId(); // Возвращаем ID пользователя
    }

    
}
