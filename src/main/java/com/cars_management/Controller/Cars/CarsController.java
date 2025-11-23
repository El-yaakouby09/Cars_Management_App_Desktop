package com.cars_management.Controller.Cars;

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

public class CarsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<Car> tableCars;

    @FXML
    private TableColumn<Car, Integer> colId;
    @FXML
    private TableColumn<Car, String> colMatricule;
    @FXML
    private TableColumn<Car, String> colMarque;
    @FXML
    private TableColumn<Car, String> colModele;
    @FXML
    private TableColumn<Car, Integer> colAnnee;
    @FXML
    private TableColumn<Car, Double> colPrix;

    @FXML
    private TextField tfId, tfMatricule, tfMarque, tfModele, tfAnnee, tfPrix;

    private final ObservableList<Car> carList = FXCollections.observableArrayList();

    private com.cars_management.Repository.CarRepository carRepository;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        tableCars.setItems(carList);

        // S'assurer que le TableView prend tout l'espace disponible
        VBox.setVgrow(tableCars, javafx.scene.layout.Priority.ALWAYS);

        // initialize repository and load existing cars from DB
        carRepository = new com.cars_management.Repository.CarRepository();
        carList.addAll(carRepository.findAll());
    }

    @FXML
    private void addCar() {
        try {
            int id = Integer.parseInt(tfId.getText());
            String matricule = tfMatricule.getText();
            String marque = tfMarque.getText();
            String modele = tfModele.getText();
            int annee = Integer.parseInt(tfAnnee.getText());
            double prix = Double.parseDouble(tfPrix.getText());
            Car car = new Car(id, marque, modele, annee, prix);
            car.setMatricule(matricule);
            carList.add(car);

            // persist
            carRepository.save(car);

            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Vérifiez les valeurs saisies.");
        }
    }

    @FXML
    private void deleteCar() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected != null) {
            carList.remove(selected);
            carRepository.delete(selected.getId());
        } else {
            showAlert("Erreur", "Veuillez sélectionner une voiture à supprimer.");
        }
    }

    @FXML
    private void updateCar() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setId(Integer.parseInt(tfId.getText()));
                selected.setMatricule(tfMatricule.getText());
                selected.setMarque(tfMarque.getText());
                selected.setModele(tfModele.getText());
                selected.setAnnee(Integer.parseInt(tfAnnee.getText()));
                selected.setPrix(Double.parseDouble(tfPrix.getText()));
                tableCars.refresh();
                clearFields();
                carRepository.update(selected);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Vérifiez les valeurs saisies.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une voiture à modifier.");
        }
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleRetoure(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("dashBord");
        stage.show();
    }
}
