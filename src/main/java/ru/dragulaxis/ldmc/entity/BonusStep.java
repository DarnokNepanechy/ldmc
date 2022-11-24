package ru.dragulaxis.ldmc.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bonus_steps")
public class BonusStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int min;
    int max;
    float cft;
}

