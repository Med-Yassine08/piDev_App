package tn.scholarcircle.appfx.services;

import tn.scholarcircle.appfx.models.Delivery;
import tn.scholarcircle.appfx.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryService implements IService<Delivery>{

    private Connection connection;
    public DeliveryService(){
        connection= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Delivery delivery) throws SQLException {
        String req ="INSERT INTO delivery (Date,Address,Customer,Status,Quantity,fees,NamePDelivery) Values ('"+delivery.getDate() +"','"+delivery.getAddress() +"','"+delivery.getCustomer() +"','"+delivery.getStatus() +"','"+delivery.getQuantity() +"','"+delivery.getFees() +"','"+delivery.getNamePDelivery() +"')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
    }

    @Override
    public void modifier(Delivery delivery) throws SQLException {
        String req ="UPDATE delivery SET Date = ?,Address = ?,Customer = ?,Status = ?,Quantity = ?,fees = ?,NamePDelivery = ? WHERE Id = ?";
        PreparedStatement ps =connection.prepareStatement(req);
        ps.setDate(1, Date.valueOf(delivery.getDate()));
        ps.setString(2,delivery.getAddress());
        ps.setString(3,delivery.getCustomer());
        ps.setString(4,delivery.getStatus());
        ps.setInt(5,delivery.getQuantity());
        ps.setDouble(6,delivery.getFees());
        ps.setString(7,delivery.getNamePDelivery());
        ps.setInt(8,delivery.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req ="DELETE FROM delivery WHERE Id =?";
        PreparedStatement ps =connection.prepareStatement(req);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<Delivery> recuperer() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String req ="SELECT * FROM delivery";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()){
            Delivery delivery = new Delivery();
            delivery.setId(rs.getInt("Id"));
            String dateString = rs.getString("Date");
            if (dateString != null && !dateString.isEmpty()) {
                delivery.setDate(LocalDate.parse(dateString));
            } else {
                continue;
            }
            delivery.setAddress(rs.getString("Address"));
            delivery.setCustomer(rs.getString("Customer"));
            delivery.setStatus(rs.getString("Status"));
            delivery.setQuantity(rs.getInt("Quantity"));
            delivery.setFees(rs.getDouble("fees"));
            delivery.setNamePDelivery(rs.getString("NamePDelivery"));
            deliveries.add(delivery);
        }
        return deliveries;
    }
    // Méthode pour récupérer tous les noms complets des livreurs
    public List<String> getAllDeliveryNames() throws SQLException {
        List<String> deliveryNames = new ArrayList<>();
        String req = "SELECT CONCAT(FirstName, ' ', LastName) AS FullName FROM PersonDelivery";
        PreparedStatement ps = connection.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String fullName = rs.getString("FullName");
            deliveryNames.add(fullName);
        }
        return deliveryNames;
    }
}
