package com.revly.Model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tutor extends User{
    private String subjectExpertise;
    private String teachingLanguage;
    private int yearsOfExperience;

}
