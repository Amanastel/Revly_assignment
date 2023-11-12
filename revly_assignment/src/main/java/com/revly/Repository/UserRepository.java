package com.revly.Repository;

import com.revly.Model.Users;
import com.revly.Model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    public Optional<Users> findByEmail(String email);
    public List<Users> findByUserType(String userType);
}
