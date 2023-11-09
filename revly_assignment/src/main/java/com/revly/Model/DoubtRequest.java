package com.revly.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
public class DoubtRequest {
    @Id
    private Integer id;
    private Integer studentId;
    private Integer tutorId;
    private String doubtSubject;
    private LocalDateTime timestamp;
    private String description;
    @Enumerated(EnumType.STRING)
    private DoubtResolved doubtResolved;

}
