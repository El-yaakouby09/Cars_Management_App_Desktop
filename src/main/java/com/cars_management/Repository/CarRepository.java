package com.cars_management.Repository;

import com.cars_management.Controller.Cars.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository implements ICarRepository {

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
            System.err.println("DB Init Error: " + e.getMessage());
        }
    }

    @Override
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
            System.err.println("Save Error: " + e.getMessage());
            return false;
        }
    }

    @Override
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
            System.err.println("Update Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Delete Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Car> findAll() {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT id, matricule, marque_model, annee, prix FROM cars";

        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("id"),
                        "",
                        "",
                        rs.getInt("annee"),
                        rs.getDouble("prix")
                );

                car.setMatricule(rs.getString("matricule"));
                car.setMarqueModel(rs.getString("marque_model"));
                list.add(car);
            }
        } catch (SQLException e) {
            System.err.println("FindAll Error: " + e.getMessage());
        }

        return list;
    }
    public int countCars() {
        String sql = "SELECT COUNT(*) FROM cars";
        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}