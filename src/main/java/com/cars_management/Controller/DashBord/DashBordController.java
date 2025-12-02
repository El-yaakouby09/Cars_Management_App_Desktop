package com.cars_management.Controller.DashBord;

import com.cars_management.Repository.CarRepository;
import com.cars_management.Repository.ContractRepository;
import com.cars_management.Repository.TopCarRepository;
import com.cars_management.TopCar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBordController {

    @FXML private Text txtCars;
    @FXML private Text txtClients;
    @FXML private Text txtRevenus;
    @FXML private Text txtBestCar;

    private final CarRepository carRepo = new CarRepository();
    private final ContractRepository contractRepo = new ContractRepository();
    private final TopCarRepository topCarRepo = new TopCarRepository();

    @FXML
    public void initialize() {
        loadDashboardStats();
    }

    private void loadDashboardStats() {

        // ---- Voitures ----
        txtCars.setText(String.valueOf(carRepo.countCars()));

        // ---- Clients ----
        txtClients.setText(String.valueOf(contractRepo.countContracts()));

        // ---- Revenus ----
        txtRevenus.setText(contractRepo.sumRevenues() + " MAD");

        // ---- Voiture la plus louée ----
        topCarRepo.refresh();
        TopCar top = topCarRepo.findTopCar();

        if (top != null && top.getMarque() != null) {
            txtBestCar.setText(
                    top.getMarque() + " " + top.getModel());
        } else {
            txtBestCar.setText("-");
        }
    }

    // ---- Navigation ----

    public void handlLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handlCars(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/cars.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handlReservations(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/reservation.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleContracts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/contract.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/settings.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
