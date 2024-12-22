package app.demo3;

public class Vehicle extends PollutionSource {
    private double emissions; // Выбросы от автомобиля
    private int day;
    private int allowedCars;
    private int totalEmissions;


    public Vehicle(String name, int x, int y, double emissions) {
        super(name, x, y);
        this.emissions = emissions;
    }

    @Override
    public double calculateEmissions() {
        // Автомобиль возвращает фиксированные выбросы
        return emissions;
    }
    // Геттер для day
    public int getDay() {
        return day;
    }
    public int getAllowedCars() {
        return allowedCars;
    }
    public void setAllowedCars(int Cars) {
        this.allowedCars = Cars;
    }
    public int getTotalEmissions() {
        return totalEmissions;
    }
    public void setTotalEmissions(int Emissions) {
        this.totalEmissions = Emissions;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setCurrentEmissions(double updatedEmissions) {
        this.emissions=updatedEmissions;
    }
}
