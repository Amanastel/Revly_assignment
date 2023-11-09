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
public class TutorAvailability {
    @Id
    private Integer id;
    private Integer tutorId;
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
    private LocalDateTime lastPingTime;
}
