package com.cars_management.Controller.Auth;

import com.cars_management.Repository.UserRepository;
import com.cars_management.Setting.Session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    @FXML
    private TextField tfCurrentUsername;
    @FXML
    private TextField tfNewUsername;
    @FXML
    private PasswordField pfNewPassword;
    @FXML
    private PasswordField pfConfirmPassword;
    @FXML
    private Label lblStatus;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        String current = Session.getCurrentUsername();
        if (current != null) {
            tfCurrentUsername.setText(current);
        }
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        String current = tfCurrentUsername.getText();
        String newUsername = tfNewUsername.getText();
        String newPassword = pfNewPassword.getText();
        String confirm = pfConfirmPassword.getText();

        if ((newUsername == null || newUsername.isBlank()) && (newPassword == null || newPassword.isBlank())) {
            showAlert(Alert.AlertType.INFORMATION, "Aucune modification", "Aucun changement à enregistrer.");
            return;
        }

        boolean ok = true;

        if (newUsername != null && !newUsername.isBlank()) {
            boolean updated = userRepo.updateUsername(current, newUsername);
            if (updated) {
                Session.setCurrentUsername(newUsername);
                tfCurrentUsername.setText(newUsername);
                lblStatus.setText("Nom d'utilisateur mis à jour.");
            } else {
                lblStatus.setText("Échec mise à jour nom d'utilisateur (peut déjà exister).\n");
                ok = false;
            }
        }

        if (newPassword != null && !newPassword.isBlank()) {
            if (!newPassword.equals(confirm)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
                return;
            }
            boolean updated = userRepo.updatePassword(Session.getCurrentUsername(), newPassword);
            if (updated) {
                lblStatus.setText((lblStatus.getText()==null?"":"") + " Mot de passe mis à jour.");
            } else {
                lblStatus.setText((lblStatus.getText()==null?"":"") + " Échec mise à jour du mot de passe.");
                ok = false;
            }
        }

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les modifications ont été enregistrées.");
            // clear new fields
            tfNewUsername.clear();
            pfNewPassword.clear();
            pfConfirmPassword.clear();
        }
    }

    @FXML
    protected void handleCancel(ActionEvent event) throws IOException {
        // go back to dashboard
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/dashBord.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}
