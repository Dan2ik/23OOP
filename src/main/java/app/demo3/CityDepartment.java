package app.demo3;

import java.util.ArrayList;
import java.util.List;

public class CityDepartment {
    private List<PollutionSource> pollutionSources;
    private List<Vehicle> vehicles;  // Список объектов типа Vehicle
    private double cityFunds; // Городской бюджет

    public CityDepartment(double initialFunds) {
        this.pollutionSources = new ArrayList<>();
        this.vehicles = new ArrayList<>();  // Инициализация списка для транспортных средств
        this.cityFunds = initialFunds;
    }

    public void addPollutionSource(PollutionSource source) {
        pollutionSources.add(source);
        if (source instanceof Vehicle) {
            vehicles.add((Vehicle) source);  // Добавляем объект Vehicle в список транспортных средств
        }
    }

    public void monitorPollution() {
        double totalEmissions = 0;
        // Мониторинг загрязнения от всех источников, включая транспортные средства
        for (PollutionSource source : pollutionSources) {
            double emissions = source.calculateEmissions();
            totalEmissions += emissions;
            System.out.println("Источник " + source.getName() + " выбрасывает " + emissions + " единиц загрязнений.");
        }
        System.out.println("Общее загрязнение: " + totalEmissions);
    }

    public void applyFines() {
        for (PollutionSource source : pollutionSources) {
            if (source instanceof Enterprise) {
                Enterprise enterprise = (Enterprise) source;
                if (enterprise.calculateEmissions() > enterprise.getAllowedEmissions()) {
                    double fine = 5000; // Пример штрафа
                    cityFunds += fine;
                    enterprise.applyFine(fine);
                }
            }
        }
    }

    public void subsidizeFilters() {
        for (PollutionSource source : pollutionSources) {
            if (source instanceof Enterprise) {
                Enterprise enterprise = (Enterprise) source;
                if (enterprise.calculateEmissions() > enterprise.getAllowedEmissions() * 1.2) { // Пример критерия
                    double subsidy = 30000; // Стоимость фильтра
                    if (cityFunds >= subsidy) {
                        cityFunds -= subsidy;
                        enterprise.installFilter();
                        System.out.println("Субсидия на фильтр для " + enterprise.getName());
                    }
                }
            }
        }
    }

    public void regulateVehicleTraffic(double percentageReduction) {
        int reducedVehicles = (int) (getNumberOfVehicles() * (1 - percentageReduction));
        setNumberOfVehicles(reducedVehicles);
        System.out.println("Ограничено движение для " + (getNumberOfVehicles() - reducedVehicles) + " транспортных средств.");
    }

    public double getCityFunds() {
        return cityFunds;
    }

    public int getNumberOfVehicles() {
        return vehicles.size();
    }

    public void setNumberOfVehicles(int a) {
        // Если требуется уменьшить количество транспортных средств
        if (a < vehicles.size()) {
            for (int i = vehicles.size() - 1; i >= a; i--) {
                vehicles.remove(i);  // Удаляем лишние элементы
            }
        } else if (a > vehicles.size()) {
            // Допустим, если мы добавляем новые автомобили
            for (int i = vehicles.size(); i < a; i++) {
                // Здесь создаем новый объект Vehicle с фиктивными данными, например:
                vehicles.add(new Vehicle("Vehicle " + (i + 1), 0, 0, 10));  // Пример
            }
        }
    }

    public void displayStatus() {
        System.out.println("Городской бюджет: " + cityFunds);
        monitorPollution();
        applyFines();
    }
}
