package com.revly.Model;

import jakarta.persistence.*;
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
public class DoubtRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users student;
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Users tutor;
    @Enumerated(EnumType.STRING)
    private Subjects doubtSubject;
    private LocalDateTime timestamp;

    @NotNull(message = "Doubt description cannot be null")
    @Size(min = 3, max = 1000, message = "Doubt description must be between 3 and 1000 characters")
    private String doubtDescription;
    @Enumerated(EnumType.STRING)
    private DoubtResolved doubtResolved;

}
