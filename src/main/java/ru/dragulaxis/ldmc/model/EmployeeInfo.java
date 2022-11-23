package ru.dragulaxis.ldmc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeInfo {
    // Выручка
    Double revenue;
    // Рентабельность
    Double profitability;
    // План по выручке
    Double revenuePlan;
    // ГСМ Сумма
    Double fuelAmount;
    // Скидка на ГСМ (Сумма)
    Double fuelDiscount;
    // Дежурства (Сумма)
    Double duty;
    // НДС перевозчиков к возмещению (Сумма)
    Double recoverableVAT;
    // Оклад
    Double salary;
    // Рабочих дней (натуральное число)
    Integer workingDays;
}
