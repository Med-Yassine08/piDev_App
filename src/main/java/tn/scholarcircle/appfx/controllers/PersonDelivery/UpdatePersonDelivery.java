package tn.scholarcircle.appfx.controllers.PersonDelivery;

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
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.PersonDelivery;
import tn.scholarcircle.appfx.services.PersonDeliveryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UpdatePersonDelivery {
    @FXML
    private TableColumn<PersonDelivery, String> AddressColl;

    @FXML
    private TextField AddressTF;

    @FXML
    private TableColumn<PersonDelivery, String> EmailColl;

    @FXML
    private TextField EmailTF;

    @FXML
    private TableColumn<PersonDelivery, String> FirstNameColl;

    @FXML
    private TextField FirstNameTF;

    @FXML
    private TableColumn<PersonDelivery, String> LastNameColl;

    @FXML
    private TextField LastNameTF;

    @FXML
    private TableColumn<PersonDelivery, String> PhoneColl;

    @FXML
    private TextField PhoneTF;

    @FXML
    private TableColumn<PersonDelivery, String> SectorColl;

    @FXML
    private ChoiceBox<String> SectorTF;

    @FXML
    private TableColumn<PersonDelivery, String> TransportationColl;

    @FXML
    private ChoiceBox<String> TransportationTF;

    @FXML
    private ChoiceBox<String> choicPage;

    @FXML
    private TableView<PersonDelivery> tableViewUPP;

    @FXML
    void DeleltPersonDelivery(ActionEvent event) {
        PersonDeliveryService personDeliveryService = new PersonDeliveryService();
        PersonDelivery personDelivery = tableViewUPP.getSelectionModel().getSelectedItem(); // Récupère la livraison sélectionnée dans la table
        if (personDelivery == null) {
            // Aucune livreur sélectionnée, afficher un message d'erreur
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
                personDeliveryService.supprimer(personDelivery.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Livraison supprimée avec succès !");
                alert.showAndWait();

                // Met à jour la liste observable utilisée par la table après la suppression
                List<PersonDelivery> personDeliveries = personDeliveryService.recuperer();
                ObservableList<PersonDelivery> observableList = FXCollections.observableList(personDeliveries);
                tableViewUPP.setItems(observableList);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void UpdatePersonDelivery(ActionEvent event) {
        PersonDeliveryService personDeliveryService = new PersonDeliveryService();
        PersonDelivery personDelivery = (PersonDelivery) tableViewUPP.getSelectionModel().getSelectedItem(); // Récupère la livraison sélectionnée dans la table
        if (personDelivery == null) {
            // Aucune livraison sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner une livraison à modifier.");
            alert.showAndWait();
            return;
        }
        // Met à jour les informations de la livraison avec les valeurs des champs de texte
        personDelivery.setFirstName(FirstNameTF.getText());
        personDelivery.setLastName(LastNameTF.getText());
        personDelivery.setAddress(AddressTF.getText());
        personDelivery.setEmail((EmailTF.getText()));
        personDelivery.setPhone(Integer.parseInt(PhoneTF.getText()));
        personDelivery.setSector(SectorTF.getValue());
        personDelivery.setTransportation(TransportationTF.getValue());

        try {
            personDeliveryService.modifier(personDelivery);
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
        List<PersonDelivery> personDeliveries = null;
        try {
            personDeliveries = personDeliveryService.recuperer();
            ObservableList<PersonDelivery> observableList = FXCollections.observableList(personDeliveries);
            tableViewUPP.setItems(observableList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void initialize() {
        PersonDeliveryService personDeliveryService = new PersonDeliveryService();
        try {
            List<PersonDelivery> personDeliveries = personDeliveryService.recuperer();
            ObservableList<PersonDelivery> observableList = FXCollections.observableList(personDeliveries);
            tableViewUPP.setItems(observableList);
            FirstNameColl.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
            LastNameColl.setCellValueFactory(new PropertyValueFactory<>("LastName"));
            AddressColl.setCellValueFactory(new PropertyValueFactory<>("Address"));
            EmailColl.setCellValueFactory(new PropertyValueFactory<>("Email"));
            PhoneColl.setCellValueFactory(new PropertyValueFactory<>("Phone"));
            SectorColl.setCellValueFactory(new PropertyValueFactory<>("Sector"));
            TransportationColl.setCellValueFactory(new PropertyValueFactory<>("Transportation"));

            tableViewUPP.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // Remplir les TextField avec les données de la ligne sélectionnée
                    FirstNameTF.setText(newSelection.getFirstName());
                    LastNameTF.setText(newSelection.getLastName());
                    AddressTF.setText(newSelection.getAddress());
                    EmailTF.setText(newSelection.getEmail());
                    PhoneTF.setText(String.valueOf(newSelection.getPhone()));
                    SectorTF.setValue(newSelection.getSector());
                    TransportationTF.setValue(newSelection.getTransportation());
                }
            });

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la récupération des livraisons : " + e.getMessage());
            alert.showAndWait();
        }

        // Remplir la ChoiceBox avec les options de statut
        TransportationTF.getItems().addAll("Voiture", "Vélo", "Moto","Scooter");

        // Remplir la ChoiceBox avec les options de statut
        SectorTF.getItems().addAll("Tunis","Ariana","Ben Arous","Manouba","Nabeul","Zaghouan","Bizerte","Béja","Jendouba","Kef","Siliana","Kairouan","Kasserine","Sidi-Bouzid","Sousse","Mahdia","Monastir","Gabès","Médenine","Tataouine","Gafsa","Tozeur","Kebili","Ben-Gardane");
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
