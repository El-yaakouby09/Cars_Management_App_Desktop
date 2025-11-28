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
        String sql = """
            CREATE TABLE IF NOT EXISTS reservations (
                id SERIAL PRIMARY KEY,
                client VARCHAR(255),
                car VARCHAR(255),
                days INTEGER,
                total DOUBLE PRECISION,
                start_date DATE,
                end_date DATE,
                price_per_day DOUBLE PRECISION
            );
        """;

        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("Init Reservation Error: " + e.getMessage());
        }
    }

    @Override
    public boolean save(Reservation r) {
        String sql = """
            INSERT INTO reservations (client, car, days, total, start_date, end_date, price_per_day)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getClient());
            ps.setString(2, r.getCar());
            ps.setInt(3, r.getDays());
            ps.setDouble(4, r.getTotal());
            ps.setDate(5, Date.valueOf(r.getStartDate()));
            ps.setDate(6, Date.valueOf(r.getEndDate()));
            ps.setDouble(7, r.getPricePerDay());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Save Reservation Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT * FROM reservations ORDER BY id DESC";

        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Reservation r = new Reservation(
                        rs.getInt("id"),
                        rs.getString("client"),
                        rs.getString("car"),
                        rs.getInt("days"),
                        rs.getDouble("total"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getDouble("price_per_day")
                );
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("FindAll Reservation Error: " + e.getMessage());
        }

        return list;
    }

    @Override
    public boolean update(int id, Reservation r) {
        String sql = """
            UPDATE reservations 
            SET client=?, car=?, days=?, total=?, start_date=?, end_date=?, price_per_day=?
            WHERE id=?
        """;

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getClient());
            ps.setString(2, r.getCar());
            ps.setInt(3, r.getDays());
            ps.setDouble(4, r.getTotal());
            ps.setDate(5, Date.valueOf(r.getStartDate()));
            ps.setDate(6, Date.valueOf(r.getEndDate()));
            ps.setDouble(7, r.getPricePerDay());
            ps.setInt(8, id);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Update Reservation Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Delete Reservation Error: " + e.getMessage());
            return false;
        }
    }
}
