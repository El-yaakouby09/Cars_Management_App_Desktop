package com.cars_management.Setting;

import java.sql.Connection;
import java.sql.DriverManager;

public class Session {

    private static Connection connection;
    private static String currentUsername;

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                String url = "jdbc:postgresql://localhost:5432/cars_db";
                String user = "postgres";      // <<< EXACTEMENT TON USER DOCKER
                String password = "postgres";  // <<< EXACTEMENT TON PASSWORD DOCKER

                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(url, user, password);

                System.out.println("[DEBUG] Connected to database successfully.");
            }

        } catch (Exception e) {

            System.out.println("[ERROR] Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }
}
