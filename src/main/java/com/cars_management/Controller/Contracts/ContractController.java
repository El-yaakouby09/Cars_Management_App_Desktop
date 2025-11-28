package com.cars_management.Controller.Contracts;

import com.cars_management.Controller.Cars.Car;
import com.cars_management.Repository.IContractRepository;
import com.cars_management.Repository.ContractRepository;
import com.cars_management.Repository.CarRepository;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContractController {

    private IContractRepository contractRepository;

    @FXML private TextField tfId;
    @FXML private TextField tfClientName;
    @FXML private TextField tfClientCNI;
    @FXML private TextField tfClientPhone;
    @FXML private TextField tfClientAddress;
    @FXML private DatePicker dpDriverLicenseDate;

    // ComboBox voitures
    @FXML private ComboBox<Car> cbCars;

    // Dates & prix
    @FXML private DatePicker dpRentalStartDate;
    @FXML private DatePicker dpRentalEndDate;
    @FXML private TextField tfRentalDays;
    @FXML private TextField tfPricePerDay;
    @FXML private TextField tfTotalPrice;

    // TABLE VIEW
    @FXML private TableView<Contract> contractTable;
    @FXML private TableColumn<Contract, Integer> colId;
    @FXML private TableColumn<Contract, String> colClientName;

    // NOUVELLE COLONNE "VOITURE"
    @FXML private TableColumn<Contract, String> colCarInfo;

    @FXML private TableColumn<Contract, LocalDate> colStartDate;
    @FXML private TableColumn<Contract, LocalDate> colEndDate;
    @FXML private TableColumn<Contract, Double> colTotalPrice;

    @FXML
    public void initialize() {

        contractRepository = new ContractRepository();

        // Init table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));

        // CHANGER PLAQUE → VOITURE (marque + modèle)
        colCarInfo.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getCarBrand() + " " + cell.getValue().getCarModel()
                )
        );

        colStartDate.setCellValueFactory(new PropertyValueFactory<>("rentalStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("rentalEndDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadContracts();

        // Load cars in ComboBox
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

        // Auto calculation
        dpRentalStartDate.setOnAction(e -> calculatePriceAndDays());
        dpRentalEndDate.setOnAction(e -> calculatePriceAndDays());
        tfPricePerDay.textProperty().addListener((obs, oldValue, newValue) -> calculatePriceAndDays());
    }

    @FXML
    private void onCarSelected() {
        Car car = cbCars.getSelectionModel().getSelectedItem();
        if (car != null) {
            tfPricePerDay.setText(String.valueOf(car.getPrix()));
            calculatePriceAndDays();
        }
    }

    private void calculatePriceAndDays() {
        LocalDate start = dpRentalStartDate.getValue();
        LocalDate end = dpRentalEndDate.getValue();

        if (start != null && end != null && !end.isBefore(start)) {
            long days = ChronoUnit.DAYS.between(start, end);
            tfRentalDays.setText(String.valueOf(days));

            try {
                double price = Double.parseDouble(tfPricePerDay.getText());
                tfTotalPrice.setText(String.valueOf(price * days));
            } catch (Exception ex) {
                tfTotalPrice.setText("0");
            }
        }
    }

    private void loadContracts() {
        contractTable.setItems(FXCollections.observableArrayList(contractRepository.findAll()));
    }

    @FXML
    private void addContract() {
        try {
            Contract contract = buildContract(null);
            contractRepository.save(contract);
            loadContracts();
            clearForm();
            showAlert("Succès", "Contrat ajouté !");
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez vérifier les informations.");
        }
    }

    @FXML
    private void updateContract() {
        if (tfId.getText().isEmpty()) {
            showAlert("Erreur", "Sélectionnez un contrat !");
            return;
        }

        try {
            Contract c = buildContract(Integer.parseInt(tfId.getText()));
            contractRepository.update(c);
            loadContracts();
            clearForm();
            showAlert("Succès", "Contrat modifié !");
        } catch (Exception e) {
            showAlert("Erreur", "Modification impossible !");
        }
    }

    @FXML
    private void deleteContract() {
        if (tfId.getText().isEmpty()) {
            showAlert("Erreur", "Sélectionnez un contrat !");
            return;
        }

        contractRepository.delete(Integer.parseInt(tfId.getText()));
        loadContracts();
        clearForm();
        showAlert("Succès", "Contrat supprimé !");
    }

    private Contract buildContract(Integer id) {

        Car selectedCar = cbCars.getSelectionModel().getSelectedItem();
        if (selectedCar == null)
            throw new RuntimeException("Aucune voiture sélectionnée");

        long days = Long.parseLong(tfRentalDays.getText());
        double price = Double.parseDouble(tfPricePerDay.getText());
        double total = price * days;

        return new Contract(
                id,
                tfClientName.getText(),
                tfClientCNI.getText(),
                tfClientPhone.getText(),
                tfClientAddress.getText(),
                dpDriverLicenseDate.getValue(),
                selectedCar.getMarque(),
                selectedCar.getModele(),
                selectedCar.getMatricule(),
                dpRentalStartDate.getValue(),
                dpRentalEndDate.getValue(),
                (int) days,
                price,
                total
        );
    }

    @FXML
    private void onTableRowClick() {
        Contract c = contractTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            tfId.setText(String.valueOf(c.getId()));
            tfClientName.setText(c.getClientName());
            tfClientCNI.setText(c.getClientCNI());
            tfClientPhone.setText(c.getClientPhone());
            tfClientAddress.setText(c.getClientAddress());
            dpDriverLicenseDate.setValue(c.getDriverLicenseDate());

            // Re-select car in ComboBox
            cbCars.getItems().forEach(car -> {
                if (car.getMatricule().equals(c.getCarPlate())) {
                    cbCars.getSelectionModel().select(car);
                }
            });

            dpRentalStartDate.setValue(c.getRentalStartDate());
            dpRentalEndDate.setValue(c.getRentalEndDate());
            tfRentalDays.setText(String.valueOf(c.getRentalDays()));
            tfPricePerDay.setText(String.valueOf(c.getPricePerDay()));
            tfTotalPrice.setText(String.valueOf(c.getTotalPrice()));
        }
    }

    private void clearForm() {
        tfId.clear();
        tfClientName.clear();
        tfClientCNI.clear();
        tfClientPhone.clear();
        tfClientAddress.clear();
        dpDriverLicenseDate.setValue(null);

        cbCars.getSelectionModel().clearSelection();

        dpRentalStartDate.setValue(null);
        dpRentalEndDate.setValue(null);
        tfRentalDays.clear();
        tfPricePerDay.clear();
        tfTotalPrice.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleRetoure() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/dashBord.fxml"));
            Stage stage = (Stage) tfClientName.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
