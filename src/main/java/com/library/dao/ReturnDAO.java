package com.library.dao;

import com.library.models.Return;
import com.library.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnDAO {
    public static List<Return> getAllReturnHistory() throws SQLException {
        List<Return> returns = new ArrayList<>();

        String sql = """
                    SELECT\s
                    ROW_NUMBER() OVER (ORDER BY b.return_date IS NULL, b.return_date DESC) AS no,
                    b.id AS borrow_id,
                    b.borrow_date,
                    b.return_due_date,
                    b.return_date,
                    u.id AS student_id,
                    u.name AS student_name,
                    bi.book_id,
                    bo.title AS book_name,
                    bi.id AS book_item_id,
                    bi.book_condition,
                    CASE
                        WHEN b.return_date IS NOT NULL AND b.return_date > b.return_due_date THEN
                            DATEDIFF(b.return_date, b.return_due_date) * 1000
                        ELSE 0
                    END AS fine
                FROM borrowings b
                JOIN borrowing_details bd ON b.id = bd.borrowing_id
                JOIN book_items bi ON bd.book_item_id = bi.id
                JOIN books bo ON bi.book_id = bo.id
                JOIN users u ON b.users_id = u.id
                ORDER BY b.return_date IS NULL, b.return_date DESC;
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Return ret = new Return();
                ret.setNo(rs.getInt("no"));
                ret.setBorrowId(rs.getInt("borrow_id"));
                ret.setStudentId(rs.getInt("student_id"));
                ret.setBookId(rs.getInt("book_id"));
                ret.setBookItemId(rs.getInt("book_item_id"));
                Date returnDate = rs.getDate("return_date");
                ret.setReturnDate(returnDate != null ? returnDate.toLocalDate() : null);
                Date dueDate = rs.getDate("return_due_date");
                ret.setDueDate(dueDate != null ? dueDate.toLocalDate() : null);
                ret.setCondition(rs.getString("book_condition"));
                ret.setFine(rs.getDouble("fine"));
                ret.setBookName(rs.getString("book_name"));
                ret.setStudentName(rs.getString("student_name"));
                ret.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                returns.add(ret);
            }
        }

        return returns;
    }

    public static void returnBook(int borrowingId, int bookItemId, String condition, LocalDate returnDate) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Ambil tanggal jatuh tempo dari borrowing
            String getDueDateSql = "SELECT return_due_date FROM borrowings WHERE id = ?";
            LocalDate dueDate = null;

            try (PreparedStatement stmt = conn.prepareStatement(getDueDateSql)) {
                stmt.setInt(1, borrowingId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        dueDate = rs.getDate("return_due_date").toLocalDate();
                    }
                }
            }

            // Update status pengembalian
            String updateBorrow = "UPDATE borrowings SET return_date = ?, status = 'returned' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateBorrow)) {
                stmt.setDate(1, Date.valueOf(returnDate));
                stmt.setInt(2, borrowingId);
                stmt.executeUpdate();
            }

            // Update status ketersediaan dan kondisi buku
            String updateBookItem = "UPDATE book_items SET is_available = 1, book_condition = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateBookItem)) {
                stmt.setString(1, condition);
                stmt.setInt(2, bookItemId);
                stmt.executeUpdate();
            }

            // Jika terlambat, hitung denda dan masukkan ke tabel fines
            if (dueDate != null && returnDate.isAfter(dueDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
                double fineAmount = daysLate * 1000.0; // Rp1000 per hari

//                String insertFine = "INSERT INTO fines (borrowing_id, amount, paid) VALUES (?, ?, 0)";
//                try (PreparedStatement stmt = conn.prepareStatement(insertFine)) {
//                    stmt.setInt(1, borrowingId);
//                    stmt.setDouble(2, fineAmount);
//                    stmt.executeUpdate();
//                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // lempar lagi supaya bisa ditangani di UI/controller
        }
    }


}
