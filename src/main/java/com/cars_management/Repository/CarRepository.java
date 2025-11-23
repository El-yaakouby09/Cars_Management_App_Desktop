package com.cars_management.Repository;

import com.cars_management.Controller.Cars.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private final String url;
    private final String user;
    private final String password;

    public CarRepository() {
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
        String sql = "CREATE TABLE IF NOT EXISTS cars (" +
                "id INTEGER PRIMARY KEY, " +
                "matricule VARCHAR(100), " +
                "marque_model VARCHAR(255), " +
                "annee INTEGER, " +
                "prix DOUBLE PRECISION" +
                ");";
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize DB: " + e.getMessage());
        }
    }

    public boolean save(Car car) {
        String sql = "INSERT INTO cars (id, matricule, marque_model, annee, prix) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, car.getId());
            ps.setString(2, car.getMatricule());
            ps.setString(3, car.getMarqueModel());
            ps.setObject(4, car.getAnnee());
            ps.setDouble(5, car.getPrix());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Car car) {
        String sql = "UPDATE cars SET matricule = ?, marque_model = ?, annee = ?, prix = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, car.getMatricule());
            ps.setString(2, car.getMarqueModel());
            ps.setObject(3, car.getAnnee());
            ps.setDouble(4, car.getPrix());
            ps.setInt(5, car.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    public List<Car> findAll() {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT id, matricule, marque_model, annee, prix FROM cars";
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String matricule = rs.getString("matricule");
                String marqueModel = rs.getString("marque_model");
                Integer annee = rs.getObject("annee") == null ? null : rs.getInt("annee");
                double prix = rs.getDouble("prix");
                Car car = new Car(id, "", "", 0, 0.0);
                car.setId(id);
                car.setMatricule(matricule);
                car.setMarqueModel(marqueModel);
                car.setAnnee(annee);
                car.setPrix(prix);
                list.add(car);
            }
        } catch (SQLException e) {
            System.err.println("FindAll failed: " + e.getMessage());
        }
        return list;
    }
}
