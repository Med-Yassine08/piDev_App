package tn.scholarcircle.appfx.controllers.Delivery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.services.DeliveryService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddDelivery {

    @FXML
    private TextField AddressTF;

    @FXML
    private TextField CustomerTF;

    @FXML
    private DatePicker DateTF;

    @FXML
    private TextField FeesTF;

    @FXML
    private TextField QuantityTF;

    @FXML
    private ChoiceBox<String> StatusTF;

    @FXML
    private ChoiceBox<String> NamePDeliveryChoiceBox;

    private DeliveryService deliveryService;

    @FXML
    void initialize() {
        // Remplir la ChoiceBox avec les options de statut
        StatusTF.getItems().addAll("En Cours", "En Attente", "Annuler");

        // Initialisez le service de livraison
        deliveryService = new DeliveryService();

        // Chargez les noms complets des livreurs depuis la base de données dans le ChoiceBox
        loadDeliveryNames();
    }

    private void loadDeliveryNames() {
        try {
            List<String> deliveryNames = deliveryService.getAllDeliveryNames();
            ObservableList<String> namesObservableList = FXCollections.observableArrayList(deliveryNames);
            NamePDeliveryChoiceBox.setItems(namesObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AjouterDelivery(ActionEvent event) {

        // Vérifier si les champs obligatoires sont vides
        if (DateTF.getValue() == null || AddressTF.getText().isEmpty() || CustomerTF.getText().isEmpty() || StatusTF.getValue().isEmpty() || FeesTF.getText().isEmpty() || QuantityTF.getText().isEmpty()|| NamePDeliveryChoiceBox.getValue().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Valider les entrées numériques pour frais
        double Fees;
        try {
            Fees = Double.parseDouble(FeesTF.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir des valeurs numériques pour les Fees.");
            alert.showAndWait();
            return;
        }

        // Valider les entrées numériques pour frais et quantité
        int Quantity;
        try {
            Quantity = Integer.parseInt(QuantityTF.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir des valeurs numériques pour  la quantité.");
            alert.showAndWait();
            return;
        }

        // Créer une nouvelle instance de Delivery
        Delivery delivery = new Delivery();
        delivery.setDate(DateTF.getValue());
        delivery.setAddress(AddressTF.getText());
        delivery.setCustomer(CustomerTF.getText());
        delivery.setStatus(StatusTF.getValue());
        delivery.setFees(Double.parseDouble(FeesTF.getText()));
        delivery.setQuantity(Integer.parseInt(QuantityTF.getText()));

        // Récupérez le nom complet sélectionné dans le ChoiceBox et enregistrez-le dans l'attribut NamePDelivery
        delivery.setNamePDelivery(NamePDeliveryChoiceBox.getValue());

        try {
            deliveryService.ajouter(delivery);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Delivery added successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Delivery not added");
            alert.showAndWait();
        }
    }

    @FXML
    void CetteDate(ActionEvent event) {
// Définir la date d'aujourd'hui comme valeur du DatePicker
        DateTF.setValue(LocalDate.now());
    }

    @FXML
    void AjouterPersonDelivery(ActionEvent event) {
        try {
            // Charger la nouvelle interface (FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/PersonDelivery/AddPersonDelivery.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre (Stage) pour la nouvelle interface
            Stage newStage = new Stage();
            newStage.setTitle("Ajouter une nouvelle livraison de personne");

            // Définir la largeur et la hauteur de la nouvelle fenêtre

            newStage.setScene(new Scene(root));

            // Ajouter un écouteur pour l'événement de fermeture de la fenêtre
            newStage.setOnHidden(e -> refreshDeliveryNames());

            // Afficher la nouvelle fenêtre
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void refreshDeliveryNames() {
        try {
            List<String> deliveryNames = deliveryService.getAllDeliveryNames();
            ObservableList<String> namesObservableList = FXCollections.observableArrayList(deliveryNames);
            NamePDeliveryChoiceBox.setItems(namesObservableList);
        } catch (SQLException e) {
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
