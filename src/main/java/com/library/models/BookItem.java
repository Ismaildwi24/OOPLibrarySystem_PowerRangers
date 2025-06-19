package com.library.models;

public class BookItem {
    private int id;
    private String bookId;
    private String inventoryCode;
    private String condition;
    private boolean isAvailable;

    public BookItem(int id, String bookId, String inventoryCode, String condition, boolean isAvailable) {
        this.id = id;
        this.bookId = bookId;
        this.inventoryCode = inventoryCode;
        this.condition = condition;
        this.isAvailable = isAvailable;
    }
    public int getId() {
        return id;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public String getCondition() {
        return condition;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

