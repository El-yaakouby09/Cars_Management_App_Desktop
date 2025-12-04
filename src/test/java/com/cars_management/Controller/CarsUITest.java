package com.cars_management.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class CarsUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/cars.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testAddCarButton(FxRobot robot) {
        // Find the text fields
        TextField tfId = robot.lookup("#tfId").queryAs(TextField.class);
        TextField tfMatricule = robot.lookup("#tfMatricule").queryAs(TextField.class);
        TextField tfMarque = robot.lookup("#tfMarque").queryAs(TextField.class);
        TextField tfModele = robot.lookup("#tfModele").queryAs(TextField.class);
        TextField tfAnnee = robot.lookup("#tfAnnee").queryAs(TextField.class);
        TextField tfPrix = robot.lookup("#tfPrix").queryAs(TextField.class);

        // Verify nodes exist and accept text (do not trigger DB operations)
        robot.clickOn(tfId).write("1");
        robot.clickOn(tfMatricule).write("1234-AB");
        robot.clickOn(tfMarque).write("Toyota");
        robot.clickOn(tfModele).write("Corolla");
        robot.clickOn(tfAnnee).write("2020");
        robot.clickOn(tfPrix).write("50");

        assertThat(tfId.getText()).isEqualTo("1");
        assertThat(tfMatricule.getText()).isEqualTo("1234-AB");
        assertThat(tfMarque.getText()).isEqualTo("Toyota");
        assertThat(tfModele.getText()).isEqualTo("Corolla");
        assertThat(tfAnnee.getText()).isEqualTo("2020");
        assertThat(tfPrix.getText()).isEqualTo("50");
    }
}