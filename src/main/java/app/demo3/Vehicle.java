package app.demo3;

public class Vehicle extends PollutionSource {
    private double emissions; // Выбросы от автомобиля

    public Vehicle(String name, int x, int y, double emissions) {
        super(name, x, y);
        this.emissions = emissions;
    }

    @Override
    public double calculateEmissions() {
        // Автомобиль возвращает фиксированные выбросы
        return emissions;
    }



    // Геттер для выбросов
    public double getEmissions() {
        return emissions;
    }
}
