package com.cars_management.Repository;

import com.cars_management.Controller.Auth.User;

import java.sql.*;

public class UserRepository {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public UserRepository() {
        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "5432";
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "cars_db";
        this.dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
        this.dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "postgres";
        this.dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Postgres driver not found: " + e.getMessage());
        }

        createTableIfNotExists();
        seedDefaultUserIfNeeded();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL" +
                ")";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create users table: " + e.getMessage());
        }
    }

    private void seedDefaultUserIfNeeded() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) == 0) {
                // create a default admin user (username: admin, password: admin)
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                    ps.setString(1, "admin");
                    ps.setString(2, "admin");
                    ps.executeUpdate();
                    System.out.println("Default admin user created (admin/admin). Please change in production.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to seed default user: " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to query user: " + e.getMessage());
        }
        return null;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save user: " + e.getMessage());
        }
    }
}
