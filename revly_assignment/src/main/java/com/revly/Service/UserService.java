package com.revly.Service;

import com.revly.Model.User;

public interface UserService {
    public User registerUser(User user);
    public User registerOnlyTutor(User user);
    public User registerOnlyStudent(User user);
}
