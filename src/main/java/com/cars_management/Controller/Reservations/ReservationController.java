package com.cars_management.Controller.Reservations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @FXML
    public void initialize() {

        colClient.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getClient()));
        colCar.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCar()));
        colDays.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getDays()).asObject());
        colTotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotal()).asObject());

        tableReservation.setItems(reservations);
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

        reservations.add(new Reservation(client, car, days, total));

        clientField.clear();
        carField.clear();
        daysField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
