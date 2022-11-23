package ru.dragulaxis.ldmc.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dragulaxis.ldmc.model.BonusStep;

@Repository
public interface BonusStepRepository extends JpaRepository<BonusStep, Long> {


}
