package com.revly.Service;

import com.revly.Model.User;

import java.util.List;

public interface UserService {
    public User registerUser(User user);
    public User registerOnlyTutor(User user);
    public User registerOnlyStudent(User user);
    public User getUserByEmail(String email);
    public List<User> getAllUsers();
    public List<User> getAllTutors();
    public List<User> getAllStudents();
}
