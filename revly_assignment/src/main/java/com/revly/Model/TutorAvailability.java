package com.revly.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TutorAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
    private LocalDateTime lastPingTime;
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;
}
