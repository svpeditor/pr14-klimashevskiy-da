package ru.edu.pr11;

public class BonusCalculator {
    // граница повышенной ставки, само значение тоже относится к повышенной ставке
    public static final double KPI_BOUNDARY = 80;

    public double calculate(EmployeePerformance p) {
        validate(p);
        double rate = p.kpiPercent() >= KPI_BOUNDARY ? 0.15 : 0.05;
        if (p.rating() == 5) rate += 0.05;
        if (p.years() > 5) rate += 0.02;
        return p.salary() * rate;
    }

    // раньше отрицательный оклад давал 0, остальное не проверялось
    private void validate(EmployeePerformance p) {
        if (p == null) {
            throw new IllegalArgumentException("Нет данных сотрудника");
        }
        if (p.salary() < 0) {
            throw new IllegalArgumentException("Отрицательный оклад: " + p.salary());
        }
        if (p.kpiPercent() < 0) {
            throw new IllegalArgumentException("Отрицательный KPI: " + p.kpiPercent());
        }
        if (p.rating() < 1 || p.rating() > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5: " + p.rating());
        }
        if (p.years() < 0) {
            throw new IllegalArgumentException("Отрицательный стаж: " + p.years());
        }
    }
}
