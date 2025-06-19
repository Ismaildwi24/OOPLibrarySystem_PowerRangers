package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CSVManager {
    private static final String DATA_DIR = "src/main/resources/data/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Buku
    public static List<Map<String, String>> readBooks() {
        return readCSV(DATA_DIR + "books.csv");
    }

    public static void writeBooks(List<Map<String, String>> books) {
        writeCSV(DATA_DIR + "books.csv", books);
    }

    // Peminjam
    public static List<Map<String, String>> readBorrowers() {
        return readCSV(DATA_DIR + "borrowers.csv");
    }

    public static void writeBorrowers(List<Map<String, String>> borrowers) {
        writeCSV(DATA_DIR + "borrowers.csv", borrowers);
    }

    // Riwayat Peminjaman
    public static List<Map<String, String>> readBorrowHistory() {
        return readCSV(DATA_DIR + "borrow_history.csv");
    }

    public static void writeBorrowHistory(List<Map<String, String>> history) {
        writeCSV(DATA_DIR + "borrow_history.csv", history);
    }

    // Metode untuk menambah peminjaman baru
    public static void addBorrow(String nim, String isbn) {
        List<Map<String, String>> history = readBorrowHistory();
        List<Map<String, String>> books = readBooks();

        // Update stok buku
        for (Map<String, String> book : books) {
            if (book.get("isbn").equals(isbn)) {
                int stok = Integer.parseInt(book.get("stok"));
                book.put("stok", String.valueOf(stok - 1));
                break;
            }
        }
        writeBooks(books);

        // Tambah riwayat peminjaman
        Map<String, String> newBorrow = new HashMap<>();
        newBorrow.put("id", String.valueOf(history.size() + 1));
        newBorrow.put("nim", nim);
        newBorrow.put("isbn", isbn);
        newBorrow.put("tanggal_pinjam", LocalDate.now().format(DATE_FORMATTER));
        newBorrow.put("tanggal_kembali", LocalDate.now().plusDays(14).format(DATE_FORMATTER));
        newBorrow.put("status", "Sedang Dipinjam");
        newBorrow.put("denda", "0");

        history.add(newBorrow);
        writeBorrowHistory(history);
    }

    // Metode untuk mengembalikan buku
    public static void returnBook(String nim, String isbn) {
        List<Map<String, String>> history = readBorrowHistory();
        List<Map<String, String>> books = readBooks();

        // Update stok buku
        for (Map<String, String> book : books) {
            if (book.get("isbn").equals(isbn)) {
                int stok = Integer.parseInt(book.get("stok"));
                book.put("stok", String.valueOf(stok + 1));
                break;
            }
        }
        writeBooks(books);

        // Update status peminjaman
        for (Map<String, String> borrow : history) {
            if (borrow.get("nim").equals(nim) && borrow.get("isbn").equals(isbn) 
                && borrow.get("status").equals("Sedang Dipinjam")) {
                borrow.put("status", "Sudah Dikembalikan");
                
                // Hitung denda jika terlambat
                LocalDate returnDate = LocalDate.parse(borrow.get("tanggal_kembali"), DATE_FORMATTER);
                if (LocalDate.now().isAfter(returnDate)) {
                    int daysLate = (int) (LocalDate.now().toEpochDay() - returnDate.toEpochDay());
                    int denda = daysLate * 1000; // Rp 1.000 per hari
                    borrow.put("denda", String.valueOf(denda));
                }
                break;
            }
        }
        writeBorrowHistory(history);
    }

    // Metode untuk memperpanjang peminjaman
    public static void extendBorrow(String nim, String isbn) {
        List<Map<String, String>> history = readBorrowHistory();

        for (Map<String, String> borrow : history) {
            if (borrow.get("nim").equals(nim) && borrow.get("isbn").equals(isbn) 
                && borrow.get("status").equals("Sedang Dipinjam")) {
                LocalDate currentReturnDate = LocalDate.parse(borrow.get("tanggal_kembali"), DATE_FORMATTER);
                borrow.put("tanggal_kembali", currentReturnDate.plusDays(7).format(DATE_FORMATTER));
                break;
            }
        }
        writeBorrowHistory(history);
    }

    // Metode untuk membaca file CSV
    private static List<Map<String, String>> readCSV(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) return records;

            String[] headers = lines.get(0).split(",");
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                Map<String, String> record = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    record.put(headers[j], values[j]);
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    // Metode untuk menulis file CSV
    private static void writeCSV(String filePath, List<Map<String, String>> records) {
        try {
            if (records.isEmpty()) return;

            List<String> lines = new ArrayList<>();
            // Write headers
            lines.add(String.join(",", records.get(0).keySet()));
            
            // Write data
            for (Map<String, String> record : records) {
                lines.add(String.join(",", record.values()));
            }

            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 