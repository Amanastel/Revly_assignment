package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.User;
import com.revly.Model.UserType;
import com.revly.Repository.UserRepository;
import com.revly.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public User registerUser(User user) {
        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User registerOnlyTutor(User user) {
        if(user.getUserType().equals(UserType.TUTOR)){
            return userRepository.save(user);
        }else{
            throw new UserException("Only tutor can register");
        }
    }

    @Override
    public User registerOnlyStudent(User user) {
        if(user.getUserType().equals(UserType.STUDENT)){
            return userRepository.save(user);
        }else {
            throw new UserException("Only student can register");
        }
    }
}
