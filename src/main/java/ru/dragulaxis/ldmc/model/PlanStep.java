package ru.dragulaxis.ldmc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "plan_steps")
public class PlanStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int min;
    int max;
    float cft;
}

