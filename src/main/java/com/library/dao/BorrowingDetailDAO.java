package com.library.dao;

import com.library.models.BorrowingDetail;
import com.library.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class BorrowingDetailDAO {
    public static void createBorrowingDetail(BorrowingDetail detail) throws SQLException {
        String sql = "INSERT INTO borrowing_details (borrowing_id, book_item_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getBorrowingId());
            stmt.setInt(2, detail.getBookItemId());
            stmt.executeUpdate();
        }
    }
}
