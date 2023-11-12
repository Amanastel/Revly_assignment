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
import java.util.List;

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

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new UserException("No users found");
        }else{
            return users;
        }
    }

    @Override
    public List<User> getAllTutors() {
        List<User> tutors = userRepository.findAll();
        if(!tutors.isEmpty()){
            List<User> tutorsList = userRepository.findByUserType(UserType.TUTOR);

            if (tutorsList.isEmpty()) {
                throw new UserException("No tutors found");
            } else {
                return tutorsList;
            }
        }
        else {
            throw new UserException("No tutors found");
        }
    }

    @Override
    public List<User> getAllStudents() {
        List<User> students = userRepository.findAll();
        if(!students.isEmpty()){
            List<User> studentsList = userRepository.findByUserType(UserType.STUDENT);
            if (studentsList.isEmpty()) {
                throw new UserException("No students found");
            } else {
                return studentsList;
            }
        }
        else {
            throw new UserException("No students found");
        }
    }
}
