package ru.dragulaxis.ldmc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.dragulaxis.ldmc.entity.BonusStep;
import ru.dragulaxis.ldmc.entity.PlanStep;
import ru.dragulaxis.ldmc.service.CalculatorService;

@SpringBootApplication
public class LdmcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdmcApplication.class, args);
	}

	// Данные по умолчанию
	@Bean
	CommandLineRunner run(CalculatorService calculatorService) {
		return  args -> {
			calculatorService.addBonusStep(new BonusStep(1L, 0,145750, 0f));
			calculatorService.addBonusStep(new BonusStep(2L, 145750,174250, 0.28f));
			calculatorService.addBonusStep(new BonusStep(3L, 174251,202751, 0.273f));
			calculatorService.addBonusStep(new BonusStep(4L, 202752,231252, 0.266f));
			calculatorService.addBonusStep(new BonusStep(5L, 231253,259753, 0.259f));
			calculatorService.addBonusStep(new BonusStep(6L, 259754,288254, 0.252f));
			calculatorService.addBonusStep(new BonusStep(7L, 288255,316755, 0.245f));
			calculatorService.addBonusStep(new BonusStep(8L, 316756,345256, 0.238f));
			calculatorService.addBonusStep(new BonusStep(9L, 345257,373757, 0.231f));
			calculatorService.addBonusStep(new BonusStep(10L, 373758,402258, 0.224f));
			calculatorService.addBonusStep(new BonusStep(11L, 402259,430759, 0.217f));
			calculatorService.addBonusStep(new BonusStep(12L, 430760,459260, 0.21f));
			calculatorService.addBonusStep(new BonusStep(13L, 459261,487761, 0.2f));
			calculatorService.addBonusStep(new BonusStep(14L, 487762,516262, 0.2f));
			calculatorService.addBonusStep(new BonusStep(15L, 487762,999999999, 0.2f));

			calculatorService.addPlanStep(new PlanStep(1L, 130,999999999, 0.6f));
			calculatorService.addPlanStep(new PlanStep(2L, 125,130, 0.8f));
			calculatorService.addPlanStep(new PlanStep(3L, 120,125, 1f));
			calculatorService.addPlanStep(new PlanStep(4L, 0,120, 1.2f));
		};
	}

}
