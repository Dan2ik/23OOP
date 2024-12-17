package app.demo3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private AnchorPane cityMapPane;

    private Circle enterprise;
    private Circle pollutionCloud;

    @FXML
    protected void initialize() {
        // Создаем точку предприятия
        enterprise = new Circle(200, 200, 10);
        enterprise.setFill(Color.BLACK);

        // Создаем область загрязнения
        pollutionCloud = new Circle(200, 200, 100);
        pollutionCloud.setFill(Color.rgb(255, 0, 0, 0.3));
        pollutionCloud.setEffect(new GaussianBlur(30));
        pollutionCloud.setVisible(false); // Скрываем до события

        // Добавляем элементы в панель
        cityMapPane.getChildren().addAll(pollutionCloud, enterprise);
    }

    @FXML
    protected void onHelloButtonClick() {
        // Показать облако загрязнения
        pollutionCloud.setVisible(true);
    }
}
