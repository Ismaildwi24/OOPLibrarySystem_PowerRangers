package com.library.models;

import java.time.LocalDate;

public class Fine {
    private String id;
    private String borrowId;
    private String studentId;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int daysLate;
    private double amount;
    private String status; // "UNPAID", "PAID"

    public Fine(String id, String borrowId, String studentId, LocalDate dueDate, LocalDate returnDate, int daysLate, double amount, String status) {
        this.id = id;
        this.borrowId = borrowId;
        this.studentId = studentId;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.daysLate = daysLate;
        this.amount = amount;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getBorrowId() { return borrowId; }
    public String getStudentId() { return studentId; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public int getDaysLate() { return daysLate; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setBorrowId(String borrowId) { this.borrowId = borrowId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setDaysLate(int daysLate) { this.daysLate = daysLate; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setStatus(String status) { this.status = status; }
} 