package ru.dragulaxis.ldmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dragulaxis.ldmc.entity.BonusStep;
import ru.dragulaxis.ldmc.model.EmployeeInfo;
import ru.dragulaxis.ldmc.entity.PlanStep;
import ru.dragulaxis.ldmc.model.Wage;
import ru.dragulaxis.ldmc.repositoy.BonusStepRepository;
import ru.dragulaxis.ldmc.repositoy.PlanStepRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculatorService {
    BonusStepRepository bonusStepRepository;
    PlanStepRepository planStepRepository;

    @Autowired
    public CalculatorService(BonusStepRepository bonusStepRepository, PlanStepRepository planStepRepository) {
        this.bonusStepRepository = bonusStepRepository;
        this.planStepRepository = planStepRepository;
    }

    public Wage calculateMotivation(EmployeeInfo info) {
        Wage wage = new Wage();
        wage.setBid(info.getSalary() / 22);
        // Math.ceil() округляет до ближайшего целого числа вверх
        wage.setSalaryOnWorkingDays(Math.ceil(info.getWorkingDays() * wage.getBid()));
        // Грязная маржа
        double dirtyMargin = info.getRevenue() * info.getProfitability() / 100;
        // Чистая маржа
        double pureMargin = info.getRevenue() * (info.getProfitability() / 100 - 0.1667)
                + info.getFuelAmount() * 0.1667 - info.getFuelDiscount() + info.getRecoverableVAT();
        // Мотивационная часть
        wage.setMotivationalPart(pureMargin * getBonusFactor(pureMargin) * planCompletionRate(info));
        // Итог
        wage.setTotal(wage.getMotivationalPart() > wage.getSalaryOnWorkingDays() ? wage.getMotivationalPart() + info.getDuty(): wage.getSalaryOnWorkingDays() + info.getDuty());

        return wage;
    }

    public List<BonusStep> getAllBonusSteps() {
        return bonusStepRepository.findAll().stream()
                .sorted(Comparator.comparingInt(BonusStep::getMin))
                .collect(Collectors.toList());
    }

    public Object getAllPlanSteps() {
        return planStepRepository.findAll().stream()
                .sorted(Comparator.comparingInt(PlanStep::getMin))
                .collect(Collectors.toList());
    }

    // Перебирает все диапазоны с коеффициетами. Если чило не попадает в диапазон, то коеффициент равен 0
    float getBonusFactor(double margin) {
        List<BonusStep> steps = bonusStepRepository.findAll();

        for (BonusStep step: steps) {
            if (isBetween(margin, step.getMin(), step.getMax())) return step.getCft();
        }

        return 0;
    }

    double planCompletionRate(EmployeeInfo info) {
        double percent = info.getRevenuePlan() / info.getRevenue() * 100;
        List<PlanStep> steps = planStepRepository.findAll();

        for (PlanStep step: steps) {
            if (isBetween(percent, step.getMin(), step.getMax())) return step.getCft();
        }

        return 0;
    }

    boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x < upper;
    }

    public void addBonusStep(BonusStep bonusStep) {
        bonusStepRepository.save(bonusStep);
    }

    public void addPlanStep(PlanStep planStep) {
        planStepRepository.save(planStep);
    }

    public void deletePlanStep(Long id) {
        planStepRepository.deleteById(id);
    }

    public void deleteBonusStep(Long id) {
        bonusStepRepository.deleteById(id);
    }

    public void editBonusStep(BonusStep bonusStep) {
        if (bonusStep.getId() == null || bonusStepRepository.findById(bonusStep.getId()).orElse(null) == null) {
            bonusStepRepository.save(bonusStep);
        } else {
            BonusStep update = bonusStepRepository.findById(bonusStep.getId()).orElse(null);
            update.setMin(bonusStep.getMin());
            update.setMax(bonusStep.getMax());
            update.setCft(bonusStep.getCft());
            bonusStepRepository.save(update);
        }
    }

    public void editPlanStep(PlanStep planStep) {
        if (planStep.getId() == null || planStepRepository.findById(planStep.getId()).orElse(null) == null) {
            planStepRepository.save(planStep);
        } else {
            PlanStep update = planStepRepository.findById(planStep.getId()).orElse(null);
            update.setMin(planStep.getMin());
            update.setMax(planStep.getMax());
            update.setCft(planStep.getCft());
            planStepRepository.save(update);
        }
    }
}
