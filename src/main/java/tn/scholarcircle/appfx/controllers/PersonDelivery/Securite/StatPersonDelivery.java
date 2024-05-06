package tn.scholarcircle.appfx.controllers.PersonDelivery.Securite;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.services.DeliveryService;
import java.time.LocalDate;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatPersonDelivery {
    @FXML
    private TextField deliveryPersonField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TreeView<String> TreeView;

    @FXML
    private PieChart PieChart;

    private DeliveryService deliveryService;

    @FXML
    void initialize() {
        deliveryService = new DeliveryService();
        afficherStatistiques();
    }

    private void afficherStatistiques() {
        try {
            // Récupérer toutes les livraisons
            List<Delivery> deliveries = deliveryService.recuperer();

            // Afficher les statistiques dans le TreeView
            afficherStatistiquesTreeView(deliveries);

            // Afficher les statistiques dans le PieChart
            afficherStatistiquesPieChart(deliveries);

        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de récupération des données
        }
    }

    private void afficherStatistiquesTreeView(List<Delivery> deliveries) {
        // Créer la racine de l'arbre
        TreeItem<String> root = new TreeItem<>("Statistiques des livraisons");

        // Créer une carte pour stocker les statistiques par date
        Map<String, Map<String, Integer>> statistiquesParDate = new HashMap<>();

        // Remplir la carte avec les statistiques
        for (Delivery delivery : deliveries) {
            String date = delivery.getDate().toString();
            String namePDelivery = delivery.getNamePDelivery();

            // Vérifier si la date existe déjà dans la carte
            if (!statistiquesParDate.containsKey(date)) {
                statistiquesParDate.put(date, new HashMap<>());
            }

            Map<String, Integer> statsParDate = statistiquesParDate.get(date);

            // Incrémenter le compteur de répétition pour le namePDelivery
            statsParDate.put(namePDelivery, statsParDate.getOrDefault(namePDelivery, 0) + 1);
        }

        // Créer des noeuds d'arbre pour chaque date et ses statistiques
        for (Map.Entry<String, Map<String, Integer>> entry : statistiquesParDate.entrySet()) {
            String date = entry.getKey();
            TreeItem<String> dateNode = new TreeItem<>(date);

            Map<String, Integer> statsParDate = entry.getValue();

            for (Map.Entry<String, Integer> stats : statsParDate.entrySet()) {
                String namePDelivery = stats.getKey();
                int repetition = stats.getValue();
                double salary = repetition * 2500;

                String statText = " - " +namePDelivery + " - " + repetition + " Delivery(s) - Salary: " + salary +" Dt ";
                TreeItem<String> statNode = new TreeItem<>(statText);
                dateNode.getChildren().add(statNode);
            }

            root.getChildren().add(dateNode);
        }

        // Définir la racine de l'arbre
        TreeView.setRoot(root);
    }

    private void afficherStatistiquesPieChart(List<Delivery> deliveries) {
        // Créer une carte pour stocker les statistiques par nomPDelivery
        Map<String, Integer> statistiques = new HashMap<>();

        // Remplir la carte avec les statistiques
        for (Delivery delivery : deliveries) {
            String namePDelivery = delivery.getNamePDelivery();

            // Incrémenter le compteur de répétition pour le namePDelivery
            statistiques.put(namePDelivery, statistiques.getOrDefault(namePDelivery, 0) + 1);
        }

        // Créer une liste d'objets PieChart.Data pour les segments du PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
            String namePDelivery = entry.getKey();
            int repetition = entry.getValue();
            double salary = repetition * 2500;
            pieChartData.add(new PieChart.Data(namePDelivery + " - total Deliveyr: "+repetition ,repetition));
        }

        // Ajouter les données au PieChart
        PieChart.setData(pieChartData);
    }

    @FXML
    void pdfBtn(ActionEvent event) {
        TreeItem<String> selectedItem = TreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            generatePDF(selectedItem);
        } else {
            showAlert("Warning", "Please select an item from the TreeView.");
        }
    }

    private void generatePDF(TreeItem<String> selectedItem) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText(selectedItem.getValue());
                contentStream.endText();
            }

            document.save("TreeViewData.pdf");
            showAlert("Success", "PDF generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while generating the PDF.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void filterDeliveries(ActionEvent event) {
        // Récupérer les valeurs entrées par l'utilisateur
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String deliveryPerson = deliveryPersonField.getText();

        try {
            // Récupérer toutes les livraisons
            List<Delivery> deliveries = deliveryService.recuperer();

            // Filtrer les livraisons en fonction des critères
            List<Delivery> filteredDeliveries = filterDeliveries(deliveries, startDate, endDate, deliveryPerson);

            // Mettre à jour les statistiques affichées
            afficherStatistiquesTreeView(filteredDeliveries);
            afficherStatistiquesPieChart(filteredDeliveries);

        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de récupération des données
        }
    }

    private List<Delivery> filterDeliveries(List<Delivery> deliveries, LocalDate startDate, LocalDate endDate, String deliveryPerson) {
        List<Delivery> filteredList = new ArrayList<>();

        // Parcourir la liste des livraisons
        for (Delivery delivery : deliveries) {
            // Vérifier si la livraison correspond aux critères de filtrage
            boolean meetsCriteria = true;

            // Filtrer par date de livraison si la date de début est spécifiée
            if (startDate != null && delivery.getDate().isBefore(startDate)) {
                meetsCriteria = false;
            }

            // Filtrer par date de livraison si la date de fin est spécifiée
            if (endDate != null && delivery.getDate().isAfter(endDate)) {
                meetsCriteria = false;
            }

            // Filtrer par personne de livraison si le champ n'est pas vide
            if (!deliveryPerson.isEmpty() && !delivery.getNamePDelivery().equals(deliveryPerson)) {
                meetsCriteria = false;
            }

            // Ajouter la livraison à la liste filtrée si elle correspond aux critères
            if (meetsCriteria) {
                filteredList.add(delivery);
            }
        }

        return filteredList;
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