package com.cars_management.Controller;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class SettingsUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/settings.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testSettingsFields(FxRobot robot) {
        TextField tfNewUsername = robot.lookup("#tfNewUsername").queryAs(TextField.class);
        PasswordField pfNewPassword = robot.lookup("#pfNewPassword").queryAs(PasswordField.class);
        PasswordField pfConfirm = robot.lookup("#pfConfirmPassword").queryAs(PasswordField.class);

        assertThat(tfNewUsername).isNotNull();
        assertThat(pfNewPassword).isNotNull();
        assertThat(pfConfirm).isNotNull();

        robot.clickOn(tfNewUsername).write("newuser");
        robot.clickOn(pfNewPassword).write("secret");
        robot.clickOn(pfConfirm).write("secret");

        assertThat(tfNewUsername.getText()).isEqualTo("newuser");
    }
}