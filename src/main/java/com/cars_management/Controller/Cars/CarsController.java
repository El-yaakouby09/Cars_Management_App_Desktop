package com.cars_management.Controller.Cars;

import com.cars_management.Repository.ICarRepository;
import com.cars_management.Repository.CarRepository;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;

public class CarsController {

    @FXML private TableView<Car> tableCars;

    @FXML private TableColumn<Car, Integer> colId;
    @FXML private TableColumn<Car, String> colMatricule;
    @FXML private TableColumn<Car, String> colMarque;
    @FXML private TableColumn<Car, String> colModele;
    @FXML private TableColumn<Car, Integer> colAnnee;
    @FXML private TableColumn<Car, Double> colPrix;

    @FXML private TextField tfId, tfMatricule, tfMarque, tfModele, tfAnnee, tfPrix;

    private final ObservableList<Car> carList = FXCollections.observableArrayList();

    private ICarRepository carRepository;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        tableCars.setItems(carList);
        VBox.setVgrow(tableCars, javafx.scene.layout.Priority.ALWAYS);

        carRepository = new CarRepository();

        // Chargement + tri par ID avec STREAMS
        carList.setAll(
                carRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Car::getId))
                        .toList()
        );

        // Remplir les champs automatiquement
        tableCars.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> fillForm(newVal));
    }

    // Remplissage automatique des champs
    private void fillForm(Car c) {
        if (c == null) return;

        tfId.setText(String.valueOf(c.getId()));
        tfMatricule.setText(c.getMatricule());
        tfMarque.setText(c.getMarque());
        tfModele.setText(c.getModele());
        tfAnnee.setText(String.valueOf(c.getAnnee()));
        tfPrix.setText(String.valueOf(c.getPrix()));
    }

    // --------ADD CAR
    @FXML
    private void addCar() {
        try {
            int id = Integer.parseInt(tfId.getText());

            // Vérifier si l’ID existe déjà
            boolean exists = carList.stream().anyMatch(c -> c.getId() == id);
            if (exists) {
                showAlert("Erreur", "ID déjà existant.");
                return;
            }

            String matricule = tfMatricule.getText();

            // Vérifier matricule
            boolean matriculeExists = carList.stream()
                    .anyMatch(c -> c.getMatricule().equalsIgnoreCase(matricule));

            if (matriculeExists) {
                showAlert("Erreur", "Matricule déjà existant.");
                return;
            }

            String marque = tfMarque.getText();
            String modele = tfModele.getText();
            int annee = Integer.parseInt(tfAnnee.getText());
            double prix = Double.parseDouble(tfPrix.getText());

            Car car = new Car(id, marque, modele, annee, prix);
            car.setMatricule(matricule);

            carRepository.save(car);

            // Recharge trié
            carList.setAll(
                    carRepository.findAll()
                            .stream()
                            .sorted(Comparator.comparing(Car::getId))
                            .toList()
            );

            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Vérifiez les valeurs saisies.");
        }
    }

    // -------- UPDATE CAR
    @FXML
    private void updateCar() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Erreur", "Sélectionnez une voiture.");
            return;
        }

        try {
            selected.setId(Integer.parseInt(tfId.getText()));
            selected.setMatricule(tfMatricule.getText());
            selected.setMarque(tfMarque.getText());
            selected.setModele(tfModele.getText());
            selected.setAnnee(Integer.parseInt(tfAnnee.getText()));
            selected.setPrix(Double.parseDouble(tfPrix.getText()));

            carRepository.update(selected);

            tableCars.refresh();
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Valeurs invalides.");
        }
    }

    // -------- DELETE CAR
    @FXML
    private void deleteCar() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Erreur", "Sélectionnez une voiture.");
            return;
        }

        carRepository.delete(selected.getId());

        carList.removeIf(c -> c.getId() == selected.getId());
        clearFields();
    }

    private void clearFields() {
        tfId.clear();
        tfMatricule.clear();
        tfMarque.clear();
        tfModele.clear();
        tfAnnee.clear();
        tfPrix.clear();
    }

    @FXML
    public void handleRetoure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
