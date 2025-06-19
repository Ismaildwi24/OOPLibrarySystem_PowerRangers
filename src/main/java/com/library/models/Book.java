package com.library.models;

public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String isbn;

    private int category;           // category_id dari tabel (int)
    private String categoryName;    // nama kategori dari tabel categories

    private String status;
    private int quantity;
    private int availableQuantity;
    private int totalQuantity;

    // Constructor untuk keperluan tampil data
    public Book(String id, String title, String author, String publisher, int year,
                String isbn, int category, String categoryName,
                int availableQuantity, int totalQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.category = category;
        this.categoryName = categoryName;
        this.availableQuantity = availableQuantity;
        this.totalQuantity = totalQuantity;
        this.quantity = totalQuantity;
    }

    public Book() {
    }

    // Constructor untuk edit (tanpa kuantitas jika tidak diperlukan)
    public Book(String id, String title, String author, String publisher, int year,
                String isbn, int category, String categoryName,
                String status, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.category = category;
        this.categoryName = categoryName;
        this.status = status;
        this.quantity = quantity;
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCategory() {
        return category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

} 