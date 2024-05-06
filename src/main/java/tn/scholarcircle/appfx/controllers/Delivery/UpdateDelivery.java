package tn.scholarcircle.appfx.controllers.Delivery;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.services.DeliveryService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UpdateDelivery {

    @FXML
    private TableColumn<Delivery, String> AddressColl;

    @FXML
    private TextField AddressTF;

    @FXML
    private TableColumn<Delivery, String> CustomerColl;

    @FXML
    private TextField CustomerTF;

    @FXML
    private TableColumn<Delivery, String> DateColl;

    @FXML
    private DatePicker DateTF;

    @FXML
    private TableColumn<Delivery, String> FeesColl;

    @FXML
    private TextField FeesTF;

    @FXML
    private ChoiceBox<String> NamePDeliveryChoiceBox;

    @FXML
    private TableColumn<Delivery, String> PersonDeliveryColl;

    @FXML
    private TableColumn<Delivery, Integer> QuantityColl;

    @FXML
    private TextField QuantityTF;

    @FXML
    private TableColumn<Delivery, String> StatusColl;

    @FXML
    private ChoiceBox<String> StatusTF;

    @FXML
    private TableView<Delivery> TableViewUP;


    @FXML
    private ChoiceBox<String> choicPage;

    @FXML
    private AnchorPane contentArea;

    @FXML
    void CetteDate(ActionEvent event) {

    }

    @FXML
    void UpdateDelivery(ActionEvent event) {
        DeliveryService deliveryService = new DeliveryService();
        Delivery delivery = TableViewUP.getSelectionModel().getSelectedItem(); // Récupère la livraison sélectionnée dans la table
        if (delivery == null) {
            // Aucune livraison sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner une Delivery à modifier.");
            alert.showAndWait();
            return;
        }

        // Vérifier si les champs de texte sont vides
        if (AddressTF.getText().isEmpty() || CustomerTF.getText().isEmpty() || DateTF.getValue() == null ||
                FeesTF.getText().isEmpty() || NamePDeliveryChoiceBox.getValue().isEmpty() || QuantityTF.getText().isEmpty() ||
                StatusTF.getValue().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Vérifier si les valeurs peuvent être converties en types requis
        LocalDate DateDelivery = DateTF.getValue();
        Double FeesDelivery;
        try {
            FeesDelivery = Double.parseDouble(FeesTF.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez entrer un nombre valide pour les fess de livraison.");
            alert.showAndWait();
            return;
        }

        int QuantityDelivery;
        try {
            QuantityDelivery = Integer.parseInt(QuantityTF.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez entrer un nombre entier valide pour la quantité.");
            alert.showAndWait();
            return;
        }

        // Met à jour les informations de la livraison avec les valeurs des champs de texte
        delivery.setAddress(AddressTF.getText());
        delivery.setCustomer(CustomerTF.getText());
        delivery.setDate(DateTF.getValue());
        delivery.setFees(FeesDelivery.doubleValue());
        delivery.setQuantity(QuantityDelivery);
        delivery.setStatus(StatusTF.getValue());

        // Récupérez le nom complet sélectionné dans le ChoiceBox et enregistrez-le dans l'attribut NamePDelivery
        delivery.setNamePDelivery(NamePDeliveryChoiceBox.getValue());

        try {
            deliveryService.modifier(delivery);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setContentText("Livraison modifiée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        // Met à jour la table avec les nouvelles données
        List<Delivery> deliveries;
        try {
            deliveries = deliveryService.recuperer();
            ObservableList<Delivery> observableList = FXCollections.observableList(deliveries);
            TableViewUP.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        DeliveryService deliveryService = new DeliveryService();
        try {
            List<Delivery> deliveries = deliveryService.recuperer();
            ObservableList<Delivery> observableList = FXCollections.observableList(deliveries);
            TableViewUP.setItems(observableList);
            AddressColl.setCellValueFactory(new PropertyValueFactory<>("Address"));
            CustomerColl.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            DateColl.setCellValueFactory(new PropertyValueFactory<>("Date"));
            FeesColl.setCellValueFactory(new PropertyValueFactory<>("Fees"));
            QuantityColl.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            StatusColl.setCellValueFactory(new PropertyValueFactory<>("Status"));
            PersonDeliveryColl.setCellValueFactory(new PropertyValueFactory<>("NamePDelivery"));


            TableViewUP.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // Remplir les TextField avec les données de la ligne sélectionnée
                    AddressTF.setText(newSelection.getAddress());
                    CustomerTF.setText(newSelection.getCustomer());
                    DateTF.setValue(newSelection.getDate());
                    FeesTF.setText(String.valueOf(newSelection.getFees()));
                    QuantityTF.setText(String.valueOf(newSelection.getQuantity()));
                    StatusTF.setValue(newSelection.getStatus());
                    NamePDeliveryChoiceBox.setValue(newSelection.getNamePDelivery());

                }
            });
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la récupération des livraisons : " + e.getMessage());
            alert.showAndWait();
        }
        // Remplir la ChoiceBox avec les options de statut
        StatusTF.getItems().addAll("En cours", "En attente", "Livré");

        // Chargez les noms complets des livreurs depuis la base de données dans le ChoiceBox
        loadDeliveryNames();

    }

    private void loadDeliveryNames() {

        DeliveryService deliveryService = new DeliveryService();

        try {
            List<String> deliveryNames = deliveryService.getAllDeliveryNames();
            ObservableList<String> namesObservableList = FXCollections.observableArrayList(deliveryNames);
            NamePDeliveryChoiceBox.setItems(namesObservableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la récupération des DeliveryNames : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void DeleteDelivery(ActionEvent event) {
        DeliveryService deliveryService = new DeliveryService();
        Delivery delivery = TableViewUP.getSelectionModel().getSelectedItem(); // Récupère la livraison sélectionnée dans la table
        if (delivery == null) {
            // Aucune livraison sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner une livraison à supprimer.");
            alert.showAndWait();
            return;
        }

        // Demande de confirmation de suppression
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette livraison ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                deliveryService.supprimer(delivery.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Livraison supprimée avec succès !");
                alert.showAndWait();

                // Met à jour la liste observable utilisée par la table après la suppression
                List<Delivery> deliveries = deliveryService.recuperer();
                ObservableList<Delivery> observableList = FXCollections.observableList(deliveries);
                TableViewUP.setItems(observableList);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleChoiceChange() {
        String selectedOption = choicPage.getValue();

        if (selectedOption.equals("Delivery")) {
            loadFXML("/tn/scholarcircle/appfx/Delivery/UpdateDelivery.fxml");
        } else if (selectedOption.equals("PersonDelivery")) {
            loadFXML("/tn/scholarcircle/appfx/PersonDelivery/UpdatePersonDelivery.fxml");
        } else {
            // Gérer les cas d'erreur ou de sélection invalide
        }
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) choicPage.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'erreur de chargement du FXML
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
