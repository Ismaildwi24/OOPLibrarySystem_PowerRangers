package com.library.models;

import java.time.LocalDate;

public class Borrowing {
    private int id;
    private int userId;
    private LocalDate borrowDate;
    private LocalDate returnDueDate;
    private LocalDate returnDate;
    private String status;

    public Borrowing() {
    }

    public Borrowing(int userId, LocalDate borrowDate, LocalDate returnDueDate, String status) {
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDueDate = returnDueDate;
        this.status = status;
    }

    public Borrowing(int id, int userId, LocalDate borrowDate, LocalDate returnDueDate, LocalDate returnDate, String status) {
        this.id = id;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDueDate = returnDueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(LocalDate returnDueDate) {
        this.returnDueDate = returnDueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
