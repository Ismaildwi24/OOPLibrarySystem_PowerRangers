package com.library.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/book_store?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Ganti jika punya password

    // Method umum untuk dapatkan koneksi
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL tidak ditemukan: " + e.getMessage());
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try {
            // Paksa load driver (kadang perlu di Java SE project)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = getConnection();
            System.out.println("✅ Koneksi berhasil ke database: " + URL);
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Gagal koneksi ke database.");
            System.err.println("Pesan error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC MySQL tidak ditemukan.");
            System.err.println("Pesan error: " + e.getMessage());
        }
    }

    // Untuk test mandiri (opsional)
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            System.out.println(DriverManager.getDrivers());

            System.out.println("✅ Koneksi ke database berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
