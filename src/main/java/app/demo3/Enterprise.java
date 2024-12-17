package app.demo3;

public class Enterprise extends PollutionSource {
    protected double allowedEmissions; // Допустимые выбросы
    protected double currentEmissions; // Текущие выбросы
    protected double taxPayments; // Налоговые отчисления

    public Enterprise(String name, int x, int y, double allowedEmissions, double taxPayments) {
        super(name, x, y);
        this.allowedEmissions = allowedEmissions;
        this.taxPayments = taxPayments;
        this.currentEmissions = 0;
    }

    @Override
    public double calculateEmissions() {
        currentEmissions = allowedEmissions * (0.8 + Math.random() * 0.4); // 80%-120%
        return currentEmissions;
    }

    public void applyFine(double fineAmount) {
        System.out.println(name + " оштрафовано на " + fineAmount + " у.е.");
    }

    public void installFilter() {
        allowedEmissions *= 0.93; // Уменьшение нормы выбросов на 7%
        System.out.println("На " + name + " установлен фильтр.");
    }
}
