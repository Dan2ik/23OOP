package app.demo3;

import com.almasb.fxgl.entity.action.Action;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    private TableView<DaySummary> carsTable;
    @FXML
    private TableView<ConcreteAction> actionsTable;

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
    @FXML
    private Text Day;
    @FXML
    private Text AllPolution;
    private ObservableList<Enterprise> enterpriseData;
    private ObservableList<Vehicle> vehicleData;
    private ObservableList<ConcreteAction> actionData;

    private List<Enterprise> enterprises;
    private List<Vehicle> vehicles;
    private int simulationDays;
    private double cityFund;
    private CityDepartment cityDepartment;
    private ObservableList<DaySummary> carSummaries;
    private boolean isRaining;
    private boolean isWindy;
    private Random random = new Random();
    private Window ownerStage;
    private int numCars;
    private double ALLP=0;
    @FXML
    private int currentDay = 0;  // Текущий день симуляции

    @FXML
    protected void initialize() {
// Инициализация параметров
        enterprises = new ArrayList<>();
        vehicles = new ArrayList<>();

// Инициализация ObservableList для таблиц
        enterpriseData = FXCollections.observableArrayList();
        vehicleData = FXCollections.observableArrayList();
        actionData = FXCollections.observableArrayList();
        carSummaries = FXCollections.observableArrayList(); // Для таблицы carsTable

// Связываем данные с таблицами
        enterpriseTable.setItems(enterpriseData);
        carsTable.setItems(carSummaries); // Используем carSummaries вместо vehicleData
        actionsTable.setItems(actionData);

// Привязка столбцов для enterpriseTable
        enterpriseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emissionsColumn.setCellValueFactory(new PropertyValueFactory<>("currentEmissions"));
        limitColumn.setCellValueFactory(new PropertyValueFactory<>("allowedEmissions"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));

// Привязка столбцов для carsTable
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        allowedCarsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfVehicles")); // Изменено на numberOfVehicles
        totalExhaustColumn.setCellValueFactory(new PropertyValueFactory<>("totalEmissions"));

// Привязка столбцов для actionsTable
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        actionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    }

    private void clearVehiclesFromMap() {
        cityMapPane.getChildren().removeIf(node ->
                node instanceof Circle &&
                        ((Circle) node).getFill() instanceof Color &&
                        ((Color) ((Circle) node).getFill()).getBlue() > 0.8
        );
    }
    public void NextDayClick(ActionEvent actionEvent) {
        ALLP=0;
        this.cityDepartment = new CityDepartment(Double.parseDouble(fundField.getText()));
        // Чтение данных из интерфейса
        int numEnterprises = Integer.parseInt(numEnterprisesField.getText());
        numCars = Integer.parseInt(numCarsField.getText());
        cityFund = Double.parseDouble(fundField.getText());
        simulationDays = Integer.parseInt(stepField.getText());
        isRaining = rainCheckBox.isSelected();
        isWindy = windCheckBox.isSelected();
        if (currentDay<simulationDays){
        // Создание окна загрузки
        Stage loadingStage = new Stage();
        loadingStage.initOwner(ownerStage);
        loadingStage.initModality(Modality.WINDOW_MODAL);
        loadingStage.setTitle("Loading...");
        Label messageLabel = new Label("Заводы парят, машины дымят");
        ProgressIndicator progressIndicator = new ProgressIndicator();

        VBox vbox = new VBox(10, progressIndicator, messageLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 150);
        loadingStage.setScene(scene);
        loadingStage.setResizable(false);

        // Показываем окно загрузки
        loadingStage.show();

        // Используем PauseTransition для задержки на 5 секунд
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(5));
        delay.setOnFinished(event -> {
            try {
                 NDay();
                updatePollutionCircles();
                AllPolution.setText("Общее количество выбросов за день: " + String.format("%.2f", ALLP));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Закрытие окна загрузки
                loadingStage.close();
            }
        });

        delay.play();
    }
        else{
            // Создание диалогового окна с сообщением
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Симуляция завершена");
            alert.setHeaderText(null);
            alert.setContentText("Дни симуляции закончились. Перезапустите симуляцию.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        currentDay=0;
        this.cityDepartment = new CityDepartment(Double.parseDouble(fundField.getText()));
        // Чтение данных из интерфейса
        int numEnterprises = Integer.parseInt(numEnterprisesField.getText());
        int numCars = Integer.parseInt(numCarsField.getText());
        cityFund = Double.parseDouble(fundField.getText());
        simulationDays = Integer.parseInt(stepField.getText());
        isRaining = rainCheckBox.isSelected();
        isWindy = windCheckBox.isSelected();

        // Создание окна загрузки
        Stage loadingStage = new Stage();
        loadingStage.initOwner(ownerStage);
        loadingStage.initModality(Modality.WINDOW_MODAL);
        loadingStage.setTitle("Loading...");

        Label messageLabel = new Label("Строятся заводы, запускаются машины");
        ProgressIndicator progressIndicator = new ProgressIndicator();

        VBox vbox = new VBox(10, progressIndicator, messageLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 150);
        loadingStage.setScene(scene);
        loadingStage.setResizable(false);

        // Показываем окно загрузки
        loadingStage.show();

        // Используем PauseTransition для задержки на 5 секунд
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(event -> {
            try {
                // Выполнение операций
                generateEnterprises(numEnterprises);
                generateVehicles(numCars);
                runSimulation();
                updatePollutionCircles();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Закрытие окна загрузки
                loadingStage.close();
            }
        });

        delay.play();
    }


    private void updatePollutionCircles() {
        // Удаляем старые круги загрязнения
        cityMapPane.getChildren().removeIf(node ->
                node instanceof Circle &&
                        ((Circle) node).getFill() instanceof Color &&
                        ((Color) ((Circle) node).getFill()).getRed() > 0.8
        );
        // Добавляем новые круги для каждого предприятия
        for (Enterprise enterprise : enterprises) {
            double currentEmissions = enterprise.calculateEmissions();
            double radius = Math.min(100, currentEmissions * 2);
            double opacity = Math.min(0.5, currentEmissions / 200);

            Circle pollutionCircle = new Circle(enterprise.getX(), enterprise.getY(), radius, Color.rgb(255, 0, 0, opacity));
            pollutionCircle.setEffect(new GaussianBlur(20));
            cityMapPane.getChildren().add(pollutionCircle);
        }

        // Добавляем новые круги для каждого автомобиля
        for (Vehicle vehicle : vehicles) {
            double currentEmissions = vehicle.calculateEmissions();
            double radius = currentEmissions / 10; // Радиус меньше, чем у предприятий
            double opacity = currentEmissions / 300; // Прозрачность ниже

            Circle pollutionCircle = new Circle(vehicle.getX(), vehicle.getY(), radius, Color.rgb(0, 0, 255, opacity)); // Синий цвет для автомобилей
            pollutionCircle.setEffect(new GaussianBlur(15)); // Меньший эффект размытия
            cityMapPane.getChildren().add(pollutionCircle);
        }
    }
    private void generateEnterprises(int count) {
        cityMapPane.getChildren().clear(); // Очистка карты
        enterprises.clear();
        enterpriseData.clear();

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(400) + 50; // Координаты предприятий
            int y = random.nextInt(300) + 50;
            double allowedEmissions = 100;
            double tax = 50000;

            // Генерируем предприятие
            Enterprise enterprise = new Enterprise("Завод " + (i + 1), x, y, allowedEmissions, tax);
            enterprises.add(enterprise);
            enterpriseData.add(enterprise);

            // Отображение размытых кругов
            double currentEmissions = enterprise.calculateEmissions(); // Начальный уровень выбросов
            double radius = Math.min(100, currentEmissions * 2); // Радиус размытых кругов (максимум 100)
            double opacity= currentEmissions / 200; // Прозрачность (максимум 0.5)

            Circle pollutionCircle = new Circle(x, y, radius, Color.rgb(255, 0, 0, opacity));
            pollutionCircle.setEffect(new GaussianBlur(20)); // Эффект размытия
            cityMapPane.getChildren().add(pollutionCircle);

            // Отображение предприятия
            Circle enterpriseCircle = new Circle(x, y, 8, Color.BLACK);
            cityMapPane.getChildren().add(enterpriseCircle);
            cityDepartment.addPollutionSource(enterprise);
        }
    }
    protected void NDay() {
        // Очистка карты от машин
        clearVehiclesFromMap();
        currentDay++;
        Day.setText("День: "+ currentDay);
// Обновляем выбросы для предприятий и добавляем их в список
        for (Enterprise enterprise : new ArrayList<>(enterprises)) {

            double updatedEmissions = (100-7*enterprise.getFilter())*(1 + (random.nextDouble() - 0.5) * 0.1);
            enterprise.setCurrentEmissions(updatedEmissions);
            ALLP+=updatedEmissions;
            System.out.println(updatedEmissions);
            // Применяем штраф
            double fineAmount = (enterprise.getCurrentEmissions() - enterprise.getAllowedEmissions()) * 0.1;
            if (enterprise.getAllowedEmissions()<=enterprise.getCurrentEmissions()) {
                cityFund += fineAmount;
                enterprise.setFine(50000);
                enterprise.installFilter();//уменьшаем выбрасы на 7 процентов
                System.out.println("Штраф для " + enterprise.getName() + ": " + 50000);
                actionData.add(new ConcreteAction(String.valueOf(currentDay),enterprise.getName(), "штраф 50000"));

            } else {
                enterprise.setFine(0);
            }

            enterpriseData.set(1, enterprise);
            // Обновляем таблицу
        }
        // Очищаем список машин
        vehicles.clear();
        Random random = new Random();
        cityDepartment.setNumberOfVehicles(numCars);
        double totalEmissionsForDay = 0; // Суммарные выбросы за день
        boolean t=false;
        int i1=0;
        double randomFactor = 0.9 + (1.1 - 0.9) * random.nextDouble(); // Рандомный коэффициент в диапазоне [0.9, 1.1]
        // Создание машин и подсчёт выбросов
        for (int i = 0; i < numCars; i++) {



// Условие с рандомизацией
            if (ALLP / (Integer.parseInt(numEnterprisesField.getText()) + Integer.parseInt(numCarsField.getText())) > 100 * randomFactor) {
                i1++;
                System.out.println(i1);

            }
            else {
                int x = random.nextInt(700) + 50;
                int y = random.nextInt(400) + 50;
                double emissions = 10 + random.nextDouble() * 10;
                ALLP+=emissions;
                Vehicle vehicle = new Vehicle("Car " + (i + 1), x, y, emissions);
                vehicle.setDay(currentDay);
                vehicles.add(vehicle);
                vehicleData.add(vehicle);
                totalEmissionsForDay += vehicle.calculateEmissions(); // Суммируем выбросы за день

                // Создание круга машины
                Circle vehicleCircle = new Circle(x, y, 4, Color.BLUE);
                cityMapPane.getChildren().add(vehicleCircle);

                // Эффект загрязнения
                double radius = Math.min(10, emissions * 5); // Радиус загрязнения
                double opacity = Math.min(0.01, emissions / 100); // Прозрачность

                Circle pollutionCircle = new Circle(x, y, radius, Color.rgb(255, 0, 0, opacity));
                pollutionCircle.setEffect(new GaussianBlur(100)); // Эффект размытия
                cityMapPane.getChildren().add(pollutionCircle);

                cityDepartment.addPollutionSource(vehicle); // Добавление в CityDepartment
            }
        }

        actionData.add(new ConcreteAction(String.valueOf(currentDay), "ограничение","ограничено движение "+i1+"автомобилям" ));
        // Сохранение данных о количестве машин и выбросах за день
        cityDepartment.setNumberOfVehicles(numCars-i1);
        cityDepartment.setTotalEmissionsForDay(totalEmissionsForDay);

        // Добавляем данные в таблицу
        carSummaries.add(new DaySummary(currentDay, numCars-i1, totalEmissionsForDay));

        // Обновляем круги загрязнения
        updatePollutionCircles();
    }
    private void generateVehicles(int count) {
        vehicles.clear();        // Очистка списка транспортных средств
        vehicleData.clear();     // Очистка таблицы (если требуется)

        Random random = new Random();
        cityDepartment.setNumberOfVehicles(count);
        for (int i = 0; i < count; i++) {
            int x = random.nextInt(400) + 50;
            int y = random.nextInt(300) + 50;
            double emissions = 50 + random.nextDouble() * 100;

            // Создание автомобиля
            Vehicle vehicle = new Vehicle("Car " + (i + 1), x, y, emissions);
            vehicles.add(vehicle);
            vehicleData.add(vehicle);

            // Круг автомобиля
            Circle vehicleCircle = new Circle(x, y, 4, Color.BLUE);
            cityMapPane.getChildren().add(vehicleCircle);

            // Эффект загрязнения
            double currentEmissions = vehicle.calculateEmissions(); // Начальный уровень выбросов
            double radius = Math.min(10, currentEmissions * 5);   // Радиус загрязнения (увеличен для наглядности)
            double opacity = Math.min(0.01, currentEmissions / 100);   // Прозрачность (увеличена для видимости)

            Circle pollutionCircle = new Circle(x, y, radius, Color.rgb(255, 0, 0, opacity));
            pollutionCircle.setEffect(new GaussianBlur(100)); // Эффект размытия// Чтобы круг не перекрывал взаимодействия
            cityMapPane.getChildren().add(pollutionCircle);
            cityDepartment.addPollutionSource(vehicle); // Добавление в CityDepartment
            vehicle.setAllowedCars(random.nextInt(100)); // Пример установки данных

        }
    }

    private void runSimulation() {
        for (int day = 1; day <= 1; day++) {
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
