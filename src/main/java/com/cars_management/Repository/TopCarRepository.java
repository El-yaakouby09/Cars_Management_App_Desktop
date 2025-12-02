package com.cars_management.Repository;

import com.cars_management.TopCar;
import com.cars_management.Setting.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TopCarRepository implements ITopCarRepository {

    private Connection connection;

    public TopCarRepository() {
        this.connection = Session.getConnection();
    }

    @Override
    public TopCar findTopCar() {

        TopCar top = null;

        try {
            connection = Session.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT marque, model FROM mv_top_cars"
            );

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                top = new TopCar();
                top.setMarque(rs.getString("marque"));
                top.setModel(rs.getString("model"));
            }

        } catch (Exception e) {
            System.out.println("[ERROR] findTopCar: " + e.getMessage());
        }

        return top;
    }

    @Override
    public void refresh() {
        try {
            connection = Session.getConnection();

            PreparedStatement ps = connection.prepareStatement(
                    "REFRESH MATERIALIZED VIEW mv_top_cars"
            );

            ps.execute();
        } catch (Exception e) {
            System.out.println("[ERROR] refresh: " + e.getMessage());
        }
    }
}
