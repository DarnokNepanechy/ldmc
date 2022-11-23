package ru.dragulaxis.ldmc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.dragulaxis.ldmc.model.BonusStep;
import ru.dragulaxis.ldmc.model.PlanStep;
import ru.dragulaxis.ldmc.service.EmployeeInfoService;

@SpringBootApplication
public class LdmcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdmcApplication.class, args);
	}

	@Bean
	CommandLineRunner run(EmployeeInfoService employeeInfoService) {
		return  args -> {
			employeeInfoService.addBonusStep(new BonusStep(1L, 0,145750, 0f));
			employeeInfoService.addBonusStep(new BonusStep(2L, 145750,174250, 0.28f));
			employeeInfoService.addBonusStep(new BonusStep(3L, 174251,202751, 0.273f));
			employeeInfoService.addBonusStep(new BonusStep(4L, 202752,231252, 0.266f));
			employeeInfoService.addBonusStep(new BonusStep(5L, 231253,259753, 0.259f));
			employeeInfoService.addBonusStep(new BonusStep(6L, 259754,288254, 0.252f));
			employeeInfoService.addBonusStep(new BonusStep(7L, 288255,316755, 0.245f));
			employeeInfoService.addBonusStep(new BonusStep(8L, 316756,345256, 0.238f));
			employeeInfoService.addBonusStep(new BonusStep(9L, 345257,373757, 0.231f));
			employeeInfoService.addBonusStep(new BonusStep(10L, 373758,402258, 0.224f));
			employeeInfoService.addBonusStep(new BonusStep(11L, 402259,430759, 0.217f));
			employeeInfoService.addBonusStep(new BonusStep(12L, 430760,459260, 0.21f));
			employeeInfoService.addBonusStep(new BonusStep(13L, 459261,487761, 0.2f));
			employeeInfoService.addBonusStep(new BonusStep(14L, 487762,516262, 0.2f));
			employeeInfoService.addBonusStep(new BonusStep(15L, 487762,999999999, 0.2f));

			employeeInfoService.addPlanStep(new PlanStep(1L, 130,999999999, 0.6f));
			employeeInfoService.addPlanStep(new PlanStep(2L, 125,130, 0.8f));
			employeeInfoService.addPlanStep(new PlanStep(3L, 120,125, 1f));
			employeeInfoService.addPlanStep(new PlanStep(4L, 0,120, 1.2f));
		};
	}

}
