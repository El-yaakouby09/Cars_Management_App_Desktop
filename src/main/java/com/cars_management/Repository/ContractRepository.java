package com.cars_management.Repository;

import com.cars_management.Controller.Contracts.Contract;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractRepository implements IContractRepository {

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
        String sql =
                "CREATE TABLE IF NOT EXISTS contracts (" +
                        "id SERIAL PRIMARY KEY, " +
                        "client_name VARCHAR(255), " +
                        "client_cni VARCHAR(255), " +
                        "client_phone VARCHAR(20), " +
                        "client_address VARCHAR(255), " +
                        "driver_license_date DATE, " +
                        "car_brand VARCHAR(100), " +
                        "car_model VARCHAR(100), " +
                        "car_plate VARCHAR(20), " +
                        "rental_start_date DATE, " +
                        "rental_end_date DATE, " +
                        "rental_days INTEGER, " +
                        "price_per_day DOUBLE PRECISION, " +
                        "total_price DOUBLE PRECISION" +
                        ")";

        try (Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement st = c.createStatement()) {

            st.execute(sql);

        } catch (SQLException e) {
            System.err.println("Table creation failed: " + e.getMessage());
        }
    }

    @Override
    public void save(Contract contract) {
        String sql =
                "INSERT INTO contracts (client_name, client_cni, client_phone, client_address, driver_license_date," +
                        "car_brand, car_model, car_plate, rental_start_date, rental_end_date, rental_days, price_per_day, total_price) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            fillStatement(ps, contract, false);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    @Override
    public void update(Contract contract) {
        String sql =
                "UPDATE contracts SET client_name=?, client_cni=?, client_phone=?, client_address=?, driver_license_date=?," +
                        "car_brand=?, car_model=?, car_plate=?, rental_start_date=?, rental_end_date=?, rental_days=?, price_per_day=?, total_price=? " +
                        "WHERE id=?";

        try (Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            fillStatement(ps, contract, true);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }

    private void fillStatement(PreparedStatement ps, Contract contract, boolean includeId) throws SQLException {
        ps.setString(1, contract.getClientName());
        ps.setString(2, contract.getClientCNI());
        ps.setString(3, contract.getClientPhone());
        ps.setString(4, contract.getClientAddress());
        ps.setDate(5, Date.valueOf(contract.getDriverLicenseDate()));
        ps.setString(6, contract.getCarBrand());
        ps.setString(7, contract.getCarModel());
        ps.setString(8, contract.getCarPlate());
        ps.setDate(9, Date.valueOf(contract.getRentalStartDate()));
        ps.setDate(10, Date.valueOf(contract.getRentalEndDate()));
        ps.setInt(11, contract.getRentalDays());
        ps.setDouble(12, contract.getPricePerDay());
        ps.setDouble(13, contract.getTotalPrice());

        if (includeId) {
            ps.setInt(14, contract.getId());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM contracts WHERE id=?";

        try (Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }
    }

    @Override
    public List<Contract> findAll() {
        List<Contract> list = new ArrayList<>();
        String sql = "SELECT * FROM contracts ORDER BY id DESC";

        try (Connection c = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

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
                list.add(contract);
            }

        } catch (SQLException e) {
            System.err.println("FindAll failed: " + e.getMessage());
        }

        return list;
    }
}
