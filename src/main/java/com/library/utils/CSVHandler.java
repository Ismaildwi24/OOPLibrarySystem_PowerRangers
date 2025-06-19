package com.library.utils;

import com.library.models.User;
import com.library.models.Book;

import java.io.*;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.time.LocalDate;

import com.library.models.Borrow;
import com.library.models.Return;
import com.library.models.Fine;

public class CSVHandler {
    private static final String STUDENTS_CSV = "src/main/resources/data/students.csv";
    private static final String ADMINS_CSV = "src/main/resources/data/admins.csv";
    private static final String BOOKS_CSV = "src/main/resources/data/books.csv";
    private static final String BORROWS_CSV = "src/main/resources/data/borrows.csv";
    private static final String RETURNS_CSV = "src/main/resources/data/returns.csv";
    private static final String FINES_CSV = "src/main/resources/data/fines.csv";

    // Membaca data mahasiswa dari CSV
    public static List<User> readStudents() {
        List<User> students = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(STUDENTS_CSV))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.length >= 5) {
                    students.add(new User(
                            line[0], // nim/username
                            null,    // password
                            line[1], // name
                            line[0], // studentNumber (nim)
                            "Mahasiswa"
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    // Membaca data admin dari CSV
    public static List<User> readAdmins() {
        List<User> admins = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ADMINS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    admins.add(new User(data[0], data[1], data[0], null, "Admin"));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admins CSV: " + e.getMessage());
        }
        return admins;
    }

    // Menyimpan data mahasiswa ke CSV
    public static void writeStudents(List<User> students) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(STUDENTS_CSV))) {
            // Write header
            writer.writeNext(new String[]{"NIM", "Name", "Email", "Phone", "Address"});

            // Write data
            for (User student : students) {
                writer.writeNext(new String[]{
                        student.getUsername(),
                        student.getName(),
                        student.getEmail(),
                        student.getPhone(),
                        student.getAddress()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menyimpan data admin ke CSV
    public static void writeAdmins(List<User> admins) {
        try (OutputStream os = new FileOutputStream(new File("src/main/resources/" + ADMINS_CSV));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            for (User admin : admins) {
                bw.write(String.format("%s,%s\n",
                        admin.getUsername(),
                        admin.getPassword()));
            }
        } catch (IOException e) {
            System.err.println("Error writing admins CSV: " + e.getMessage());
        }
    }

    // Menambahkan mahasiswa baru ke CSV
    public static void addStudent(User student) {
        List<User> students = readStudents();
        students.add(student);
        writeStudents(students);
    }

    // Menambahkan admin baru ke CSV
    public static void addAdmin(User admin) {
        try (OutputStream os = new FileOutputStream(new File("src/main/resources/" + ADMINS_CSV), true);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os))) {
            bw.write(String.format("%s,%s\n",
                    admin.getUsername(),
                    admin.getPassword()));
        } catch (IOException e) {
            System.err.println("Error adding admin to CSV: " + e.getMessage());
        }
    }

    // Mengecek apakah username mahasiswa sudah ada
    public static boolean isStudentUsernameExists(String username) {
        List<User> students = readStudents();
        return students.stream()
                .anyMatch(student -> student.getUsername().equals(username));
    }

    // Mengecek apakah student number sudah ada
    public static boolean isStudentNumberExists(String studentNumber) {
        List<User> students = readStudents();
        return students.stream()
                .anyMatch(student -> student.getStudentNumber().equals(studentNumber));
    }

    // Mengecek apakah username admin sudah ada
    public static boolean isAdminUsernameExists(String username) {
        List<User> admins = readAdmins();
        return admins.stream()
                .anyMatch(admin -> admin.getUsername().equals(username));
    }

    // Membuat file CSV jika belum ada
    public static void initializeCSVFiles() {
        try {
            // Buat direktori jika belum ada
            File dataDir = new File("src/main/resources/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // Buat file CSV jika belum ada
            File studentsFile = new File("src/main/resources/" + STUDENTS_CSV);
            File adminsFile = new File("src/main/resources/" + ADMINS_CSV);

            if (!studentsFile.exists()) {
                studentsFile.createNewFile();
            }
            if (!adminsFile.exists()) {
                adminsFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error initializing CSV files: " + e.getMessage());
        }
    }

    // Membaca data buku dari CSV
    public static java.util.List<Book> readBooks() {
        java.util.List<Book> books = new java.util.ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + BOOKS_CSV))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 7) {
                    books.add(new Book(
                            data[0], data[1], data[2], data[3], 2025, "a", 1, "", 1, Integer.parseInt(data[6])
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading books CSV: " + e.getMessage());
        }
        return books;
    }

    // Menulis seluruh data buku ke CSV
    public static void writeBooks(java.util.List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/" + BOOKS_CSV))) {
            bw.write("id,title,author,isbn,category,status,quantity\n");
            for (Book book : books) {
                bw.write(String.format("%s,%s,%s,%s,%s,%s,%d\n",
                        book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                        book.getCategory(), book.getStatus(), book.getQuantity()
                ));
            }
        } catch (Exception e) {
            System.err.println("Error writing books CSV: " + e.getMessage());
        }
    }

    // Menambah buku baru ke CSV
    public static void addBook(Book book) {
        java.util.List<Book> books = readBooks();
        books.add(book);
        writeBooks(books);
    }

    // Update buku (berdasarkan id)
    public static void updateBook(Book updatedBook) {
        java.util.List<Book> books = readBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                break;
            }
        }
        writeBooks(books);
    }

    // Hapus buku (berdasarkan id)
    public static void deleteBook(String bookId) {
        java.util.List<Book> books = readBooks();
        books.removeIf(book -> book.getId().equals(bookId));
        writeBooks(books);
    }

    public static void updateStudent(User updatedStudent) {
        List<User> students = readStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUsername().equals(updatedStudent.getUsername())) {
                students.set(i, updatedStudent);
                break;
            }
        }
        writeStudents(students);
    }

    public static void deleteStudent(String username) {
        List<User> students = readStudents();
        students.removeIf(student -> student.getUsername().equals(username));
        writeStudents(students);
    }

    public static List<Borrow> readBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(BORROWS_CSV))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.length >= 7) {
                    borrows.add(new Borrow(
                            line[0], // id
                            line[1], // bookId
                            line[2], // studentId
                            LocalDate.parse(line[3]), // borrowDate
                            LocalDate.parse(line[4]), // dueDate
                            line[5].equals("null") ? null : LocalDate.parse(line[5]), // returnDate
                            line[6]  // status
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return borrows;
    }

    public static void writeBorrows(List<Borrow> borrows) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(BORROWS_CSV))) {
            // Write header
            writer.writeNext(new String[]{"ID", "BookID", "StudentID", "BorrowDate", "DueDate", "ReturnDate", "Status"});

            // Write data
            for (Borrow borrow : borrows) {
                writer.writeNext(new String[]{
                        borrow.getId(),
                        borrow.getBookId(),
                        borrow.getStudentId(),
                        borrow.getBorrowDate().toString(),
                        borrow.getDueDate().toString(),
                        borrow.getReturnDate() == null ? "null" : borrow.getReturnDate().toString(),
                        borrow.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addBorrow(Borrow borrow) {
        List<Borrow> borrows = readBorrows();
        borrows.add(borrow);
        writeBorrows(borrows);
    }

    public static void updateBorrow(Borrow updatedBorrow) {
        List<Borrow> borrows = readBorrows();
        for (int i = 0; i < borrows.size(); i++) {
            if (borrows.get(i).getId().equals(updatedBorrow.getId())) {
                borrows.set(i, updatedBorrow);
                break;
            }
        }
        writeBorrows(borrows);
    }

    public static void deleteBorrow(String id) {
        List<Borrow> borrows = readBorrows();
        borrows.removeIf(borrow -> borrow.getId().equals(id));
        writeBorrows(borrows);
    }

//    public static List<Return> readReturns() {
//        List<Return> returns = new ArrayList<>();
//        try (CSVReader reader = new CSVReader(new FileReader(RETURNS_CSV))) {
//            String[] line;
//            boolean isFirstLine = true;
//            while ((line = reader.readNext()) != null) {
//                if (isFirstLine) {
//                    isFirstLine = false;
//                    continue;
//                }
//                if (line.length >= 7) {
//                    returns.add(new Return(
//                            line[0], // id
//                            line[1], // borrowId
//                            line[2], // bookId
//                            line[3], // studentId
//                            LocalDate.parse(line[4]), // returnDate
//                            line[5], // condition
//                            Double.parseDouble(line[6]) // fine
//                    ));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return returns;
//    }

//    public static void writeReturns(List<Return> returns) {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(RETURNS_CSV))) {
//            // Write header
//            writer.writeNext(new String[]{"ID", "BorrowID", "BookID", "StudentID", "ReturnDate", "Condition", "Fine"});
//
//            // Write data
//            for (Return ret : returns) {
//                writer.writeNext(new String[]{
//                        ret.getId(),
//                        ret.getBorrowId(),
//                        ret.getBookId(),
//                        ret.getStudentId(),
//                        ret.getReturnDate().toString(),
//                        ret.getCondition(),
//                        String.valueOf(ret.getFine())
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void addReturn(Return ret) {
//        List<Return> returns = readReturns();
//        returns.add(ret);
//        writeReturns(returns);
//    }
//
//    public static void updateReturn(Return updatedReturn) {
//        List<Return> returns = readReturns();
//        for (int i = 0; i < returns.size(); i++) {
//            if (returns.get(i).getId().equals(updatedReturn.getId())) {
//                returns.set(i, updatedReturn);
//                break;
//            }
//        }
//        writeReturns(returns);
//    }
//
//    public static void deleteReturn(String id) {
//        List<Return> returns = readReturns();
//        returns.removeIf(ret -> ret.getId().equals(id));
//        writeReturns(returns);
//    }

    public static List<Fine> readFines() {
        List<Fine> fines = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(FINES_CSV))) {
            String[] line;
            boolean isFirstLine = true;
            while ((line = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.length >= 8) {
                    fines.add(new Fine(
                            line[0], // id
                            line[1], // borrowId
                            line[2], // studentId
                            LocalDate.parse(line[3]), // dueDate
                            LocalDate.parse(line[4]), // returnDate
                            Integer.parseInt(line[5]), // daysLate
                            Double.parseDouble(line[6]), // amount
                            line[7]  // status
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fines;
    }

    public static void writeFines(List<Fine> fines) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FINES_CSV))) {
            // Write header
            writer.writeNext(new String[]{"ID", "BorrowID", "StudentID", "DueDate", "ReturnDate", "DaysLate", "Amount", "Status"});

            // Write data
            for (Fine fine : fines) {
                writer.writeNext(new String[]{
                        fine.getId(),
                        fine.getBorrowId(),
                        fine.getStudentId(),
                        fine.getDueDate().toString(),
                        fine.getReturnDate().toString(),
                        String.valueOf(fine.getDaysLate()),
                        String.valueOf(fine.getAmount()),
                        fine.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFine(Fine fine) {
        List<Fine> fines = readFines();
        fines.add(fine);
        writeFines(fines);
    }

    public static void updateFine(Fine updatedFine) {
        List<Fine> fines = readFines();
        for (int i = 0; i < fines.size(); i++) {
            if (fines.get(i).getId().equals(updatedFine.getId())) {
                fines.set(i, updatedFine);
                break;
            }
        }
        writeFines(fines);
    }

    public static void deleteFine(String id) {
        List<Fine> fines = readFines();
        fines.removeIf(fine -> fine.getId().equals(id));
        writeFines(fines);
    }

    // Helper method untuk menghitung denda
    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        int daysLate = (int) java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysLate <= 0) return 0.0;
        return daysLate * 1000.0; // Rp 1.000 per hari keterlambatan
    }

    public static void resetAllCSVs() {
        // Kosongkan semua file CSV utama
        try {
            // Students
            try (CSVWriter writer = new CSVWriter(new FileWriter(STUDENTS_CSV))) {
                writer.writeNext(new String[]{"NIM", "Name", "Email", "Phone", "Address"});
            }
            // Admins
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ADMINS_CSV))) {
                // Header opsional, bisa dikosongkan
            }
            // Books
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_CSV))) {
                bw.write("id,title,author,isbn,category,status,quantity\n");
            }
            // Borrows
            try (CSVWriter writer = new CSVWriter(new FileWriter(BORROWS_CSV))) {
                writer.writeNext(new String[]{"ID", "BookID", "StudentID", "BorrowDate", "DueDate", "ReturnDate", "Status"});
            }
            // Returns
            try (CSVWriter writer = new CSVWriter(new FileWriter(RETURNS_CSV))) {
                writer.writeNext(new String[]{"ID", "BorrowID", "BookID", "StudentID", "ReturnDate", "Condition", "Fine"});
            }
            // Fines
            try (CSVWriter writer = new CSVWriter(new FileWriter(FINES_CSV))) {
                writer.writeNext(new String[]{"ID", "BorrowID", "StudentID", "DueDate", "ReturnDate", "DaysLate", "Amount", "Status"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}