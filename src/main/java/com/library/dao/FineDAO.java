package com.library.dao;

import com.library.utils.DatabaseUtil;

import java.sql.*;

public class FineDAO {
    public static void createFine(int borrowingId, double amount) throws SQLException {
        String sql = "INSERT INTO fines (borrowing_id, amount, paid) VALUES (?, ?, 0)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
        }
    }

    public static double getTotalFines() throws SQLException {
        String sql = "SELECT SUM(amount) FROM fines";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }


}
