package com.library.models;

public class BorrowingDetail {
    private int id;
    private int borrowingId;
    private int bookItemId;

    public BorrowingDetail() {
    }

    public BorrowingDetail(int id, int borrowingId, int bookItemId) {
        this.id = id;
        this.borrowingId = borrowingId;
        this.bookItemId = bookItemId;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getBookItemId() {
        return bookItemId;
    }

    public void setBookItemId(int bookItemId) {
        this.bookItemId = bookItemId;
    }

}
