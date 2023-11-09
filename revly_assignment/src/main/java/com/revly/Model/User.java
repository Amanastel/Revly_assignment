package com.revly.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private Integer id;
    private String userType;
    private String userLanguage;
    private String subjectExpertise; // For tutors
    private String classGrade; // For students
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;
    @NotNull(message = "Email cannot be null")
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    private LocalDateTime registrationDate;

}
