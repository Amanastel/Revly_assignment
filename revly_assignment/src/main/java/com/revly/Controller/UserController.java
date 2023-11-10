package com.revly.Controller;

import com.revly.Model.User;
import com.revly.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /*

    for testing purpose only
    Student registration
    POST http://localhost:8080/user/register

            {
          "userType": "STUDENT",
          "userLanguage": "ENGLISH",
          "classGrade": "10",
          "name": "John Doe",
          "email": "john.doe@student.com",
          "password": "aman1234",
          "address": "123 Main Street, Cityville",

        }


    Teacher registration
            {
          "userType": "TUTOR",
          "subjectExpertise": "MATHS",
          "name": "Jane Tutor",
          "email": "jane.tutor@tutor.com",
          "password": "aman1234",
          "address": "456 Oak Avenue, Townsville",

        }
     */

    @PostMapping("user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

}
