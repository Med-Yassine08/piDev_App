package tn.scholarcircle.appfx.controllers.PersonDelivery.Securite;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.PersonDelivery;
import tn.scholarcircle.appfx.services.PersonDeliveryService;

import java.io.IOException;
import java.sql.SQLException;

public class ServicePersonDelivery {


    @FXML
    private TextField YourEmail;


    private PersonDeliveryService personDeliveryService;

    public ServicePersonDelivery() {
        personDeliveryService = new PersonDeliveryService();
    }

    @FXML
    void validation(ActionEvent event) {
        String email = YourEmail.getText();

        if (email.isEmpty()) {
            showAlert("Veuillez saisir votre e-mail.");
            return;
        }

        // Vérification si l'e-mail existe dans la base de données
        if (emailExistsInDatabase(email)) {
            // Charger une autre scène si l'e-mail existe
            loadNewScene("/tn/scholarcircle/appfx/PersonDelivery/Sercurite/StatPersonDelivery.fxml");
        } else {
            showAlert("L'e-mail n'existe pas dans la base de données.");
        }
    }

    private boolean emailExistsInDatabase(String email) {
        // Vérifier si l'e-mail existe dans la base de données en utilisant le service PersonDeliveryService
        try {
            for (PersonDelivery personDelivery : personDeliveryService.recuperer()) {
                if (personDelivery.getEmail().equals(email)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadNewScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) YourEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void UpdSupDv(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/Delivery/UpdateDelivery.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void HomePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/Delivery/ShowDelivery.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void AddDV(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/Delivery/AddDelivery.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void ServicePersonDelivery(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/PersonDelivery/Sercurite/ServicePersonDelivery.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Validationbtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/Validation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
