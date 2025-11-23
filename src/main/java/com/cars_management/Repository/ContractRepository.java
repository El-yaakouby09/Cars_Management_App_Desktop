package com.cars_management.Repository;

import com.cars_management.Controller.Contracts.Contract;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContractRepository {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public ContractRepository() {
        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "5432";
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "cars_db";
        this.dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
        this.dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "postgres";
        this.dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL driver not found: " + e.getMessage());
        }

        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS contracts (" +
                "id SERIAL PRIMARY KEY, " +
                "client_name VARCHAR(255) NOT NULL, " +
                "client_cni VARCHAR(255) NOT NULL, " +
                "client_phone VARCHAR(20) NOT NULL, " +
                "client_address VARCHAR(255) NOT NULL, " +
                "driver_license_date DATE NOT NULL, " +
                "car_brand VARCHAR(100) NOT NULL, " +
                "car_model VARCHAR(100) NOT NULL, " +
                "car_plate VARCHAR(20) NOT NULL, " +
                "rental_start_date DATE NOT NULL, " +
                "rental_end_date DATE NOT NULL, " +
                "rental_days INTEGER NOT NULL, " +
                "price_per_day NUMERIC(10, 2) NOT NULL, " +
                "total_price NUMERIC(10, 2) NOT NULL" +
                ")";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Contracts table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Failed to create contracts table: " + e.getMessage());
        }
    }

    public void save(Contract contract) {
        String insertSQL = "INSERT INTO contracts " +
                "(client_name, client_cni, client_phone, client_address, driver_license_date, " +
                "car_brand, car_model, car_plate, rental_start_date, rental_end_date, rental_days, " +
                "price_per_day, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, contract.getClientName());
            pstmt.setString(2, contract.getClientCNI());
            pstmt.setString(3, contract.getClientPhone());
            pstmt.setString(4, contract.getClientAddress());
            pstmt.setDate(5, Date.valueOf(contract.getDriverLicenseDate()));
            pstmt.setString(6, contract.getCarBrand());
            pstmt.setString(7, contract.getCarModel());
            pstmt.setString(8, contract.getCarPlate());
            pstmt.setDate(9, Date.valueOf(contract.getRentalStartDate()));
            pstmt.setDate(10, Date.valueOf(contract.getRentalEndDate()));
            pstmt.setInt(11, contract.getRentalDays());
            pstmt.setDouble(12, contract.getPricePerDay());
            pstmt.setDouble(13, contract.getTotalPrice());

            pstmt.executeUpdate();
            System.out.println("Contract saved successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to save contract: " + e.getMessage());
        }
    }

    public void update(Contract contract) {
        String updateSQL = "UPDATE contracts SET " +
                "client_name=?, client_cni=?, client_phone=?, client_address=?, driver_license_date=?, " +
                "car_brand=?, car_model=?, car_plate=?, rental_start_date=?, rental_end_date=?, " +
                "rental_days=?, price_per_day=?, total_price=? " +
                "WHERE id=?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, contract.getClientName());
            pstmt.setString(2, contract.getClientCNI());
            pstmt.setString(3, contract.getClientPhone());
            pstmt.setString(4, contract.getClientAddress());
            pstmt.setDate(5, Date.valueOf(contract.getDriverLicenseDate()));
            pstmt.setString(6, contract.getCarBrand());
            pstmt.setString(7, contract.getCarModel());
            pstmt.setString(8, contract.getCarPlate());
            pstmt.setDate(9, Date.valueOf(contract.getRentalStartDate()));
            pstmt.setDate(10, Date.valueOf(contract.getRentalEndDate()));
            pstmt.setInt(11, contract.getRentalDays());
            pstmt.setDouble(12, contract.getPricePerDay());
            pstmt.setDouble(13, contract.getTotalPrice());
            pstmt.setInt(14, contract.getId());

            pstmt.executeUpdate();
            System.out.println("Contract updated successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to update contract: " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        String deleteSQL = "DELETE FROM contracts WHERE id=?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Contract deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to delete contract: " + e.getMessage());
        }
    }

    public List<Contract> findAll() {
        List<Contract> contracts = new ArrayList<>();
        String selectSQL = "SELECT * FROM contracts ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                Contract contract = new Contract(
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("client_cni"),
                        rs.getString("client_phone"),
                        rs.getString("client_address"),
                        rs.getDate("driver_license_date").toLocalDate(),
                        rs.getString("car_brand"),
                        rs.getString("car_model"),
                        rs.getString("car_plate"),
                        rs.getDate("rental_start_date").toLocalDate(),
                        rs.getDate("rental_end_date").toLocalDate(),
                        rs.getInt("rental_days"),
                        rs.getDouble("price_per_day"),
                        rs.getDouble("total_price")
                );
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("FindAll failed: " + e.getMessage());
        }

        return contracts;
    }
}
