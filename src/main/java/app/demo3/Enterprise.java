package app.demo3;

public class Enterprise extends PollutionSource {
    private double allowedEmissions; // Допустимые выбросы
    private double currentEmissions; // Текущие выбросы
    private double taxPayments;      // Налоговые отчисления
    private int fine;

    public Enterprise(String name, int x, int y, double allowedEmissions, double taxPayments) {
        super(name, x, y);
        this.allowedEmissions = allowedEmissions;
        this.taxPayments = taxPayments;
        this.currentEmissions = 0;
    }

    @Override
    public double calculateEmissions() {
        // Выбросы варьируются от 80% до 120% от допустимого предела
        currentEmissions = allowedEmissions * (0.8 + Math.random() * 0.4);
        return currentEmissions;
    }

    public void applyFine(double fineAmount) {
        System.out.println(name + " оштрафовано на " + fineAmount + " у.е.");
    }

    public void installFilter() {
        allowedEmissions *= 0.93; // Уменьшение нормы выбросов на 7%
        System.out.println("На предприятии \"" + name + "\" установлен фильтр.");
    }

    // Геттеры
    public double getAllowedEmissions() {
        return allowedEmissions;
    }

    public double getCurrentEmissions() {
        return currentEmissions;
    }
    public int getFine() {
        return fine;
    }
    public void setFine(int a) {
        this.fine=a;
    }
}
