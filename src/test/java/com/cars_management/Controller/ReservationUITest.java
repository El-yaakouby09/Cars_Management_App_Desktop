package com.cars_management.Controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class ReservationUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/reservation.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testReservationFields(FxRobot robot) {
        TextField clientField = robot.lookup("#clientField").queryAs(TextField.class);
        ComboBox<?> cbCars = robot.lookup("#cbCars").queryAs(ComboBox.class);
        DatePicker startDate = robot.lookup("#startDateField").queryAs(DatePicker.class);
        DatePicker endDate = robot.lookup("#endDateField").queryAs(DatePicker.class);
        TableView<?> table = robot.lookup("#tableReservation").queryAs(TableView.class);

        assertThat(clientField).isNotNull();
        assertThat(cbCars).isNotNull();
        assertThat(startDate).isNotNull();
        assertThat(endDate).isNotNull();
        assertThat(table).isNotNull();

        robot.clickOn(clientField).write("Alice");
        assertThat(clientField.getText()).isEqualTo("Alice");
    }
}