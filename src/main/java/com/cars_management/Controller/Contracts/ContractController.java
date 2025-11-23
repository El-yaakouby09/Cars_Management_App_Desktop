package com.cars_management.Controller.Contracts;

import com.cars_management.Repository.ContractRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContractController {
    private ContractRepository contractRepository;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfClientName;
    @FXML
    private TextField tfClientCNI;
    @FXML
    private TextField tfClientPhone;
    @FXML
    private TextField tfClientAddress;
    @FXML
    private DatePicker dpDriverLicenseDate;
    
    @FXML
    private TextField tfCarBrand;
    @FXML
    private TextField tfCarModel;
    @FXML
    private TextField tfCarPlate;
    
    @FXML
    private DatePicker dpRentalStartDate;
    @FXML
    private DatePicker dpRentalEndDate;
    @FXML
    private TextField tfRentalDays;
    @FXML
    private TextField tfPricePerDay;
    @FXML
    private TextField tfTotalPrice;
    
    @FXML
    private TableView<Contract> contractTable;
    @FXML
    private TableColumn<Contract, Integer> colId;
    @FXML
    private TableColumn<Contract, String> colClientName;
    @FXML
    private TableColumn<Contract, String> colCarPlate;
    @FXML
    private TableColumn<Contract, LocalDate> colStartDate;
    @FXML
    private TableColumn<Contract, LocalDate> colEndDate;
    @FXML
    private TableColumn<Contract, Double> colTotalPrice;

    @FXML
    public void initialize() {
        contractRepository = new ContractRepository();
        
        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colCarPlate.setCellValueFactory(new PropertyValueFactory<>("carPlate"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("rentalStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("rentalEndDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadContracts();

        // Add listener to calculate rental days and total price when dates change
        dpRentalStartDate.setOnAction(e -> calculatePriceAndDays());
        dpRentalEndDate.setOnAction(e -> calculatePriceAndDays());
        tfPricePerDay.textProperty().addListener((obs, oldVal, newVal) -> calculatePriceAndDays());
    }

    private void loadContracts() {
        ObservableList<Contract> contracts = FXCollections.observableArrayList(
                contractRepository.findAll()
        );
        contractTable.setItems(contracts);
    }

    private void calculatePriceAndDays() {
        LocalDate startDate = dpRentalStartDate.getValue();
        LocalDate endDate = dpRentalEndDate.getValue();
        
        if (startDate != null && endDate != null && endDate.isAfter(startDate)) {
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            tfRentalDays.setText(String.valueOf(days));
            
            try {
                double pricePerDay = Double.parseDouble(tfPricePerDay.getText());
                double totalPrice = days * pricePerDay;
                tfTotalPrice.setText(String.format("%.2f", totalPrice));
            } catch (NumberFormatException e) {
                tfTotalPrice.setText("0.00");
            }
        } else {
            tfRentalDays.setText("0");
            tfTotalPrice.setText("0.00");
        }
    }

    @FXML
    private void addContract() {
        try {
            LocalDate startDate = dpRentalStartDate.getValue();
            LocalDate endDate = dpRentalEndDate.getValue();
            
            if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
                showAlert("Erreur", "Les dates de location sont invalides.");
                return;
            }

            long days = ChronoUnit.DAYS.between(startDate, endDate);
            double pricePerDay = Double.parseDouble(tfPricePerDay.getText());
            double totalPrice = days * pricePerDay;

            Contract contract = new Contract(
                    null,
                    tfClientName.getText(),
                    tfClientCNI.getText(),
                    tfClientPhone.getText(),
                    tfClientAddress.getText(),
                    dpDriverLicenseDate.getValue(),
                    tfCarBrand.getText(),
                    tfCarModel.getText(),
                    tfCarPlate.getText(),
                    startDate,
                    endDate,
                    (int) days,
                    pricePerDay,
                    totalPrice
            );

            contractRepository.save(contract);
            clearForm();
            loadContracts();
            showAlert("Succès", "Contrat enregistré avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour les prix.");
        }
    }

    @FXML
    private void updateContract() {
        try {
            if (tfId.getText().isEmpty()) {
                showAlert("Erreur", "Sélectionnez un contrat à modifier.");
                return;
            }

            LocalDate startDate = dpRentalStartDate.getValue();
            LocalDate endDate = dpRentalEndDate.getValue();
            
            if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
                showAlert("Erreur", "Les dates de location sont invalides.");
                return;
            }

            long days = ChronoUnit.DAYS.between(startDate, endDate);
            double pricePerDay = Double.parseDouble(tfPricePerDay.getText());
            double totalPrice = days * pricePerDay;

            Contract contract = new Contract(
                    Integer.parseInt(tfId.getText()),
                    tfClientName.getText(),
                    tfClientCNI.getText(),
                    tfClientPhone.getText(),
                    tfClientAddress.getText(),
                    dpDriverLicenseDate.getValue(),
                    tfCarBrand.getText(),
                    tfCarModel.getText(),
                    tfCarPlate.getText(),
                    startDate,
                    endDate,
                    (int) days,
                    pricePerDay,
                    totalPrice
            );

            contractRepository.update(contract);
            clearForm();
            loadContracts();
            showAlert("Succès", "Contrat modifié avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
        }
    }

    @FXML
    private void deleteContract() {
        try {
            if (tfId.getText().isEmpty()) {
                showAlert("Erreur", "Sélectionnez un contrat à supprimer.");
                return;
            }

            int contractId = Integer.parseInt(tfId.getText());
            contractRepository.delete(contractId);
            clearForm();
            loadContracts();
            showAlert("Succès", "Contrat supprimé avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID de contrat invalide.");
        }
    }

    @FXML
    private void onTableRowClick() {
        Contract selectedContract = contractTable.getSelectionModel().getSelectedItem();
        if (selectedContract != null) {
            tfId.setText(String.valueOf(selectedContract.getId()));
            tfClientName.setText(selectedContract.getClientName());
            tfClientCNI.setText(selectedContract.getClientCNI());
            tfClientPhone.setText(selectedContract.getClientPhone());
            tfClientAddress.setText(selectedContract.getClientAddress());
            dpDriverLicenseDate.setValue(selectedContract.getDriverLicenseDate());
            tfCarBrand.setText(selectedContract.getCarBrand());
            tfCarModel.setText(selectedContract.getCarModel());
            tfCarPlate.setText(selectedContract.getCarPlate());
            dpRentalStartDate.setValue(selectedContract.getRentalStartDate());
            dpRentalEndDate.setValue(selectedContract.getRentalEndDate());
            tfRentalDays.setText(String.valueOf(selectedContract.getRentalDays()));
            tfPricePerDay.setText(String.valueOf(selectedContract.getPricePerDay()));
            tfTotalPrice.setText(String.format("%.2f", selectedContract.getTotalPrice()));
        }
    }

    @FXML
    private void clearForm() {
        tfId.clear();
        tfClientName.clear();
        tfClientCNI.clear();
        tfClientPhone.clear();
        tfClientAddress.clear();
        dpDriverLicenseDate.setValue(null);
        tfCarBrand.clear();
        tfCarModel.clear();
        tfCarPlate.clear();
        dpRentalStartDate.setValue(null);
        dpRentalEndDate.setValue(null);
        tfRentalDays.clear();
        tfPricePerDay.clear();
        tfTotalPrice.clear();
    }

    @FXML
    private void handleRetoure() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/dashBord.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) tfClientName.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Failed to load dashBord: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
