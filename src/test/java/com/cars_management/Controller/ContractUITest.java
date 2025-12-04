package com.cars_management.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class ContractUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/contract.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testContractFieldsExist(FxRobot robot) {
        TextField tfClientName = robot.lookup("#tfClientName").queryAs(TextField.class);
        TextField tfClientPhone = robot.lookup("#tfClientPhone").queryAs(TextField.class);
        ComboBox<?> cbCars = robot.lookup("#cbCars").queryAs(ComboBox.class);
        TextField tfPricePerDay = robot.lookup("#tfPricePerDay").queryAs(TextField.class);
        TextField tfTotalPrice = robot.lookup("#tfTotalPrice").queryAs(TextField.class);

        assertThat(tfClientName).isNotNull();
        assertThat(tfClientPhone).isNotNull();
        assertThat(cbCars).isNotNull();
        assertThat(tfPricePerDay).isNotNull();
        assertThat(tfTotalPrice).isNotNull();

        robot.clickOn(tfClientName).write("Jean Dupont");
        robot.clickOn(tfClientPhone).write("0612345678");
        robot.clickOn(tfPricePerDay).write("120");

        assertThat(tfClientName.getText()).isEqualTo("Jean Dupont");
        assertThat(tfClientPhone.getText()).isEqualTo("0612345678");
        assertThat(tfPricePerDay.getText()).isEqualTo("120");
    }
}