package tn.scholarcircle.appfx.services;

import tn.scholarcircle.appfx.models.PersonDelivery;
import tn.scholarcircle.appfx.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDeliveryService implements IService<PersonDelivery>{
    private Connection connection;
    public PersonDeliveryService(){
        connection= MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouter(PersonDelivery personDelivery) throws SQLException {
        String req ="INSERT INTO persondelivery (FirstName,LastName,Address,Email,Phone,Sector,Transportation) Values ('"+personDelivery.getFirstName() +"','"+personDelivery.getLastName() +"','"+personDelivery.getAddress() +"','"+personDelivery.getEmail() +"','"+personDelivery.getPhone() +"','"+personDelivery.getSector() +"','"+personDelivery.getTransportation() +"')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
    }

    @Override
    public void modifier(PersonDelivery personDelivery) throws SQLException {
        String req ="UPDATE persondelivery SET FirstName =?,LastName =?,Address =?,Email =?,Phone =?,Sector =?,Transportation =? WHERE Id = ?";
        PreparedStatement ps =connection.prepareStatement(req);
        ps.setString(1, personDelivery.getFirstName());
        ps.setString(2,personDelivery.getLastName());
        ps.setString(3,personDelivery.getAddress());
        ps.setString(4,personDelivery.getEmail());
        ps.setInt(5,personDelivery.getPhone());
        ps.setString(6,personDelivery.getSector());
        ps.setString(7,personDelivery.getTransportation());
        ps.setInt(8,personDelivery.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req ="DELETE FROM persondelivery WHERE Id =?";
        PreparedStatement ps =connection.prepareStatement(req);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<PersonDelivery> recuperer() throws SQLException {
        List<PersonDelivery> personDeliveries =new ArrayList<>();
        String req ="SELECT * FROM persondelivery";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            PersonDelivery personDelivery = new PersonDelivery();
            personDelivery.setId(rs.getInt("Id"));
            personDelivery.setFirstName(rs.getString("FirstName"));
            personDelivery.setLastName(rs.getString("LastName"));
            personDelivery.setAddress(rs.getString("Address"));
            personDelivery.setEmail(rs.getString("Email"));
            personDelivery.setPhone(rs.getInt("Phone"));
            personDelivery.setSector(rs.getString("Sector"));
            personDelivery.setTransportation(rs.getString("Transportation"));
            personDeliveries.add(personDelivery);
        }

        return personDeliveries;
    }
}

