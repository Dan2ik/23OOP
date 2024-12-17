package app.demo3;

public abstract class PollutionSource {
    protected String name;
    protected int x, y; // Координаты на карте

    public PollutionSource(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public abstract double calculateEmissions();

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

