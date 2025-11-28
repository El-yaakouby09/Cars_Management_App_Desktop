package com.cars_management.Controller.Contracts;

import com.cars_management.Repository.ContractRepository;
import com.cars_management.Repository.IContractRepository;

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

    private IContractRepository contractRepository;

    @FXML private TextField tfId;
    @FXML private TextField tfClientName;
    @FXML private TextField tfClientCNI;
    @FXML private TextField tfClientPhone;
    @FXML private TextField tfClientAddress;
    @FXML private DatePicker dpDriverLicenseDate;

    @FXML private TextField tfCarBrand;
    @FXML private TextField tfCarModel;
    @FXML private TextField tfCarPlate;

    @FXML private DatePicker dpRentalStartDate;
    @FXML private DatePicker dpRentalEndDate;
    @FXML private TextField tfRentalDays;
    @FXML private TextField tfPricePerDay;
    @FXML private TextField tfTotalPrice;

    @FXML private TableView<Contract> contractTable;
    @FXML private TableColumn<Contract, Integer> colId;
    @FXML private TableColumn<Contract, String> colClientName;
    @FXML private TableColumn<Contract, String> colCarPlate;
    @FXML private TableColumn<Contract, LocalDate> colStartDate;
    @FXML private TableColumn<Contract, LocalDate> colEndDate;
    @FXML private TableColumn<Contract, Double> colTotalPrice;

    @FXML
    public void initialize() {

        // Utiliser la vraie implémentation
        contractRepository = new ContractRepository();

        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colCarPlate.setCellValueFactory(new PropertyValueFactory<>("carPlate"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("rentalStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("rentalEndDate"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadContracts();

        // Calcul automatique prix total + jours
        dpRentalStartDate.setOnAction(e -> calculatePriceAndDays());
        dpRentalEndDate.setOnAction(e -> calculatePriceAndDays());
        tfPricePerDay.textProperty().addListener((obs, o, n) -> calculatePriceAndDays());
    }

    // Charger la liste
    private void loadContracts() {
        contractTable.setItems(FXCollections.observableArrayList(contractRepository.findAll()));
    }

    // Calcul nombre de jours + total
    private void calculatePriceAndDays() {
        LocalDate start = dpRentalStartDate.getValue();
        LocalDate end = dpRentalEndDate.getValue();

        if (start != null && end != null && end.isAfter(start)) {
            long days = ChronoUnit.DAYS.between(start, end);
            tfRentalDays.setText(String.valueOf(days));

            try {
                double price = Double.parseDouble(tfPricePerDay.getText());
                tfTotalPrice.setText(String.valueOf(price * days));
            } catch (Exception e) {
                tfTotalPrice.setText("0");
            }
        }
    }

    // ============= CRUD =============

    @FXML
    private void addContract() {
        try {
            Contract c = buildContract(null);
            contractRepository.save(c);
            loadContracts();
            clearForm();
            showAlert("Succès", "Contrat ajouté");
        } catch (Exception e) {
            showAlert("Erreur", "Vérifiez vos valeurs");
        }
    }

    @FXML
    private void updateContract() {
        if (tfId.getText().isEmpty()) {
            showAlert("Erreur", "Sélectionnez un contrat");
            return;
        }

        try {
            Contract c = buildContract(Integer.parseInt(tfId.getText()));
            contractRepository.update(c);
            loadContracts();
            clearForm();
            showAlert("Succès", "Contrat modifié");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur de modification");
        }
    }

    @FXML
    private void deleteContract() {
        if (tfId.getText().isEmpty()) {
            showAlert("Erreur", "Sélectionnez un contrat à supprimer");
            return;
        }

        contractRepository.delete(Integer.parseInt(tfId.getText()));
        loadContracts();
        clearForm();
        showAlert("Succès", "Contrat supprimé");
    }

    // Créer l’objet contrat
    private Contract buildContract(Integer id) {
        LocalDate start = dpRentalStartDate.getValue();
        LocalDate end = dpRentalEndDate.getValue();
        long days = ChronoUnit.DAYS.between(start, end);
        double price = Double.parseDouble(tfPricePerDay.getText());
        double total = days * price;

        return new Contract(
                id,
                tfClientName.getText(),
                tfClientCNI.getText(),
                tfClientPhone.getText(),
                tfClientAddress.getText(),
                dpDriverLicenseDate.getValue(),
                tfCarBrand.getText(),
                tfCarModel.getText(),
                tfCarPlate.getText(),
                start,
                end,
                (int) days,
                price,
                total
        );
    }

    // Sélection d’une ligne dans le tableau
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
            tfCarBrand.setText(c.getCarBrand());
            tfCarModel.setText(c.getCarModel());
            tfCarPlate.setText(c.getCarPlate());
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
        tfCarBrand.clear();
        tfCarModel.clear();
        tfCarPlate.clear();
        dpRentalStartDate.setValue(null);
        dpRentalEndDate.setValue(null);
        tfRentalDays.clear();
        tfPricePerDay.clear();
        tfTotalPrice.clear();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
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
