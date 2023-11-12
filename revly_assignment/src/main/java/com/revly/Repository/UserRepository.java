package com.revly.Repository;

import com.revly.Model.User;
import com.revly.Model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByEmail(String email);
    public List<User> findByUserType(UserType userType);
}
