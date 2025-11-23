package com.cars_management.Controller.Clients;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientController {

	public void handleRetoure(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("dashBord");
		stage.show();
	}
}
