package ru.edu.pr11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// быстрая проверка без Maven, полный набор тестов - mvn test
public class SelfCheck {
    private static final BonusCalculator CALC = new BonusCalculator();
    private static int passed = 0;
    private static int count = 0;

    public static void main(String[] args) {
        double b = BonusCalculator.KPI_BOUNDARY;
        check("below boundary", bonus(100000, b - 0.01, 4, 2) == 5000);
        check("at boundary", bonus(100000, b, 4, 2) == 15000);
        check("all additions", Math.abs(bonus(100000, 100, 5, 6) - 22000) < 0.001);
        check("years 5 and 6", bonus(100000, 50, 4, 5) == 5000 && Math.abs(bonus(100000, 50, 4, 6) - 7000) < 0.001);
        check("zero salary", bonus(0, 90, 4, 2) == 0);
        check("negative salary", !valid(-1, 90, 4, 2));
        check("negative kpi", !valid(100000, -1, 4, 2));
        check("rating 0 and 6", !valid(100000, 90, 0, 2) && !valid(100000, 90, 6, 2));
        check("negative years", !valid(100000, 90, 4, -1));
        System.out.println("Unit: " + passed + "/" + count + " passed");
        // свой файл: java -cp out ru.edu.pr11.SelfCheck data/my.csv
        runFile(args.length > 0 ? args[0] : "data/sample.csv");
    }

    private static double bonus(double salary, double kpi, int rating, int years) {
        return CALC.calculate(new EmployeePerformance(salary, kpi, rating, years));
    }

    private static boolean valid(double salary, double kpi, int rating, int years) {
        try {
            bonus(salary, kpi, rating, years);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void check(String name, boolean ok) {
        count++;
        if (ok) {
            passed++;
            System.out.println("PASS " + name);
        } else {
            System.out.println("FAIL " + name);
        }
    }

    // ---------- премии по файлу salary;kpi;rating;years ----------

    private static void runFile(String fileName) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println("Не удалось прочитать " + fileName);
            return;
        }
        System.out.println();
        System.out.println("Премии по файлу " + fileName + " (граница KPI " + BonusCalculator.KPI_BOUNDARY + "):");
        for (int i = 1; i < lines.size(); i++) {   // первая строка - заголовок
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            String[] p = line.split(";", -1);
            try {
                if (p.length != 4) {
                    throw new IllegalArgumentException("нужно 4 значения через ;");
                }
                EmployeePerformance e = new EmployeePerformance(Double.parseDouble(p[0].strip()), Double.parseDouble(p[1].strip()),
                        Integer.parseInt(p[2].strip()), Integer.parseInt(p[3].strip()));
                System.out.println(line + " -> премия " + String.format("%.2f", CALC.calculate(e)));
            } catch (NumberFormatException e) {
                System.out.println(line + " -> ошибка: не число");
            } catch (IllegalArgumentException e) {
                System.out.println(line + " -> ошибка: " + e.getMessage());
            }
        }
    }
}
