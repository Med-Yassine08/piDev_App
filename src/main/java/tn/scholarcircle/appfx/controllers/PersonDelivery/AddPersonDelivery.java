package tn.scholarcircle.appfx.controllers.PersonDelivery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.PersonDelivery;
import tn.scholarcircle.appfx.services.PersonDeliveryService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class AddPersonDelivery {

    @FXML
    private TextField EmailTF;

    @FXML
    private TextField FirstNameTF;

    @FXML
    private TextField LastNameTF;

    @FXML
    private TextField PhoneTF;

    @FXML
    private ChoiceBox<String> SectorTF;

    @FXML
    private ChoiceBox<String> TransportationTF;
    @FXML
    private TextField AddressTF;

    @FXML
    void AjouterPersonDelivery(ActionEvent event) {
        PersonDeliveryService personDeliveryService = new PersonDeliveryService();
        PersonDelivery personDelivery = new PersonDelivery();
        personDelivery.setFirstName(FirstNameTF.getText());
        personDelivery.setLastName(LastNameTF.getText());
        personDelivery.setAddress(AddressTF.getText());
        personDelivery.setEmail(EmailTF.getText());
        personDelivery.setPhone(Integer.parseInt(PhoneTF.getText()));
        personDelivery.setSector(SectorTF.getValue());
        personDelivery.setTransportation(TransportationTF.getValue());

        //email
        try {
            personDeliveryService.ajouter(personDelivery);
            // Envoyer l'email après l'ajout réussi du PersonDelivery
            sendEmail(EmailTF.getText(), "Vous avez bien été créé dans l'entreprise Scholar Circle, veuillez vérifier vos données en affichant les champs de PersonDelivery avec son ID.");
            // Afficher une boîte de dialogue pour confirmer l'ajout
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Delivery added");
            alert.showAndWait();
        } catch (SQLException e) {
            // Afficher une boîte de dialogue en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la Ajout des PersonDelivery : " + e.getMessage());
            alert.showAndWait();
        }
    }
    private void sendEmail(String recipient, String messageBody) {
        // Adresse email et mot de passe de l'expéditeur
        final String username = "Ines.ketata@esprit.tn";
        final String password = "222JFT3747";

        // Paramètres pour la configuration du serveur SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Créer une session de messagerie avec authentification
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Créer un message email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Nouveau PersonDelivery ajouté");
            message.setText(messageBody);

            // Envoyer le message
            Transport.send(message);

            System.out.println("Email envoyé avec succès!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        // Remplir la ChoiceBox avec les options de statut
        TransportationTF.getItems().addAll("Voiture", "Vélo", "Moto","Scooter");

        // Remplir la ChoiceBox avec les options de statut
        SectorTF.getItems().addAll("Tunis","Ariana","Ben Arous","Manouba","Nabeul","Zaghouan","Bizerte","Béja","Jendouba","Kef","Siliana","Kairouan","Kasserine","Sidi-Bouzid","Sousse","Mahdia","Monastir","Gabès","Médenine","Tataouine","Gafsa","Tozeur","Kebili","Ben-Gardane");
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
