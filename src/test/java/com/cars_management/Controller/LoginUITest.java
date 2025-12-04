package com.cars_management.Controller;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class LoginUITest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML directly to avoid calling controller start methods
        javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        stage.setScene(new javafx.scene.Scene(root));
        stage.show();
    }

    @Test
    void testLoginButton(FxRobot robot) {
        // Find the username and password fields
        TextField usernameField = robot.lookup("#usernameField").queryAs(TextField.class);
        PasswordField passwordField = robot.lookup("#passwordField").queryAs(PasswordField.class);
        // The login button in the FXML does not have fx:id, look it up by style class
        Button loginButton = robot.lookup(".buttonLogin").queryAs(Button.class);

        robot.clickOn(usernameField).write("admin");
        robot.clickOn(passwordField).write("password");

        // Verify fields contain the written text and button exists
        assertThat(usernameField.getText()).isEqualTo("admin");
        assertThat(passwordField.getText()).isEqualTo("password");
        assertThat(loginButton).isNotNull();
    }
}