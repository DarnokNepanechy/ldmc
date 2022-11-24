package ru.dragulaxis.ldmc.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wage {
    // Ставка (Вычисляемое значение=Оклад/22)
    double bid;
    // Оклад по рабочим дням (Ставка*Рабочих дней)
    double salaryOnWorkingDays;
    // Мотивационная часть
    double motivationalPart;
    // С учетом оклада (Результат работы калькулятора)(Сумма)
    double total;
}
