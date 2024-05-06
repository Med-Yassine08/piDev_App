package tn.scholarcircle.appfx.test;

import tn.scholarcircle.appfx.services.DeliveryService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DeliveryService ls = new DeliveryService();

        try {
            System.out.println(ls.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


    }
}
