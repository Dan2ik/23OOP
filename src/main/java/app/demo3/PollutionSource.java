package app.demo3;

public abstract class PollutionSource {
    protected String name; // Название источника загрязнения
    protected int x, y;    // Координаты на карте

    public PollutionSource(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Абстрактный метод для расчета выбросов
    public abstract double calculateEmissions();

    // Геттеры
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

