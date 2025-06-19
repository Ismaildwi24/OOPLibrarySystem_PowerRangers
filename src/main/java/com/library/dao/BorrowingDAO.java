package com.library.dao;

import com.library.models.*;
import com.library.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import java.sql.*;
import java.util.*;

public class BorrowingDAO {
    public static int countActiveBorrows() throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrowings WHERE status = 'borrowed'";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public static Borrowing createBorrowing(Borrowing borrowing) throws SQLException {
        String sql = "INSERT INTO borrowings (users_id, borrow_date, return_due_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, borrowing.getUserId());
            stmt.setDate(2, Date.valueOf(borrowing.getBorrowDate()));
            stmt.setDate(3, Date.valueOf(borrowing.getReturnDueDate()));
            stmt.setString(4, borrowing.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating borrowing failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrowing.setId(generatedKeys.getInt(1)); // set ID yang dihasilkan
                } else {
                    throw new SQLException("Creating borrowing failed, no ID obtained.");
                }
            }
        }
        return borrowing;
    }

    public static void addBorrowingDetail(int borrowingId, int bookItemId) throws SQLException {
        String sql = "INSERT INTO borrowing_details (borrowing_id, book_item_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingId);
            stmt.setInt(2, bookItemId);
            stmt.executeUpdate();
        }
    }

    public static void returnBorrowing(int borrowingId) throws SQLException {
        String sql = "UPDATE borrowings SET return_date = ?, status = 'returned' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, borrowingId);
            stmt.executeUpdate();
        }
    }

    public static List<BorrowingDetail> getBorrowingDetails(int borrowingId) throws SQLException {
        List<BorrowingDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_details WHERE borrowing_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowingDetail detail = new BorrowingDetail();
                detail.setId(rs.getInt("id"));
                detail.setBorrowingId(rs.getInt("borrowing_id"));
                detail.setBookItemId(rs.getInt("book_item_id"));
                details.add(detail);
            }
        }
        return details;
    }

    public static List<Borrowing> getBorrowingsByUserId(int userId) throws SQLException {
        List<Borrowing> list = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE users_id = ? ORDER BY borrow_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Borrowing b = new Borrowing();
                b.setId(rs.getInt("id"));
                b.setUserId(rs.getInt("users_id"));
                b.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                b.setReturnDueDate(rs.getDate("return_due_date").toLocalDate());
                Date returnDate = rs.getDate("return_date");
                b.setReturnDate(returnDate != null ? returnDate.toLocalDate() : null);
                b.setStatus(rs.getString("status"));
                list.add(b);
            }
        }

        return list;
    }

    public static Map<String, Integer> getBorrowStatsByUsername(String username) throws SQLException {
        String sql = "SELECT " +
                "SUM(CASE WHEN b.status = 'borrowed' THEN 1 ELSE 0 END) AS buku_dipinjam, " +
                "COUNT(*) AS total_peminjaman, " +
                "SUM(CASE WHEN b.status = 'borrowed' AND b.return_due_date > CURRENT_DATE AND b.return_due_date < DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY) THEN 1 ELSE 0 END) AS hampir_jatuh_tempo, " +
                "SUM(CASE WHEN b.status = 'returned' THEN 1 ELSE 0 END) AS telah_dikembalikan " +
                "FROM borrowings b " +
                "JOIN users u ON b.users_id = u.id " +
                "WHERE u.username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            Map<String, Integer> result = new HashMap<>();
            if (rs.next()) {
                result.put("bukuDipinjam", rs.getInt("buku_dipinjam"));
                result.put("totalPeminjaman", rs.getInt("total_peminjaman"));
                result.put("hampirJatuhTempo", rs.getInt("hampir_jatuh_tempo"));
                result.put("telahDikembalikan", rs.getInt("telah_dikembalikan"));
            }
            return result;
        }
    }

    public static List<Borrow> getBorrowedBooksByUsername(String username) throws SQLException {
        List<Borrow> borrows = new ArrayList<>();
        String sql = """
                    SELECT b.id, bo.title AS book_title, b.return_due_date, b.status
                    FROM borrowings b
                    JOIN users u ON b.users_id = u.id
                    JOIN borrowing_details bd ON b.id = bd.borrowing_id
                    JOIN book_items bi ON bd.book_item_id = bi.id
                    JOIN books bo ON bi.book_id = bo.id
                    WHERE u.username = ? AND b.status = 'borrowed'
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setId(String.valueOf(rs.getInt("id")));
                borrow.setBookTitle(rs.getString("book_title"));
                borrow.setDueDate(rs.getDate("return_due_date").toLocalDate());
                borrow.setStatus(rs.getString("status"));
                borrows.add(borrow);
            }
        }

        return borrows;
    }


}
