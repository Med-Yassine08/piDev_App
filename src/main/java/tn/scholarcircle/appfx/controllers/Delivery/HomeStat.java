package tn.scholarcircle.appfx.controllers.Delivery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.services.DeliveryService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeStat {
    @FXML
    private BarChart<String, Number> StatDelivery;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    void initialize() {
        DeliveryService deliveryService = new DeliveryService();

        // Récupération de toutes les livraisons
        List<Delivery> deliveries;
        try {
            deliveries = deliveryService.recuperer();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Map pour stocker les statistiques par date
        Map<LocalDate, Double> feesByDate = new HashMap<>();
        Map<LocalDate, Integer> quantityByDate = new HashMap<>();

        // Calcul des statistiques
        for (Delivery delivery : deliveries) {
            LocalDate date = delivery.getDate();
            if (date != null) {
                double fees = delivery.getFees();
                int quantity = delivery.getQuantity();

                feesByDate.put(date, feesByDate.getOrDefault(date, 0.0) + fees);
                quantityByDate.put(date, quantityByDate.getOrDefault(date, 0) + quantity);
            }
        }

        // Création des séries pour le graphique
        XYChart.Series<String, Number> seriesFees = new XYChart.Series<>();
        seriesFees.setName("Frais");

        XYChart.Series<String, Number> seriesQuantity = new XYChart.Series<>();
        seriesQuantity.setName("Quantité");

        // Ajout des données aux séries
        for (LocalDate date : feesByDate.keySet()) {
            seriesFees.getData().add(new XYChart.Data<>(date.toString(), feesByDate.get(date)));
            seriesQuantity.getData().add(new XYChart.Data<>(date.toString(), quantityByDate.get(date)));
        }

        // Configuration des axes du graphique
        xAxis.setLabel("Date");
        yAxis.setLabel("Valeur");

        // Ajout des séries au graphique
        StatDelivery.getData().addAll(seriesFees, seriesQuantity);

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
