package tn.scholarcircle.appfx.controllers.Delivery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import tn.scholarcircle.appfx.models.Delivery;
import javafx.scene.control.TableView;
import tn.scholarcircle.appfx.services.DeliveryService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Pagination;

public class ShowDelivery {


    @FXML
    private TextField FilterField;
    @FXML
    private TableColumn<Delivery, String> AddressCol;

    @FXML
    private TableColumn<Delivery, String> CustomerCol;

    @FXML
    private TableColumn<Delivery, LocalDate> DateCol;

    @FXML
    private TableColumn<Delivery, Double> FeesCol;

    @FXML
    private TableColumn<Delivery, String> NamePDeliveryCol;

    @FXML
    private TableColumn<Delivery, Integer> QuantityCol;

    @FXML
    private TableColumn<Delivery, String> StatusCol;

    @FXML
    private TableView<Delivery> TableView;

    @FXML
    private Pagination Pagination;


    private final int pageSize = 5; // Nombre d'éléments par page
    private DeliveryService deliveryService = new DeliveryService();


    @FXML
    void initialize()  {
        DeliveryService deliveryService = new DeliveryService();
        try {
            List<Delivery> Deliveries = deliveryService.recuperer();
            ObservableList<Delivery> observableList = FXCollections.observableArrayList(Deliveries);
            TableView.setItems(observableList);
            DateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
            AddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
            CustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
            StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
            QuantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            FeesCol.setCellValueFactory(new PropertyValueFactory<>("Fees"));
            NamePDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("NamePDelivery"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la récupération des Delivery : " + e.getMessage());
            alert.showAndWait();
        }
        try {
            List<Delivery> allDeliveries = deliveryService.recuperer();
            ObservableList<Delivery> observableList = FXCollections.observableArrayList(allDeliveries);
            TableView.setItems(observableList);
            setupPagination(allDeliveries);
            setupTableView();
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de la récupération des Delivery", e.getMessage());
        }
        // Ajouter un écouteur de changement de texte au champ de recherche
        FilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(newValue.toLowerCase());
        });

        // Pour restaurer les données d'origine lorsque le champ de recherche est vidé
        FilterField.setOnKeyReleased(event -> {
            if (FilterField.getText().isEmpty()) {
                handleSearch(""); // Recherche vide pour restaurer les données d'origine
            }
        });
    }

    //pagination
    private void setupPagination(List<Delivery> allDeliveries) {
        int totalDeliveries = allDeliveries.size();
        int totalPages = (int) Math.ceil((double) totalDeliveries / pageSize);

        Pagination.setPageCount(totalPages);
        Pagination.setPageFactory(pageIndex -> createPage(pageIndex, allDeliveries));
    }

    private Node createPage(int pageIndex, List<Delivery> allDeliveries) {
        int fromIndex = pageIndex * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allDeliveries.size());
        List<Delivery> subList = allDeliveries.subList(fromIndex, toIndex);

        TableView.setItems(FXCollections.observableArrayList(subList));
        return TableView;
    }

    private void setupTableView() {
        DateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        AddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        CustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        FeesCol.setCellValueFactory(new PropertyValueFactory<>("Fees"));
        NamePDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("NamePDelivery"));
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(title + ": " + content);
        alert.showAndWait();
    }


    @FXML
    void PDFDelivery(ActionEvent event) {
        Delivery deliverySelectionne = TableView.getSelectionModel().getSelectedItem();

        if (deliverySelectionne != null) {
            // Générer le PDF pour le Person Delivery sélectionné
            generateDeliveryPDF(deliverySelectionne);

            // Afficher une boîte de dialogue pour informer l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF généré");
            alert.setContentText("Le document PDF pour le livreur sélectionné a été généré avec succès !");
            alert.showAndWait();
        } else {
            // Afficher un message d'erreur si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez sélectionner un PersonDelivery dans le tableau.");
            alert.showAndWait();
        }

    }
    private void generateDeliveryPDF(Delivery delivery) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);

            // Écrire les informations du Livreur dans le PDF
            writeText(contentStream, "Date: " + delivery.getDate());
            writeText(contentStream, "Address: " + delivery.getAddress());
            writeText(contentStream, "Customer: " + delivery.getCustomer());
            writeText(contentStream, "Status: " + delivery.getStatus());
            writeText(contentStream, "Quantity: " + delivery.getQuantity());
            writeText(contentStream, "Fees: " + delivery.getFees());
            writeText(contentStream, "PersonDelivery: " + delivery.getNamePDelivery());

            contentStream.endText();
            contentStream.close();

            // Sauvegarde du document PDF
            File file = new File("Delivery" + delivery.getId() + ".pdf");
            document.save(file);
            document.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors de la génération du PDF : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void writeText(PDPageContentStream contentStream, String text) throws IOException {
        contentStream.showText(text);
        contentStream.newLineAtOffset(0, -20); // Décalage pour la prochaine ligne
    }

    // Méthode pour gérer la recherche et la restauration des données d'origine
    private void handleSearch(String searchTerm) {
        try {
            List<Delivery> allDeliveries = deliveryService.recuperer();
            ObservableList<Delivery> observableList = FXCollections.observableArrayList(allDeliveries);

            // Appliquer le filtre de recherche si le terme de recherche n'est pas vide
            if (!searchTerm.isEmpty()) {
                // Créer un FilteredList avec la liste d'origine
                FilteredList<Delivery> filteredList = new FilteredList<>(observableList);

                // Appliquer le prédicat de filtre
                filteredList.setPredicate(delivery ->
                        delivery.getAddress().toLowerCase().contains(searchTerm) ||
                                delivery.getCustomer().toLowerCase().contains(searchTerm) ||
                                delivery.getDate().toString().contains(searchTerm) ||
                                delivery.getStatus().toLowerCase().contains(searchTerm));

                // Mettre à jour le TableView avec le FilteredList
                TableView.setItems(filteredList);
            } else {
                // Si le champ de recherche est vide, restaurer les données d'origine
                TableView.setItems(observableList);
                setupPagination(allDeliveries);
                setupTableView();
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de l'actualisation des Deliveries", e.getMessage());
        }
    }




    @FXML
    void StatDelivery(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/scholarcircle/appfx/Delivery/HomeStat.fxml"));
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

