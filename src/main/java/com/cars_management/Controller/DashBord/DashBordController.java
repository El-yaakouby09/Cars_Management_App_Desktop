package com.cars_management.Controller.DashBord;

import com.cars_management.Repository.CarRepository;
import com.cars_management.Repository.ContractRepository;
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

    private Stage stage;
    private Scene scene;
    private Parent root;

    // ---- DASHBOARD FIELDS ----
    @FXML private Text txtCars;      // Nombre de voitures
    @FXML private Text txtClients;   // Nombre de clients
    @FXML private Text txtRevenus;   // Revenus totaux

    // ---- Repositories ----
    private final CarRepository carRepo = new CarRepository();
    private final ContractRepository contractRepo = new ContractRepository();

    // ---- INITIALISATION ----
    @FXML
    public void initialize() {
        loadDashboardStats();
    }

    private void loadDashboardStats() {

        // Nombre total de voitures
        int totalCars = carRepo.countCars();
        txtCars.setText(String.valueOf(totalCars));

        // Nombre total de clients (contrats)
        int totalClients = contractRepo.countContracts();
        txtClients.setText(String.valueOf(totalClients));

        // Revenus totaux (sommes des contrats)
        double totalRevenue = contractRepo.sumRevenues();
        txtRevenus.setText(totalRevenue + " MAD");
    }

    // ---------------- NAVIGATION ----------------

    public void handlLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    public void handlCars(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/cars.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cars");
        stage.show();
    }

    public void handlReservations(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/reservation.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Reservations");
        stage.show();
    }

    public void handleContracts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/contract.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Contrats");
        stage.show();
    }

    public void handleSettings(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/settings.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Paramètres");
        stage.show();
    }
}
