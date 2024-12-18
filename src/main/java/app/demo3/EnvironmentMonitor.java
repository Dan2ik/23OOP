package app.demo3;

public class EnvironmentMonitor {
    private double residualConcentration = 0;

    public double calculateConcentration(double emissionsToday, Weather weather) {
        residualConcentration = (residualConcentration * 0.7) / weather.getDispersionFactor();
        residualConcentration += emissionsToday;
        return residualConcentration;
    }
}
