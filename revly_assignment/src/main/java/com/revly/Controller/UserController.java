package com.revly.Controller;

import com.revly.Model.Users;
import com.revly.Service.TutorAvailabilityService;
import com.revly.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
//@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public final TutorAvailabilityService tutorAvailabilityService;


    @Autowired
    public UserController(UserService userService, TutorAvailabilityService tutorAvailabilityService) {
        this.userService = userService;
        this.tutorAvailabilityService = tutorAvailabilityService;
    }




    /*

    for testing purpose only
    Student registration
    POST http://localhost:8080/users/register

            {
          "userType": "STUDENT",
          "userLanguage": "ENGLISH",
          "classGrade": "10",
          "name": "Aman Kumar",
          "email": "aman@student.com",
          "password": "aman1234",
          "address": "123 Main Street, Kanpur",

        }


    Teacher registration
            {
          "userType": "TUTOR",
          "subjectExpertise": "MATHS",
          "name": "Rahul Kumar",
          "email": "rahul.tutor@tutor.com",
          "password": "aman1234",
          "address": "456 Oak Avenue, Townsville",

        }
     */

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Handles the endpoint for user registration.
     *
     * @param user The user details for registration.
     * @return ResponseEntity with the registered user details.
     */
    @PostMapping("/users/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerUser(user));
    }


    /**
     * Handles the endpoint for registering only a tutor.
     *
     * @param user The tutor details for registration.
     * @return ResponseEntity with the registered tutor details.
     */
    @PostMapping("/users/registerOnlyTutor")
    public ResponseEntity<Users> registerOnlyTutor(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerOnlyTutor(user));
    }


    /**
     * Handles the endpoint for registering only a student.
     *
     * @param user The student details for registration.
     * @return ResponseEntity with the registered student details.
     */
    @PostMapping("/users/registerOnlyStudent")
    public ResponseEntity<Users> registerOnlyStudent(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerOnlyStudent(user));
    }


    /**
     * Handles the endpoint for retrieving all users.
     *
     * @return ResponseEntity with the list of all users.
     */
    @GetMapping("/users/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    /**
     * Handles the endpoint for retrieving all tutors.
     *
     * @return ResponseEntity with the list of all tutors.
     */
    @GetMapping("/users/getAllTutors")
    public ResponseEntity<List<Users>> getAllTutors() {
        return ResponseEntity.ok(userService.getAllTutors());
    }

    /**
     * Handles the endpoint for retrieving all students.
     *
     * @return ResponseEntity with the list of all students.
     */
    @GetMapping("users/getAllStudents")
    public ResponseEntity<List<Users>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }


    /**
     * Handles the endpoint for getting details of the logged-in user.
     *
     * @param auth The authentication object.
     * @return ResponseEntity with the details of the logged-in user.
     */
    @GetMapping("/signIn")
    public ResponseEntity<Users > getLoggedInCustomerDetailsHandler(Authentication auth) {
        Users users = userService.getUserByEmail(auth.getName());
        log.info("User: {}", auth.getName());
        if(users.getUserType().equalsIgnoreCase("ROLE_TUTOR")){
            tutorAvailabilityService.addTutorAvailability(users.getEmail());
        }
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

}
