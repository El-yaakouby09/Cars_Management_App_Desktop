package com.cars_management.Repository;

import com.cars_management.Controller.Reservations.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements IReservationRepository {

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
            System.err.println("Failed to initialize table: " + e.getMessage());
        }
    }

    @Override
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

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT id, client, car, days, total FROM reservations";

        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getString("client"),
                        rs.getString("car"),
                        rs.getInt("days"),
                        rs.getDouble("total")
                );
                r.setId(rs.getInt("id")); // IMPORTANT : stocker l'ID
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Reservation findAll failed: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(int id, Reservation r) {
        String sql = "UPDATE reservations SET client=?, car=?, days=?, total=? WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getClient());
            ps.setString(2, r.getCar());
            ps.setInt(3, r.getDays());
            ps.setDouble(4, r.getTotal());
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Reservation update failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Reservation delete failed: " + e.getMessage());
            return false;
        }
    }
}
