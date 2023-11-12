package com.revly.Service;

import com.revly.Model.Users;

import java.util.List;

public interface UserService {
    public Users registerUser(Users user);
    public Users registerOnlyTutor(Users user);
    public Users registerOnlyStudent(Users user);
    public Users getUserByEmail(String email);
    public List<Users> getAllUsers();
    public List<Users> getAllTutors();
    public List<Users> getAllStudents();
}
