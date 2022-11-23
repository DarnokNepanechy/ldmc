package ru.dragulaxis.ldmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dragulaxis.ldmc.model.BonusStep;
import ru.dragulaxis.ldmc.model.EmployeeInfo;
import ru.dragulaxis.ldmc.model.PlanStep;
import ru.dragulaxis.ldmc.model.Wage;
import ru.dragulaxis.ldmc.repositoy.BonusStepRepository;
import ru.dragulaxis.ldmc.repositoy.PlanStepRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class EmployeeInfoService {
    BonusStepRepository bonusStepRepository;
    PlanStepRepository planStepRepository;
    Properties stepsProperties;
    String stepsPropertiesPath = "src/main/resources/steps.properties";

    public EmployeeInfoService() {
        stepsProperties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(stepsPropertiesPath);
            stepsProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public EmployeeInfoService(BonusStepRepository bonusStepRepository, PlanStepRepository planStepRepository) {
        this.bonusStepRepository = bonusStepRepository;
        this.planStepRepository = planStepRepository;
    }

    public String validation(EmployeeInfo info) {
        StringBuilder result = new StringBuilder();
        if (info.getRevenue() < 0.01 || info.getRevenue() > 999999999) result.append("Выручка вне диапазона (принимается от 0.01 до 999 999 999)");
        if (info.getProfitability() < 0 || info.getProfitability() > 999999999) result.append("Рентабельность вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getRevenuePlan() < 0 || info.getRevenuePlan() > 999999999) result.append("План по выручке вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getFuelAmount() < 0 || info.getFuelAmount() > 999999999) result.append("ГСМ вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getFuelDiscount() < 0 || info.getFuelDiscount() > 999999999) result.append("Скидка на ГСМ вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getDuty() < 0 || info.getDuty() > 999999999) result.append("Дежурства вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getRecoverableVAT() < -999999999 || info.getRecoverableVAT() > 999999999) result.append("НДС перевозчиков к возмещению вне диапазона (принимается от -999 999 999 до 999 999 999)");
        if (info.getSalary() < 0 || info.getSalary() > 999999999) result.append("Оклад вне диапазона (принимается от 0 до 999 999 999)");
        if (info.getWorkingDays() < 0 || info.getWorkingDays() > 999999999) result.append("Количество рабочих дней вне диапазона (принимается от 0 до 999 999 999)");

        return result.toString();
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

    // Перебирает все диапазоны с коеффициетами
    // если чило не попадает в диапазон, то коеффициент равен 0
    float getBonusFactor(double margin) {

        List<BonusStep> steps = bonusStepRepository.findAll();
        for (BonusStep step: steps) {
            if (isBetween(margin, step.getMin(), step.getMax())) return step.getCft();
        }

        // todo: можно сделать так, чтобы нашлось самый близкий диапазон
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

    public BonusStep findById(Long id) {
        return bonusStepRepository.findById(id).orElse(null);
    }

    public Object getAllPlanSteps() {
        return planStepRepository.findAll().stream()
                .sorted(Comparator.comparingInt(PlanStep::getMin))
                .collect(Collectors.toList());
    }

    public void deletePlanStep(Long id) {
        planStepRepository.deleteById(id);
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

    public void addPlanStep(PlanStep planStep) {
        planStepRepository.save(planStep);
    }
}
