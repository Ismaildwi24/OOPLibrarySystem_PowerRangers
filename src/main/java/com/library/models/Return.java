package com.library.models;

import java.time.LocalDate;

public class Return {
    private int id;
    private int borrowId;
    private int bookId;         // ⬅️ Tambahan
    private String bookName;         // ⬅️ Tambahan
    private String studentName;         // ⬅️ Tambahan
    private int bookItemId;
    private int studentId;
    private int no;
    private LocalDate returnDate;
    private String condition;   // "GOOD", "DAMAGED", "LOST"
    private double fine;
    private LocalDate dueDate; // ⬅️ Tambahan
    private LocalDate borrowDate; // ⬅️ Tambahan


    // Constructor lengkap
    public Return(int id, int borrowId, int bookId, int bookItemId, int studentId,
                  LocalDate returnDate, String condition, double fine) {
        this.id = id;
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.bookItemId = bookItemId;
        this.studentId = studentId;
        this.returnDate = returnDate;
        this.condition = condition;
        this.fine = fine;
    }

    // Constructor kosong
    public Return() {
    }

    // Getters
    public String getBookName() {
        return bookName;
    }

    public int getNo() {
        return no;
    }

    public String getStudentName() {
        return studentName;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }


    public int getBorrowId() {
        return borrowId;
    }

    public int getBookId() {
        return bookId;
    }                // ⬅️ Tambahan

    public int getBookItemId() {
        return bookItemId;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getCondition() {
        return condition;
    }

    public double getFine() {
        return fine;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setBookItemId(int bookItemId) {
        this.bookItemId = bookItemId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setNo(int no) {
        this.no = no;
    }

}
