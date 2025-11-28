package com.cars_management.Controller.Reservations;

import com.cars_management.Controller.Cars.Car;
import com.cars_management.Repository.CarRepository;
import com.cars_management.Repository.IReservationRepository;
import com.cars_management.Repository.ReservationRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationController {

    @FXML private TextField clientField;
    @FXML private ComboBox<Car> cbCars;
    @FXML private TextField priceField;
    @FXML private DatePicker dpStart;
    @FXML private DatePicker dpEnd;
    @FXML private TextField daysField;

    @FXML private TableView<Reservation> tableReservation;
    @FXML private TableColumn<Reservation, String> colClient;
    @FXML private TableColumn<Reservation, String> colCar;
    @FXML private TableColumn<Reservation, Integer> colDays;
    @FXML private TableColumn<Reservation, Double> colTotal;

    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private final IReservationRepository reservationRepository = new ReservationRepository();
    private final ReservationService reservationService = new ReservationService();

    private Reservation selectedReservation = null;

    @FXML
    public void initialize() {

        // Table columns
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colCar.setCellValueFactory(new PropertyValueFactory<>("car"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Charger les réservations existantes
        reservations.addAll(reservationRepository.findAll());
        tableReservation.setItems(reservations);

        // Listener sélection table
        tableReservation.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedReservation = newVal;
            if (newVal != null) {
                fillFormFromReservation(newVal);
            }
        });

        // Charger les voitures dans la ComboBox
        CarRepository carRepo = new CarRepository();
        cbCars.setItems(FXCollections.observableArrayList(carRepo.findAll()));

        cbCars.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                setText(empty || car == null ? "" :
                        car.getMarque() + " " + car.getModele() + " - " + car.getMatricule());
            }
        });

        cbCars.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);
                setText(empty || car == null ? "" :
                        car.getMarque() + " " + car.getModele() + " - " + car.getMatricule());
            }
        });

        // recalcul auto des jours / total quand les dates ou le prix changent
        dpStart.setOnAction(e -> calculateDays());
        dpEnd.setOnAction(e -> calculateDays());
        priceField.textProperty().addListener((obs, o, n) -> calculateDays());
    }

    // Remplir le formulaire quand on clique sur une ligne
    private void fillFormFromReservation(Reservation r) {
        clientField.setText(r.getClient());
        // on ne connaît que le nom de la voiture (String) => on ne re-sélectionne pas le Car
        // tu peux améliorer plus tard si tu ajoutes une colonne "carId"
        daysField.setText(String.valueOf(r.getDays()));
        priceField.setText(""); // impossible de le déduire à 100% (total / days)
        dpStart.setValue(null);
        dpEnd.setValue(null);
    }

    // ====== ComboBox voiture sélectionnée ======
    @FXML
    private void onCarSelected() {
        Car car = cbCars.getSelectionModel().getSelectedItem();
        if (car != null && car.getPrix() != null) {
            priceField.setText(String.valueOf(car.getPrix()));
            calculateDays();
        }
    }

    // ====== Calcul du nombre de jours (dpStart/dpEnd) ======
    private void calculateDays() {
        LocalDate start = dpStart.getValue();
        LocalDate end = dpEnd.getValue();

        if (start != null && end != null && !end.isBefore(start)) {
            long days = ChronoUnit.DAYS.between(start, end);
            daysField.setText(String.valueOf(days));
        }
    }

    // ====== AJOUTER ======
    @FXML
    private void addReservation() {
        try {
            Car selectedCar = cbCars.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                showAlert("Erreur", "Veuillez sélectionner une voiture.");
                return;
            }

            int days = Integer.parseInt(daysField.getText());
            double price = Double.parseDouble(priceField.getText());
            double total = reservationService.calculerTotal(days, price);

            Reservation r = new Reservation(
                    clientField.getText(),
                    selectedCar.getMarque() + " " + selectedCar.getModele(),
                    days,
                    total
            );

            if (reservationRepository.save(r)) {
                reservations.add(r);
                clearForm();
            } else {
                showAlert("Erreur", "Échec de l'enregistrement.");
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Vérifiez les valeurs (jours/prix).");
        }
    }

    // ====== MODIFIER ======
    @FXML
    private void handleModifierReservation(ActionEvent event) {
        if (selectedReservation == null) {
            showAlert("Erreur", "Veuillez sélectionner une réservation dans le tableau.");
            return;
        }

        try {
            Car selectedCar = cbCars.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                showAlert("Erreur", "Veuillez sélectionner une voiture.");
                return;
            }

            int days = Integer.parseInt(daysField.getText());
            double price = Double.parseDouble(priceField.getText());
            double total = reservationService.calculerTotal(days, price);

            selectedReservation.setClient(clientField.getText());
            selectedReservation.setCar(selectedCar.getMarque() + " " + selectedCar.getModele());
            selectedReservation.setDays(days);
            selectedReservation.setTotal(total);

            if (selectedReservation.getId() != null) {
                reservationRepository.update(selectedReservation.getId(), selectedReservation);
            }

            tableReservation.refresh();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Valeurs invalides pour jours/prix.");
        }
    }

    // ====== SUPPRIMER ======
    @FXML
    private void handleSupprimerReservation(ActionEvent event) {
        if (selectedReservation == null) {
            showAlert("Erreur", "Veuillez sélectionner une réservation.");
            return;
        }

        if (selectedReservation.getId() != null) {
            reservationRepository.delete(selectedReservation.getId());
        }

        reservations.remove(selectedReservation);
        clearForm();
    }

    // ====== RETOUR ======
    @FXML
    public void handleRetoure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("dashBord");
        stage.show();
    }

    // ====== Utils ======
    private void clearForm() {
        clientField.clear();
        cbCars.getSelectionModel().clearSelection();
        priceField.clear();
        dpStart.setValue(null);
        dpEnd.setValue(null);
        daysField.clear();
        tableReservation.getSelectionModel().clearSelection();
        selectedReservation = null;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
