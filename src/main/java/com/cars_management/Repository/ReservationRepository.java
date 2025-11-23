package com.cars_management.Repository;

import com.cars_management.Controller.Reservations.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private final String url;
    private final String user;
    private final String password;

    public ReservationRepository() {
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "5432");
        String db = System.getenv().getOrDefault("DB_NAME", "cars_db");
        this.user = System.getenv().getOrDefault("DB_USER", "postgres");
        this.password = System.getenv().getOrDefault("DB_PASSWORD", "postgres");
        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

        init();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void init() {
        String sql = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id SERIAL PRIMARY KEY, " +
                "client VARCHAR(255), " +
                "car VARCHAR(255), " +
                "days INTEGER, " +
                "total DOUBLE PRECISION" +
                ");";
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize reservations table: " + e.getMessage());
        }
    }

    public boolean save(Reservation r) {
        String sql = "INSERT INTO reservations (client, car, days, total) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getClient());
            ps.setString(2, r.getCar());
            ps.setInt(3, r.getDays());
            ps.setDouble(4, r.getTotal());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Reservation save failed: " + e.getMessage());
            return false;
        }
    }

    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT client, car, days, total FROM reservations";
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                String client = rs.getString("client");
                String car = rs.getString("car");
                int days = rs.getInt("days");
                double total = rs.getDouble("total");
                list.add(new Reservation(client, car, days, total));
            }
        } catch (SQLException e) {
            System.err.println("Reservation findAll failed: " + e.getMessage());
        }
        return list;
    }
}
