# Практическая работа №14. Автоматические тесты, Maven и проверка граничных условий

Климашевский Даниил Александрович, группа ЭФБО-07-25, вариант 4.

Расчет премии по рейтингу, KPI и стажу. Сначала написаны JUnit 5 тесты по правилам и границам, они нашли дефекты исходного BonusCalculator, потом дефекты исправлены. По варианту 4 граница KPI 80 + 25 % = 100.

## Сборка и запуск

Нужны Java 21 и Maven.

```bash
mvn test
```

Быстрая проверка без Maven (только src/main/java):

```bash
./run_selfcheck.sh
java -cp out ru.edu.pr11.SelfCheck data/boundaries.csv
```

`run_selfcheck.bat` - то же для Windows.

Сначала выполняются проверки, потом обрабатывается `data/sample.csv` (формат `salary;kpi;rating;years`, первая строка - заголовок).
Свой файл: `java -cp out ru.edu.pr11.SelfCheck data/boundaries.csv`.

Пакет называется `ru.edu.pr11`, как в исходном проекте.

## Что где лежит

- `src/main/java/ru/edu/pr11/BonusCalculator.java` - расчет премии
- `src/main/java/ru/edu/pr11/EmployeePerformance.java` - данные сотрудника
- `src/main/java/ru/edu/pr11/SelfCheck.java` - быстрая проверка без Maven
- `src/test/java/ru/edu/pr11/BonusCalculatorTest.java` - JUnit 5 тесты
- `data/sample.csv` - открытый набор из задания, `data/boundaries.csv` - мои граничные значения
- `diagrams/` - activity-диаграмма и диаграмма классов (dot и png)
- `DECISIONS.md` - вариант, два решения, граничные проверки, ответы на вопросы задания
- `REPORT.docx` - отчет
