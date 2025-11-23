package com.cars_management.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;        // ✔ CORRECT
import javafx.scene.control.PasswordField;    // ✔ CORRECT
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    public void switchPage2(ActionEvent event) throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || password.isEmpty()){
            showError("information invalide!!!");
            return;
        }else{
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("dashBord");
        stage.show(); }
    }

    private void showError(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
        alert.close();
    }

}
