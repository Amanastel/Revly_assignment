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
    POST http://localhost:8080/user/register

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

    @PostMapping("/users/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/users/registerOnlyTutor")
    public ResponseEntity<Users> registerOnlyTutor(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerOnlyTutor(user));
    }

    @PostMapping("/users/registerOnlyStudent")
    public ResponseEntity<Users> registerOnlyStudent(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType("ROLE_"+user.getUserType().toUpperCase());
        return ResponseEntity.ok(userService.registerOnlyStudent(user));
    }

    @GetMapping("/users/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/getAllTutors")
    public ResponseEntity<List<Users>> getAllTutors() {
        return ResponseEntity.ok(userService.getAllTutors());
    }

    @GetMapping("users/getAllStudents")
    public ResponseEntity<List<Users>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    @GetMapping("/signIn")
    public ResponseEntity<String > getLoggedInCustomerDetailsHandler(Authentication auth) {
        Users users = userService.getUserByEmail(auth.getName());
        log.info("User: {}", auth.getName());
        if(users.getUserType().equalsIgnoreCase("ROLE_TUTOR")){
            tutorAvailabilityService.addTutorAvailability(users.getEmail());
        }
        return new ResponseEntity<>("User: "+auth.getName(), HttpStatus.ACCEPTED);
    }

}
