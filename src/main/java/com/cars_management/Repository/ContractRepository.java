package com.cars_management.Repository;

import com.cars_management.Controller.Contracts.Contract;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractRepository implements IContractRepository {

    private final String url;
    private final String user;
    private final String password;

    public ContractRepository() {

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
                CREATE TABLE IF NOT EXISTS contracts (
                    id SERIAL PRIMARY KEY,
                    client_name VARCHAR(255),
                    client_cni VARCHAR(100),
                    client_phone VARCHAR(100),
                    client_address VARCHAR(255),
                    driver_license_date DATE,
                    car_brand VARCHAR(255),
                    car_model VARCHAR(255),
                    car_plate VARCHAR(100),
                    rental_start_date DATE,
                    rental_end_date DATE,
                    rental_days INTEGER,
                    price_per_day DOUBLE PRECISION,
                    total_price DOUBLE PRECISION
                );
                """;

        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("❌ Error creating contracts table: " + e.getMessage());
        }
    }

    @Override
    public void save(Contract c) {

        String sql = """
                INSERT INTO contracts (
                    client_name, client_cni, client_phone, client_address,
                    driver_license_date, car_brand, car_model, car_plate,
                    rental_start_date, rental_end_date, rental_days,
                    price_per_day, total_price
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getClientName());
            ps.setString(2, c.getClientCNI());
            ps.setString(3, c.getClientPhone());
            ps.setString(4, c.getClientAddress());
            ps.setDate(5, c.getDriverLicenseDate() != null ? Date.valueOf(c.getDriverLicenseDate()) : null);
            ps.setString(6, c.getCarBrand());
            ps.setString(7, c.getCarModel());
            ps.setString(8, c.getCarPlate());
            ps.setDate(9, Date.valueOf(c.getRentalStartDate()));
            ps.setDate(10, Date.valueOf(c.getRentalEndDate()));
            ps.setInt(11, c.getRentalDays());
            ps.setDouble(12, c.getPricePerDay());
            ps.setDouble(13, c.getTotalPrice());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error inserting contract: " + e.getMessage());
        }
    }

    @Override
    public void update(Contract c) {

        String sql = """
                UPDATE contracts SET
                    client_name=?, client_cni=?, client_phone=?, client_address=?,
                    driver_license_date=?, car_brand=?, car_model=?, car_plate=?,
                    rental_start_date=?, rental_end_date=?, rental_days=?,
                    price_per_day=?, total_price=? 
                WHERE id=?
                """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getClientName());
            ps.setString(2, c.getClientCNI());
            ps.setString(3, c.getClientPhone());
            ps.setString(4, c.getClientAddress());
            ps.setDate(5, c.getDriverLicenseDate() != null ? Date.valueOf(c.getDriverLicenseDate()) : null);
            ps.setString(6, c.getCarBrand());
            ps.setString(7, c.getCarModel());
            ps.setString(8, c.getCarPlate());
            ps.setDate(9, Date.valueOf(c.getRentalStartDate()));
            ps.setDate(10, Date.valueOf(c.getRentalEndDate()));
            ps.setInt(11, c.getRentalDays());
            ps.setDouble(12, c.getPricePerDay());
            ps.setDouble(13, c.getTotalPrice());
            ps.setInt(14, c.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error updating contract: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM contracts WHERE id=?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error deleting contract: " + e.getMessage());
        }
    }

    @Override
    public List<Contract> findAll() {

        List<Contract> list = new ArrayList<>();

        String sql = "SELECT * FROM contracts ORDER BY id DESC";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Contract c = new Contract(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("client_cni"),
                        rs.getString("client_phone"),
                        rs.getString("client_address"),
                        rs.getDate("driver_license_date") != null ? rs.getDate("driver_license_date").toLocalDate() : null,
                        rs.getString("car_brand"),
                        rs.getString("car_model"),
                        rs.getString("car_plate"),
                        rs.getDate("rental_start_date").toLocalDate(),
                        rs.getDate("rental_end_date").toLocalDate(),
                        rs.getInt("rental_days"),
                        rs.getDouble("price_per_day"),
                        rs.getDouble("total_price")
                );

                list.add(c);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error reading contracts: " + e.getMessage());
        }

        return list;
    }
    public int countContracts() {
        String sql = "SELECT COUNT(*) FROM contracts";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Count Contracts Error: " + e.getMessage());
        }
        return 0;
    }

    public double sumRevenues() {
        String sql = "SELECT SUM(total_price) FROM contracts";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Sum Revenues Error: " + e.getMessage());
        }
        return 0.0;
    }


}
