package com.revly.Service.impl;

import com.revly.Exception.UserException;
import com.revly.Model.Users;
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

    /**
     * Registers a new user and sets the registration date.
     *
     * @param user The user to be registered.
     * @return The registered user.
     */
    @Override
    @Transactional
    public Users registerUser(Users user) {
        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.save(user);
    }


    /**
     * Registers a user only if the user type is "ROLE_TUTOR".
     *
     * @param user The user to be registered.
     * @return The registered tutor.
     * @throws UserException if the user type is not "ROLE_TUTOR".
     */
    @Override
    public Users registerOnlyTutor(Users user) {
        if(user.getUserType().equalsIgnoreCase("ROLE_TUTOR")){
            return userRepository.save(user);
        }else{
            throw new UserException("Only tutor can register");
        }
    }


    /**
     * Registers a user only if the user type is "ROLE_STUDENT".
     *
     * @param user The user to be registered.
     * @return The registered student.
     * @throws UserException if the user type is not "ROLE_STUDENT".
     */
    @Override
    public Users registerOnlyStudent(Users user) {
        if(user.getUserType().equalsIgnoreCase("ROLE_STUDENT")){
            return userRepository.save(user);
        }else {
            throw new UserException("Only student can register");
        }
    }


    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return The user with the specified email.
     * @throws UserException if the user is not found.
     */
    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
    }


    /**
     * Retrieves a list of all users.
     *
     * @return List of all users.
     * @throws UserException if no users are found.
     */
    @Override
    public List<Users> getAllUsers() {
        List<Users> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new UserException("No users found");
        }else{
            return users;
        }
    }

    /**
     * Retrieves a list of all tutors.
     *
     * @return List of all tutors.
     * @throws UserException if no tutors are found.
     */
    @Override
    public List<Users> getAllTutors() {
        List<Users> tutors = userRepository.findAll();
        if(!tutors.isEmpty()){
            List<Users> tutorsList = userRepository.findByUserType("ROLE_TUTOR");

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


    /**
     * Retrieves a list of all students.
     *
     * @return List of all students.
     * @throws UserException if no students are found.
     */
    @Override
    public List<Users> getAllStudents() {
        List<Users> students = userRepository.findAll();
        if(!students.isEmpty()){
            List<Users> studentsList = userRepository.findByUserType("ROLE_STUDENT");
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

    @Override
    public Users deleteUser(Integer id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new UserException("User not found"));
        userRepository.delete(user);
        return user;
    }
}
