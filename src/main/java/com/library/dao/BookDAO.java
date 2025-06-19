package com.library.dao;

import com.library.models.Book;
import com.library.models.BookItem;
import com.library.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookDAO {

    public static List<Book> searchBooksByTitle(String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.author, b.publisher, b.year_published, b.isbn, b.category_id, c.name AS category_name, " +
                "COUNT(bi.id) AS total_quantity, " +
                "SUM(CASE WHEN bi.is_available = 1 THEN 1 ELSE 0 END) AS available_quantity " +
                "FROM books b " +
                "LEFT JOIN categories c ON b.category_id = c.id " +
                "LEFT JOIN book_items bi ON bi.book_id = b.id " +
                "WHERE b.title LIKE ? " +
                "GROUP BY b.id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getString("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setYear(rs.getInt("year_published"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setCategoryName(rs.getString("category_name"));
                    book.setQuantity(rs.getInt("total_quantity"));
                    book.setAvailableQuantity(rs.getInt("available_quantity"));
                    books.add(book);
                }
            }
        }

        return books;
    }

    public static int countAllBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM books";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public static List<Book> getAllBooksWithAvailability() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.author, b.publisher, b.isbn, b.year_published, b.category_id, c.name AS categoryName, " +
                "COUNT(bi.id) AS total_quantity, " +
                "SUM(CASE WHEN bi.is_available = 1 THEN 1 ELSE 0 END) AS available_quantity " +
                "FROM books b " +
                "LEFT JOIN book_items bi ON bi.book_id = b.id " +
                "LEFT JOIN categories c ON b.category_id = c.id " +
                "GROUP BY b.id";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("categoryName");
                Book book = new Book(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("year_published"),
                        rs.getString("isbn"),
                        rs.getInt("category_id"),
                        rs.getString("categoryName"),
                        rs.getInt("available_quantity"),
                        rs.getInt("total_quantity")
                );
                books.add(book);
            }
        }
        return books;
    }

    public static int insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, year_published, isbn, category_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setInt(4, book.getYear());
            stmt.setString(5, book.getIsbn());
            stmt.setInt(6, book.getCategory());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID buku
            } else {
                throw new SQLException("Gagal mendapatkan ID buku.");
            }
        }
    }

    public static void insertBookItems(int bookId, int quantity) throws SQLException {
        String sql = "INSERT INTO book_items (book_id, inventory_code, book_condition, is_available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 1; i <= quantity; i++) {
                String inventoryCode = "BOOK-" + bookId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                stmt.setInt(1, bookId);
                stmt.setString(2, inventoryCode);
                stmt.setString(3, "good"); // default condition
                stmt.setInt(4, 1);  // available
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public static void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn = ?, year_published = ?, category_id = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getIsbn());
            stmt.setInt(5, book.getYear());

//            int categoryId = CategoryDAO.getOrCreateCategoryIdByName(book.getCategoryName());
            stmt.setInt(6, book.getCategory());

            stmt.setString(7, book.getId());

            stmt.executeUpdate();
        }
    }

    public static void deleteBook(String bookId) throws SQLException {
        // Hapus dulu item dari book_items (foreign key constraint)
        String deleteItemsSql = "DELETE FROM book_items WHERE book_id = ?";
        String deleteBookSql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(deleteItemsSql);
             PreparedStatement stmt2 = conn.prepareStatement(deleteBookSql)) {

            // Hapus book_items terlebih dahulu
            stmt1.setString(1, bookId);
            stmt1.executeUpdate();

            // Baru hapus buku
            stmt2.setString(1, bookId);
            stmt2.executeUpdate();
        }
    }

    public static List<BookItem> getBookItemsByBookId(String bookId) throws SQLException {
        List<BookItem> items = new ArrayList<>();

        String sql = "SELECT id, inventory_code, `book_condition`, is_available FROM book_items WHERE book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BookItem item = new BookItem(
                        rs.getInt("id"),
                        bookId,
                        rs.getString("inventory_code"),
                        rs.getString("book_condition"),
                        rs.getBoolean("is_available")
                );
                items.add(item);
            }
        }
        return items;
    }

    // Ambil daftar buku dengan quantity > 0
    public static List<Book> getBooksWithAvailableQuantity() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT b.id, b.title, b.author, b.publisher, b.year_published, b.isbn, b.category_id, c.name AS category_name,
                       COUNT(bi.id) AS total_quantity,
                       SUM(CASE WHEN bi.is_available = 1 THEN 1 ELSE 0 END) AS available_quantity
                FROM books b
                LEFT JOIN book_items bi ON bi.book_id = b.id
                LEFT JOIN categories c ON b.category_id = c.id
                GROUP BY b.id
                HAVING available_quantity > 0
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("year_published"),
                        rs.getString("isbn"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("available_quantity"),
                        rs.getInt("total_quantity")
                ));
            }
        }
        return books;
    }

    // Ambil salah satu item tersedia
    public static BookItem getAvailableBookItem(String bookId) throws SQLException {
        String sql = "SELECT * FROM book_items WHERE book_id = ? AND is_available = 1 LIMIT 1";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BookItem(
                        rs.getInt("id"),
                        bookId,
                        rs.getString("inventory_code"),
                        rs.getString("book_condition"),
                        rs.getBoolean("is_available")
                );
            }
        }
        return null;
    }

    // Update ketersediaan buku
    public static void updateBookItemAvailability(int itemId, boolean available) throws SQLException {
        String sql = "UPDATE book_items SET is_available = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }

}
