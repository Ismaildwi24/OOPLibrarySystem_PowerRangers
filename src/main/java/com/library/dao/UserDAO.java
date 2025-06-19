package com.library.dao;

import com.library.models.User;
import com.library.utils.DatabaseUtil;

import java.sql.*;

public class UserDAO {

    public static int countByRole(String role) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public static boolean isUsernameExists(String username) {
        String query = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isStudentNumberExists(String studentNumber) {
        String query = "SELECT 1 FROM users WHERE student_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, name, student_number, role, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // gunakan hash kalau perlu
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getStudentNumber());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getAddress());

            stmt.executeUpdate(); // Lempar SQLException jika gagal
        }
    }

    public static User loginUserMahasiswa(String username, String student_number) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND student_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, student_number);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Ambil semua informasi dari hasil query
                int id = rs.getInt("id"); // Ambil ID dari database
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                // Buat dan return user dengan ID yang valid
                return new User(id, username, password, name, student_number, role);
            } else {
                return null; // Login gagal
            }
        }
    }


    public static User loginUserAdmin(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // kalau pakai hashing, cocokan hash-nya

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Buat User dari hasil query
                String name = rs.getString("name");
                String studentNumber = rs.getString("student_number");
//                String password = rs.getString("password");
                String role = rs.getString("role");

                return new User(username, password, name, studentNumber, role);
            } else {
                return null; // Login gagal
            }
        }
    }

}
