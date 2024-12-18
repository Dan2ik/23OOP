package app.demo3;

public class Weather {
    private double windStrength; // 0.0 - 1.0
    private boolean rain;

    public Weather(double windStrength, boolean rain) {
        this.windStrength = windStrength;
        this.rain = rain;
    }

    public double getDispersionFactor() {
        // Чем выше сила ветра и дождь, тем быстрее рассасываются загрязняющие вещества
        return 1 + windStrength + (rain ? 0.5 : 0);
    }
}
