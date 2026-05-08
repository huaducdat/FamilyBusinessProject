package com.huaducdat.storemanager.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {

        try {

            String host = "localhost";

            String port = "3306";

            String dbName = "store_manager_db";

            String username = "root";

            String password = "123456";

            // =========================
            // CONNECT MYSQL
            // =========================

            String url =
                    "jdbc:mysql://" +
                            host +
                            ":" +
                            port +
                            "/?serverTimezone=UTC";

            Connection conn =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            Statement stmt = conn.createStatement();

            // =========================
            // CREATE DATABASE
            // =========================

            stmt.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + dbName
            );

            System.out.println(
                    "🔥 DATABASE READY"
            );

            stmt.close();

            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}