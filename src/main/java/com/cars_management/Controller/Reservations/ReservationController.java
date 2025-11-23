package com.cars_management.Controller.Reservations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservationController {

    @FXML
    private TextField clientField;

    @FXML
    private TextField carField;

    @FXML
    private TextField daysField;

    @FXML
    private TableView<Reservation> tableReservation;

    @FXML
    private TableColumn<Reservation, String> colClient;

    @FXML
    private TableColumn<Reservation, String> colCar;

    @FXML
    private TableColumn<Reservation, Integer> colDays;

    @FXML
    private TableColumn<Reservation, Double> colTotal;

    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private ReservationService service = new ReservationService();
    private com.cars_management.Repository.ReservationRepository reservationRepository = new com.cars_management.Repository.ReservationRepository();

    @FXML
    public void initialize() {

        colClient.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getClient()));
        colCar.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCar()));
        colDays.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getDays()).asObject());
        colTotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotal()).asObject());

        tableReservation.setItems(reservations);
        // load persisted reservations
        reservations.addAll(reservationRepository.findAll());
    }

    @FXML
    private void addReservation() {

        String client = clientField.getText();
        String car = carField.getText();
        int days;

        try {
            days = Integer.parseInt(daysField.getText());
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez entrer un nombre valide pour les jours !");
            return;
        }

        double total = service.calculerTotal(days);

        Reservation r = new Reservation(client, car, days, total);
        reservations.add(r);
        reservationRepository.save(r);

        clientField.clear();
        carField.clear();
        daysField.clear();
    }

    @FXML
    public void handleRetoure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("dashBord");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
