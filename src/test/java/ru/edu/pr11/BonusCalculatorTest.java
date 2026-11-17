package ru.edu.pr11;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BonusCalculatorTest {
    private static final double DELTA = 0.001;
    // новый объект для каждого теста, тесты не зависят друг от друга
    private final BonusCalculator calculator = new BonusCalculator();

    // дефект 1: отрицательный оклад давал 0 вместо ошибки
    @Test
    void negativeSalaryMustBeRejected() {
        EmployeePerformance p = new EmployeePerformance(-1, 90, 4, 2);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(p));
    }

    // дефект 2: отрицательный KPI принимался и давал 5 %
    @Test
    void negativeKpiMustBeRejected() {
        EmployeePerformance p = new EmployeePerformance(100000, -1, 4, 2);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(p));
    }

    // дефект 3: рейтинг вне 1..5 принимался
    @ParameterizedTest
    @ValueSource(ints = {0, -1, 6})
    void ratingOutOfRangeMustBeRejected(int rating) {
        EmployeePerformance p = new EmployeePerformance(100000, 90, rating, 2);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(p));
    }

    // дефект 4: отрицательный стаж принимался
    @Test
    void negativeYearsMustBeRejected() {
        EmployeePerformance p = new EmployeePerformance(100000, 90, 4, -1);
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(p));
    }

    // вариант 4, граница: 99.99 - обычная ставка 5 %
    @Test
    void kpiJustBelowBoundaryGivesBaseRate() {
        EmployeePerformance p = new EmployeePerformance(100000, 99.99, 4, 2);
        double bonus = calculator.calculate(p);
        assertEquals(5000, bonus, DELTA);
    }

    // вариант 4, граница: 100.00 уже повышенная ставка 15 % (сравнение >=)
    @Test
    void boundaryAtHundredPercent() {
        EmployeePerformance p = new EmployeePerformance(100000, 100.00, 4, 2);
        double bonus = calculator.calculate(p);
        assertEquals(15000, bonus, DELTA);
    }

    // по варианту 80.00 теперь обычная ставка
    @Test
    void eightyPercentIsBaseRateInVariant() {
        EmployeePerformance p = new EmployeePerformance(100000, 80.00, 4, 2);
        double bonus = calculator.calculate(p);
        assertEquals(5000, bonus, DELTA);
    }

    @Test
    void typicalBonus() {
        EmployeePerformance p = new EmployeePerformance(120000, 105, 4, 3);
        double bonus = calculator.calculate(p);
        assertEquals(18000, bonus, DELTA);
    }

    @Test
    void allAdditionsTogether() {
        EmployeePerformance p = new EmployeePerformance(100000, 100, 5, 6);
        double bonus = calculator.calculate(p);
        assertEquals(22000, bonus, DELTA);
    }

    // граница стажа: 5 лет без надбавки, 6 лет +2 %
    @Test
    void yearsBoundaryFiveAndSix() {
        double five = calculator.calculate(new EmployeePerformance(100000, 50, 4, 5));
        double six = calculator.calculate(new EmployeePerformance(100000, 50, 4, 6));
        assertEquals(5000, five, DELTA);
        assertEquals(7000, six, DELTA);
    }

    // нижние границы: оклад 0, KPI 0, стаж 0, рейтинг 1 допустимы
    @Test
    void zeroValuesAreAllowed() {
        assertEquals(0, calculator.calculate(new EmployeePerformance(0, 90, 4, 2)), DELTA);
        assertEquals(5000, calculator.calculate(new EmployeePerformance(100000, 0, 1, 0)), DELTA);
    }

    @Test
    void ratingFiveAddsFivePercent() {
        double four = calculator.calculate(new EmployeePerformance(100000, 50, 4, 1));
        double five = calculator.calculate(new EmployeePerformance(100000, 50, 5, 1));
        assertEquals(5000, five - four, DELTA);
    }

    // таблица случаев: kpi; rating; years; ожидаемая премия при окладе 100 000
    @ParameterizedTest
    @CsvSource({
            "99.99, 4, 2, 5000",
            "100.00, 4, 2, 15000",
            "80.00, 4, 2, 5000",
            "95.5, 5, 2, 10000",
            "60, 3, 10, 7000",
            "120, 5, 8, 22000"
    })
    void bonusTable(double kpi, int rating, int years, double expected) {
        EmployeePerformance p = new EmployeePerformance(100000, kpi, rating, years);
        assertEquals(expected, calculator.calculate(p), DELTA);
    }
}
