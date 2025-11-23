package com.cars_management.Repository;

import com.cars_management.Controller.Clients.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private final String url;
    private final String user;
    private final String password;

    public ClientRepository() {
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
        String sql = "CREATE TABLE IF NOT EXISTS clients (" +
                "id INTEGER PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "phone VARCHAR(50), " +
                "email VARCHAR(255)" +
                ");";
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize clients table: " + e.getMessage());
        }
    }

    public boolean save(Client client) {
        String sql = "INSERT INTO clients (id, name, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, client.getId());
            ps.setString(2, client.getName());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Client save failed: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Client client) {
        String sql = "UPDATE clients SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getPhone());
            ps.setString(3, client.getEmail());
            ps.setObject(4, client.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Client update failed: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Client delete failed: " + e.getMessage());
            return false;
        }
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT id, name, phone, email FROM clients";
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Integer id = rs.getObject("id") == null ? null : rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                Client client = new Client(id, name, phone, email);
                list.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Client findAll failed: " + e.getMessage());
        }
        return list;
    }
}
