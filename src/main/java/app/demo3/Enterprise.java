package app.demo3;

public class Enterprise extends PollutionSource {
    private double allowedEmissions; // Допустимые выбросы
    private double currentEmissions; // Текущие выбросы
    private int filter=0;
    private double taxPayments;      // Налоговые отчисления
    private int fine;

    public Enterprise(String name, int x, int y, double allowedEmissions, double taxPayments) {
        super(name, x, y);
        this.allowedEmissions = 100;
        this.taxPayments = taxPayments;
        this.currentEmissions = 0;

    }

    @Override
    public double calculateEmissions() {
        // Выбросы варьируются от 70% до 120% от допустимого предела
        //if (allowedEmissions<currentEmissions){
         //   this.fine=50000;
        //}
        return currentEmissions;
    }

    public void applyFine(double fineAmount) {
        System.out.println(name + " оштрафовано на " + fineAmount + " у.е.");
    }

    public void installFilter() {
        filter++;
        System.out.println("На предприятии \"" + name + "\" установлен фильтр.");
    }

    public int getFilter(){
        return filter;
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

    public void setCurrentEmissions(double updatedEmissions) {
        this.currentEmissions=updatedEmissions;
    }
}
