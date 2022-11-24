package ru.dragulaxis.ldmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dragulaxis.ldmc.model.EmployeeInfo;
import ru.dragulaxis.ldmc.entity.BonusStep;
import ru.dragulaxis.ldmc.entity.PlanStep;
import ru.dragulaxis.ldmc.model.Wage;
import ru.dragulaxis.ldmc.service.CalculatorService;

@Controller
@RequestMapping
public class CalculatorController {
    CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping
    public String getCalculatorPage(Model model) {
        model.addAttribute("info", new EmployeeInfo());

        return "index";
    }

    @PostMapping("/calculator")
    public String getPage(Model model, @ModelAttribute(value = "info") EmployeeInfo employeeInfo) {
        Wage wage = calculatorService.calculateMotivation(employeeInfo);
        model.addAttribute("bid", String.format("%,.2f", wage.getBid()));
        model.addAttribute("salaryOnWorkingDays", String.format("%,.2f", wage.getSalaryOnWorkingDays()));
        model.addAttribute("motivationalPart", String.format("%,.2f", wage.getMotivationalPart()));
        model.addAttribute("total", String.format("%,.2f", wage.getTotal()));

        return "index";
    }

    @GetMapping("/bonussteps")
    public String getBonusPage(Model model) {
        model.addAttribute("url", "/bonussteps");
        model.addAttribute("steps", calculatorService.getAllBonusSteps());
        model.addAttribute("step", new BonusStep());

        return "steps";
    }

    @GetMapping("/bonussteps/delete/{id}")
    public String deleteBonusStep(@PathVariable("id") Long id) {
        calculatorService.deleteBonusStep(id);

        return "redirect:/bonussteps";
    }

    @PostMapping("/bonussteps/edit")
    public String editBonusStep(@ModelAttribute(value = "step") BonusStep step) {
        calculatorService.editBonusStep(step);

        return "redirect:/bonussteps";
    }

    @GetMapping("/plansteps")
    public String getPlanPage(Model model) {
        model.addAttribute("url", "/plansteps");
        model.addAttribute("steps", calculatorService.getAllPlanSteps());
        model.addAttribute("step",  new PlanStep());

        return "steps";
    }

    @GetMapping("/plansteps/delete/{id}")
    public String deletePlanStep(@PathVariable("id") Long id) {
        calculatorService.deletePlanStep(id);

        return "redirect:/plansteps";
    }

    @PostMapping("/plansteps/edit")
    public String editPlanStep(@ModelAttribute(value = "step") PlanStep step) {
        calculatorService.editPlanStep(step);

        return "redirect:/plansteps";
    }
}
