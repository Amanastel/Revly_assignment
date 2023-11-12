package com.revly.Model;

import jakarta.persistence.*;
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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Users student;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id")
    private Users tutor;
    @Enumerated(EnumType.STRING)
    private Subjects doubtSubject;
    private LocalDateTime timestamp;
    private String doubtDescription;
    @Enumerated(EnumType.STRING)
    private DoubtResolved doubtResolved;

}
