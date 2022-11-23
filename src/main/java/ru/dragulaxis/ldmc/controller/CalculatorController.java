package ru.dragulaxis.ldmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.strategy.BaseInstantiatorStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dragulaxis.ldmc.model.EmployeeInfo;
import ru.dragulaxis.ldmc.model.BonusStep;
import ru.dragulaxis.ldmc.model.PlanStep;
import ru.dragulaxis.ldmc.model.Wage;
import ru.dragulaxis.ldmc.service.EmployeeInfoService;

@Controller
@RequestMapping
public class CalculatorController {

    EmployeeInfoService employeeInfoService;

    @Autowired
    public CalculatorController(EmployeeInfoService employeeInfoService) {
        this.employeeInfoService = employeeInfoService;
    }

    @GetMapping
    public String getCalculatorPage(Model model) {
        EmployeeInfo info = new EmployeeInfo();
        model.addAttribute("info", info);
        return "index";
    }

    @PostMapping("/calculator")
    public String getPage(Model model, @ModelAttribute(value = "info") EmployeeInfo employeeInfo) {

        String errors = employeeInfoService.validation(employeeInfo);

        System.out.println(errors);

        if (!errors.equals("")) {
            model.addAttribute("errors", errors);
        } else {
            Wage wage = employeeInfoService.calculateMotivation(employeeInfo);
            model.addAttribute("bid", String.format("%,.2f", wage.getBid()));
            model.addAttribute("salaryOnWorkingDays", String.format("%,.2f", wage.getSalaryOnWorkingDays()));
            model.addAttribute("motivationalPart", String.format("%,.0f", Math.ceil(wage.getMotivationalPart())));
            model.addAttribute("total", String.format("%,.0f", Math.ceil(wage.getTotal())));
        }

        return "index";
    }

    @GetMapping("/bonussteps")
    public String getBonusPage(Model model) {
        model.addAttribute("steps", employeeInfoService.getAllBonusSteps());

        BonusStep bonusStep = new BonusStep();
        model.addAttribute("step", bonusStep);

        return "bonussteps";
    }

    @GetMapping(path = "/bonussteps/delete/{id}")
    public String deleteBonusStep(@PathVariable("id") Long id) {
        employeeInfoService.deleteBonusStep(id);
        return "redirect:/bonussteps";
    }

    @PostMapping(path = "/bonussteps/edit")
    public String editBonusStep(@ModelAttribute(value = "step") BonusStep step) {
        employeeInfoService.editBonusStep(step);
        return "redirect:/bonussteps";
    }

    @GetMapping("/plansteps")
    public String getPlanPage(Model model) {
        model.addAttribute("steps", employeeInfoService.getAllPlanSteps());

        PlanStep planStep = new PlanStep();
        model.addAttribute("step", planStep);

        return "plansteps";
    }

    @GetMapping(path = "/plansteps/delete/{id}")
    public String deletePlanStep(@PathVariable("id") Long id) {
        employeeInfoService.deletePlanStep(id);
        return "redirect:/plansteps";
    }

    @PostMapping(path = "/plansteps/edit")
    public String editPlanStep(@ModelAttribute(value = "step") PlanStep step) {
        employeeInfoService.editPlanStep(step);
        return "redirect:/plansteps";
    }
}
