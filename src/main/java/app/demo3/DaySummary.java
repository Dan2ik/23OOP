package app.demo3;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
public class DaySummary {
    private final SimpleIntegerProperty day;
    private final SimpleIntegerProperty numberOfVehicles;
    private final SimpleDoubleProperty totalEmissions;

    public DaySummary(int day, int numberOfVehicles, double totalEmissions) {
        this.day = new SimpleIntegerProperty(day);
        this.numberOfVehicles = new SimpleIntegerProperty(numberOfVehicles);
        this.totalEmissions = new SimpleDoubleProperty(totalEmissions);
    }

    public int getDay() {
        return day.get();
    }

    public void setDay(int day) {
        this.day.set(day);
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles.get();
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles.set(numberOfVehicles);
    }

    public double getTotalEmissions() {
        return totalEmissions.get();
    }

    public void setTotalEmissions(double totalEmissions) {
        this.totalEmissions.set(totalEmissions);
    }
}
