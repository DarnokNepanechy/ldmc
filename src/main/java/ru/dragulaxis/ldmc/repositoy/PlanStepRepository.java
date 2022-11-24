package ru.dragulaxis.ldmc.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dragulaxis.ldmc.entity.PlanStep;

@Repository
public interface PlanStepRepository extends JpaRepository<PlanStep, Long> { }
