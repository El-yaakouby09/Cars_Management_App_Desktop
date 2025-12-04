package com.cars_management.Controller;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class DashBordUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testDashboardStatsPresent(FxRobot robot) {
        Text txtCars = robot.lookup("#txtCars").queryAs(Text.class);
        Text txtClients = robot.lookup("#txtClients").queryAs(Text.class);
        Text txtRevenus = robot.lookup("#txtRevenus").queryAs(Text.class);
        Text txtBestCar = robot.lookup("#txtBestCar").queryAs(Text.class);

        assertThat(txtCars).isNotNull();
        assertThat(txtClients).isNotNull();
        assertThat(txtRevenus).isNotNull();
        assertThat(txtBestCar).isNotNull();
    }
}