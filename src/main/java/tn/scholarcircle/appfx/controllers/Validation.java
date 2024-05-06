package tn.scholarcircle.appfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.services.DeliveryService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validation {

    private DeliveryService deliveryService;

    public Validation() {
        deliveryService = new DeliveryService();
    }
    @FXML
    private TreeView<String> treeView;

    @FXML
    void initialize() {
        //tree
        try {
            // Récupérer la liste des livraisons
            List<Delivery> deliveries = deliveryService.recuperer();

            // Construire la structure d'arbre pour les livraisons par jour
            Map<LocalDate, TreeItem<String>> dateNodes = new HashMap<>();
            for (Delivery delivery : deliveries) {
                LocalDate date = delivery.getDate();
                if (!dateNodes.containsKey(date)) {
                    TreeItem<String> dayNode = new TreeItem<>(date.toString());
                    dateNodes.put(date, dayNode);
                }
                TreeItem<String> deliveryNode = new TreeItem<>("ID: " + delivery.getId() +
                        ", Status: " + delivery.getStatus() +
                        ", NamePDelivery: " + delivery.getNamePDelivery());
                dateNodes.get(date).getChildren().add(deliveryNode);
            }

            // Créer un nœud racine pour l'arborescence
            TreeItem<String> root = new TreeItem<>("Livraisons");
            root.getChildren().addAll(dateNodes.values());

            // Définir le nœud racine dans le TreeView
            treeView.setRoot(root);

        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de manière appropriée
        }
    }
    @FXML
    void livreBTN(ActionEvent event) {
        // Récupérer les éléments sélectionnés dans le TreeView
        List<TreeItem<String>> selectedItems = treeView.getSelectionModel().getSelectedItems();

        // Récupérer la liste des livraisons depuis le service de livraison
        List<Delivery> deliveries;
        try {
            deliveries = deliveryService.recuperer();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de manière appropriée
            return; // Sortir de la méthode si une erreur se produit
        }

        // Mettre à jour le statut des livraisons sélectionnées
        for (TreeItem<String> selectedItem : selectedItems) {
            // Obtenir la valeur (texte) de l'élément sélectionné
            String deliveryInfo = selectedItem.getValue();

            // Extraire l'ID de livraison à partir du texte
            String[] parts = deliveryInfo.split(", ");
            int id = Integer.parseInt(parts[0].substring(4)); // Pour obtenir uniquement l'ID

            // Rechercher la livraison correspondante dans la liste de livraisons
            for (Delivery delivery : deliveries) {
                if (delivery.getId() == id) {
                    // Mettre à jour le statut de la livraison dans la base de données
                    try {
                        delivery.setStatus("en Livrée");
                        deliveryService.modifier(delivery);
                        break; // Sortir de la boucle une fois que la livraison est trouvée et mise à jour
                    } catch (SQLException e) {
                        e.printStackTrace(); // Gérer les erreurs de manière appropriée
                    }
                }
            }
        }

        // Mettre à jour le TreeView après la modification
        updateTreeView();
    }

    private void updateTreeView() {
        initialize(); // Reconstruire la structure de l'arbre avec les nouvelles données
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





}
