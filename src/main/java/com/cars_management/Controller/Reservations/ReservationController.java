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
    private TextField clientField1;
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

    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private final ReservationService service = new ReservationService();

    // la réservation actuellement sélectionnée
    private Reservation selectedReservation = null;

    @FXML
    public void initialize() {

        // Colonnes
        colClient.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getClient()));
        colCar.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCar()));
        colDays.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getDays()).asObject());
        colTotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotal()).asObject());

        // Charger les données depuis la BD
        reservations.addAll(service.findAll());
        tableReservation.setItems(reservations);

        // Listener sur la sélection du tableau
        tableReservation.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            selectedReservation = newValue;
            if (newValue != null) {
                fillFields(newValue);
            }
        });
    }

    // Remplir les champs avec la réservation sélectionnée
    private void fillFields(Reservation r) {
        clientField.setText(r.getClient());
        carField.setText(r.getCar());
        daysField.setText(String.valueOf(r.getDays()));
    }

    // Effacer les champs
    private void clearFields() {
        clientField.clear();
        carField.clear();
        daysField.clear();
        selectedReservation = null;
        tableReservation.getSelectionModel().clearSelection();
    }

    // ------------ BOUTON AJOUTER ------------
    @FXML
    private void handleModifierReservation(ActionEvent event) {

        if (selectedReservation == null) {
            showAlert("Attention", "Veuillez sélectionner une réservation !");
            return;
        }

        try {
            selectedReservation.setClient(clientField.getText());
            selectedReservation.setCar(carField.getText());
            selectedReservation.setDays(Integer.parseInt(daysField.getText()));

            double price = Double.parseDouble(clientField1.getText());
            selectedReservation.setTotal(price * selectedReservation.getDays());

            boolean ok = service.update(selectedReservation.getId(), selectedReservation);
            if (!ok) {
                showAlert("Erreur", "La modification a échoué !");
                return;
            }

            reservations.clear();
            reservations.addAll(service.findAll());
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Champs invalides !");
        }
    }

    // ------------ BOUTON MODIFIER ------------
    @FXML
    private void addReservation() {
        try {
            String client = clientField.getText();
            String car = carField.getText();
            int days = Integer.parseInt(daysField.getText());
            double price = Double.parseDouble(clientField1.getText());

            double total = price * days;

            Reservation r = new Reservation(client, car, days, total);

            boolean ok = service.save(r);
            if (!ok) {
                showAlert("Erreur", "Échec de l'enregistrement !");
                return;
            }

            reservations.clear();
            reservations.addAll(service.findAll());

            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Veuillez remplir correctement les champs !");
        }
    }

    // ------------ BOUTON SUPPRIMER ------------
    @FXML
    private void handleSupprimerReservation(ActionEvent event) {

        if (selectedReservation == null) {
            showAlert("Attention", "Veuillez d'abord sélectionner une réservation.");
            return;
        }

        if (selectedReservation.getId() == null) {
            showAlert("Erreur", "Cette réservation n'a pas d'ID (rechargez la liste).");
            return;
        }

        boolean ok = service.delete(selectedReservation.getId());
        if (!ok) {
            showAlert("Erreur", "Échec de la suppression en base.");
            return;
        }

        reservations.remove(selectedReservation);
        clearFields();
    }

    // ------------ BOUTON RETOUR ------------
    @FXML
    public void handleRetoure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("dashBord");
        stage.show();
    }

    // ------------ ALERTES ------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
