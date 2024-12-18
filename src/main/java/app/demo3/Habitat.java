package app.demo3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.GaussianBlur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Habitat {
    @FXML
    private TextField numEnterprisesField;
    @FXML
    private TextField numCarsField;
    @FXML
    private TextField fundField;
    @FXML
    private TextField stepField;
    @FXML
    private CheckBox rainCheckBox;
    @FXML
    private CheckBox windCheckBox;
    @FXML
    private AnchorPane cityMapPane;
    @FXML
    private TableView<?> enterpriseTable;
    @FXML
    private TableView<?> carsTable;
    @FXML
    private TableView<?> actionsTable;

    private List<Enterprise> enterprises;
    private List<Vehicle> vehicles;
    private int simulationDays;
    private double cityFund;
    private boolean isRaining;
    private boolean isWindy;
    private Random random = new Random();

    @FXML
    protected void initialize() {
        // Инициализация параметров
        enterprises = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

    @FXML
    protected void onHelloButtonClick() {
        // Чтение данных из интерфейса
        int numEnterprises = Integer.parseInt(numEnterprisesField.getText());
        int numCars = Integer.parseInt(numCarsField.getText()) * 1000; // Конвертируем в тысячи
        cityFund = Double.parseDouble(fundField.getText());
        simulationDays = Integer.parseInt(stepField.getText());
        isRaining = rainCheckBox.isSelected();
        isWindy = windCheckBox.isSelected();

        // Генерация предприятий и автомобилей
        generateEnterprises(numEnterprises);
        generateVehicles(numCars);

        // Запуск моделирования
        runSimulation();
    }

    private void generateEnterprises(int count) {
        cityMapPane.getChildren().clear(); // Очистка карты
        enterprises.clear();
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(400) + 50; // Координаты предприятий
            int y = random.nextInt(300) + 50;
            double allowedEmissions = 50 + random.nextDouble() * 100;
            double tax = 500 + random.nextDouble() * 1000;

            Enterprise enterprise = new Enterprise("Завод " + (i + 1), x, y, allowedEmissions, tax);
            enterprises.add(enterprise);

            // Отображение предприятия на карте
            Circle enterpriseCircle = new Circle(x, y, 8, Color.BLACK);
            cityMapPane.getChildren().add(enterpriseCircle);
        }
    }

    private void generateVehicles(int count) {
        vehicles.clear();
        for (int i = 0; i < count; i++) {
            double emissions = 0.5 + random.nextDouble(); // Средний выхлоп автомобиля
            vehicles.add(new Vehicle("Car " + (i + 1), 0, 0, emissions));
        }
    }

    private void runSimulation() {
        for (int day = 1; day <= simulationDays; day++) {
            double totalEmissions = 0.0;
            // Считаем выбросы от предприятий
            for (Enterprise e : enterprises) {
                totalEmissions += e.calculateEmissions();
            }

            // Считаем выбросы от автомобилей (75% от всех автомобилей двигаются)
            int activeVehicles = (int) (vehicles.size() * 0.75);
            for (int i = 0; i < activeVehicles; i++) {
                totalEmissions += vehicles.get(i).calculateEmissions();
            }

            // Учитываем погодные условия
            if (isRaining) totalEmissions *= 0.9; // Дождь уменьшает выбросы на 10%
            if (isWindy) totalEmissions *= 0.85; // Ветер уменьшает выбросы на 15%

            // Обновляем карту загрязнения
            updatePollutionMap(totalEmissions);

            // Обновление данных на интерфейсе (заглушка)
            System.out.println("Day " + day + ": Total emissions = " + totalEmissions);
        }
    }

    private void updatePollutionMap(double totalEmissions) {
        cityMapPane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getRadius() > 10);
        // Создаем облако загрязнения
        Circle pollutionCloud = new Circle(250, 200, totalEmissions / 10, Color.rgb(255, 0, 0, 0.3));
        pollutionCloud.setEffect(new GaussianBlur(20));
        cityMapPane.getChildren().add(pollutionCloud);
    }
}
