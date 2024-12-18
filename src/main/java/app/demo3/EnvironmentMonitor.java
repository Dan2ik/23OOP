package app.demo3;

public class EnvironmentMonitor {
    private double residualConcentration = 0;

    // Метод для расчета концентрации загрязняющих веществ с учетом погоды
    public double calculateConcentration(double emissionsToday, Weather weather) {
        // Учет погодных условий для рассасывания загрязняющих веществ
        residualConcentration = (residualConcentration * 0.7) / weather.getDispersionFactor(); // Расчет остаточной концентрации
        residualConcentration += emissionsToday; // Добавляем выбросы за текущий день
        return residualConcentration;
    }
}
