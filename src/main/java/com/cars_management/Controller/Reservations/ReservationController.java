package com.cars_management.Controller.Reservations;

import com.cars_management.Controller.Cars.Car;
import com.cars_management.Repository.CarRepository;
import com.cars_management.Repository.IReservationRepository;
import com.cars_management.Repository.ReservationRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationController {

    @FXML private TextField clientField;
    @FXML private ComboBox<Car> cbCars;
    @FXML private TextField priceField;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private TextField daysField;

    @FXML private TableView<Reservation> tableReservation;
    @FXML private TableColumn<Reservation, String> colClient;
    @FXML private TableColumn<Reservation, String> colCar;
    @FXML private TableColumn<Reservation, Integer> colDays;
    @FXML private TableColumn<Reservation, Double> colTotal;

    private final CarRepository carRepo = new CarRepository();
    private final IReservationRepository reservationRepo = new ReservationRepository();

    private Reservation selected = null;

    // ------------------ INITIALISATION ------------------

    @FXML
    public void initialize() {

        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colCar.setCellValueFactory(new PropertyValueFactory<>("car"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tableReservation.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> fillFields(newSel)
        );

        cbCars.setItems(FXCollections.observableArrayList(carRepo.findAll()));

        cbCars.setOnAction(e -> onCarSelected());
        startDateField.setOnAction(e -> onDatesChanged());
        endDateField.setOnAction(e -> onDatesChanged());
        priceField.textProperty().addListener((a, b, c) -> onDatesChanged());

        loadReservations();
    }

    // ------------------ CHARGEMENT ------------------

    private void loadReservations() {
        tableReservation.setItems(FXCollections.observableArrayList(reservationRepo.findAll()));
    }

    // ------------------ AUTO REMPLISSAGE ------------------

    private void fillFields(Reservation r) {
        if (r == null) return;

        selected = r;

        clientField.setText(r.getClient());
        daysField.setText(String.valueOf(r.getDays()));
        priceField.setText(String.valueOf(r.getPricePerDay()));
        startDateField.setValue(r.getStartDate());
        endDateField.setValue(r.getEndDate());

        cbCars.getItems().stream()
                .filter(c -> (c.getMarqueModel() + " - " + c.getMatricule()).equals(r.getCar()))
                .findFirst()
                .ifPresent(c -> cbCars.getSelectionModel().select(c));
    }

    // ------------------ CHOIX VOITURE ------------------

    @FXML
    private void onCarSelected() {
        Car c = cbCars.getSelectionModel().getSelectedItem();
        if (c == null) return;

        priceField.setText(String.valueOf(c.getPrix()));
        onDatesChanged();
    }

    // ------------------ CALCUL ------------------

    @FXML
    private void onDatesChanged() {

        LocalDate start = startDateField.getValue();
        LocalDate end = endDateField.getValue();

        if (start == null || end == null || end.isBefore(start)) {
            daysField.setText("");
            return;
        }

        long days = ChronoUnit.DAYS.between(start, end);
        daysField.setText(String.valueOf(days));
    }

    // ------------------ AJOUT ------------------

    @FXML
    private void addReservation() {
        try {
            // Validation des champs
            if (clientField.getText().isEmpty()) {
                showError("Veuillez saisir le nom du client.");
                return;
            }
            if (cbCars.getSelectionModel().getSelectedItem() == null) {
                showError("Veuillez choisir une voiture.");
                return;
            }
            if (startDateField.getValue() == null || endDateField.getValue() == null) {
                showError("Veuillez choisir la date de début et la date de fin.");
                return;
            }
            if (priceField.getText().isEmpty()) {
                showError("Veuillez saisir / vérifier le prix par jour.");
                return;
            }
            if (daysField.getText().isEmpty()) {
                showError("Le nombre de jours est vide. Vérifiez les dates.");
                return;
            }

            // Construire l'objet
            Reservation r = buildReservation(null);

            // Sauvegarde en base
            boolean ok = reservationRepo.save(r);
            if (!ok) {
                showError("La sauvegarde en base a échoué (voir console).");
                return;
            }

            // Recharger la table
            loadReservations();
            clearFields();
            showInfo("Réservation ajoutée.");

        } catch (NumberFormatException e) {
            showError("Vérifiez le prix et le nombre de jours (valeurs numériques).");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur inattendue lors de l'ajout.");
        }
    }


    // ------------------ MODIFIER ------------------

    @FXML
    private void handleModifierReservation() {

        if (selected == null) {
            showError("Sélectionnez une réservation.");
            return;
        }

        try {
            Reservation r = buildReservation(selected.getId());
            reservationRepo.update(selected.getId(), r);

            loadReservations();
            clearFields();
            selected = null;

            showInfo("Réservation modifiée.");

        } catch (Exception e) {
            showError("Erreur lors de la modification.");
        }
    }

    // ------------------ SUPPRIMER ------------------

    @FXML
    private void handleSupprimerReservation() {
        if (selected == null) {
            showError("Sélectionnez une réservation.");
            return;
        }

        reservationRepo.delete(selected.getId());
        loadReservations();
        clearFields();
        selected = null;

        showInfo("Réservation supprimée.");
    }

    // ------------------ BUILD OBJET ------------------

    private Reservation buildReservation(Integer id) {

        Car c = cbCars.getSelectionModel().getSelectedItem();

        int days = Integer.parseInt(daysField.getText());
        double price = Double.parseDouble(priceField.getText());
        double total = days * price;

        return new Reservation(
                id,
                clientField.getText(),
                (c == null ? "" : c.getMarqueModel() + " - " + c.getMatricule()),
                days,
                total,
                startDateField.getValue(),
                endDateField.getValue(),
                price
        );
    }

    // ------------------ RETOUR ------------------

    @FXML
    private void handleRetoure() {
        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/Fxml/dashBord.fxml"));

            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            javafx.stage.Stage stage = (javafx.stage.Stage) clientField.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour.");
        }
    }

    // ------------------ UTILS ------------------

    private void clearFields() {
        clientField.clear();
        cbCars.getSelectionModel().clearSelection();
        priceField.clear();
        startDateField.setValue(null);
        endDateField.setValue(null);
        daysField.clear();
        selected = null;
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.show();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.show();
    }
}
