package app.demo3;

import com.almasb.fxgl.entity.action.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.GaussianBlur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HabitatController {

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
    private TableView<Enterprise> enterpriseTable;
    @FXML
    private TableView<Vehicle> carsTable;
    @FXML
    private TableView<Action> actionsTable;

    @FXML
    private TableColumn<Enterprise, String> enterpriseNameColumn;
    @FXML
    private TableColumn<Enterprise, Double> emissionsColumn;
    @FXML
    private TableColumn<Enterprise, Double> limitColumn;
    @FXML
    private TableColumn<Enterprise, Double> penaltyColumn;

    @FXML
    private TableColumn<Vehicle, Integer> dayColumn;
    @FXML
    private TableColumn<Vehicle, Integer> allowedCarsColumn;
    @FXML
    private TableColumn<Vehicle, Double> totalExhaustColumn;

    @FXML
    private TableColumn<Action, String> dateColumn;
    @FXML
    private TableColumn<Action, String> actionTypeColumn;
    @FXML
    private TableColumn<Action, String> actionDescriptionColumn;

    private ObservableList<Enterprise> enterpriseData;
    private ObservableList<Vehicle> vehicleData;
    private ObservableList<Action> actionData;

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

        // Инициализация ObservableList для таблиц
        enterpriseData = FXCollections.observableArrayList();
        vehicleData = FXCollections.observableArrayList();
        actionData = FXCollections.observableArrayList();

        // Связываем данные с таблицами
        enterpriseTable.setItems(enterpriseData);
        carsTable.setItems(vehicleData);
        actionsTable.setItems(actionData);

        // Привязка столбцов для enterpriseTable
        enterpriseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emissionsColumn.setCellValueFactory(new PropertyValueFactory<>("currentEmissions"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("allowedEmissions"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Привязка столбцов для carsTable
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        allowedCarsColumn.setCellValueFactory(new PropertyValueFactory<>("allowedCars"));
        totalExhaustColumn.setCellValueFactory(new PropertyValueFactory<>("totalEmissions"));

        // Привязка столбцов для actionsTable
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        actionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
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
        enterpriseData.clear();

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(400) + 50; // Координаты предприятий
            int y = random.nextInt(300) + 50;
            double allowedEmissions = 50 + random.nextDouble() * 100;
            double tax = 500 + random.nextDouble() * 1000;

            // Генерируем предприятие
            Enterprise enterprise = new Enterprise("Завод " + (i + 1), x, y, allowedEmissions, tax);
            enterprises.add(enterprise);
            enterpriseData.add(enterprise);

            // Отображение размытых кругов
            double currentEmissions = enterprise.calculateEmissions(); // Начальный уровень выбросов
            double radius = Math.min(100, currentEmissions * 2); // Радиус размытых кругов (максимум 100)
            double opacity = Math.min(0.5, currentEmissions / 200); // Прозрачность (максимум 0.5)

            Circle pollutionCircle = new Circle(x, y, radius, Color.rgb(255, 0, 0, opacity));
            pollutionCircle.setEffect(new GaussianBlur(20)); // Эффект размытия
            cityMapPane.getChildren().add(pollutionCircle);

            // Отображение предприятия
            Circle enterpriseCircle = new Circle(x, y, 8, Color.BLACK);
            cityMapPane.getChildren().add(enterpriseCircle);
        }
    }
    private void updatePollutionCircles() {
        // Удаляем старые круги загрязнения
        cityMapPane.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getFill() instanceof Color && ((Color) ((Circle) node).getFill()).getRed() > 0.8);

        // Добавляем новые круги для каждого предприятия
        for (Enterprise enterprise : enterprises) {
            double currentEmissions = enterprise.calculateEmissions();
            double radius = Math.min(100, currentEmissions * 2);
            double opacity = Math.min(0.5, currentEmissions / 200);

            Circle pollutionCircle = new Circle(enterprise.getX(), enterprise.getY(), radius, Color.rgb(255, 0, 0, opacity));
            pollutionCircle.setEffect(new GaussianBlur(20));
            cityMapPane.getChildren().add(pollutionCircle);
        }
    }
    private void generateVehicles(int count) {
        vehicles.clear();
        vehicleData.clear();

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

            // Считаем выбросы от автомобилей
            int activeVehicles = (int) (vehicles.size() * 0.75);
            for (int i = 0; i < activeVehicles; i++) {
                totalEmissions += vehicles.get(i).calculateEmissions();
            }

            // Учитываем погодные условия
            if (isRaining) totalEmissions *= 0.9; // Дождь уменьшает выбросы на 10%
            if (isWindy) totalEmissions *= 0.85; // Ветер уменьшает выбросы на 15%

            // Применяем штрафы и обновляем интерфейс
            applyFines();
            updatePollutionCircles();
        }
    }

    private void applyFines() {
        for (Enterprise e : enterprises) {
            double emissions = e.calculateEmissions();
            if (emissions > e.getAllowedEmissions()) {
                double fine = emissions * 0.1; // Штраф в 10% от выбросов
                cityFund += fine;
            }
        }
    }
}
