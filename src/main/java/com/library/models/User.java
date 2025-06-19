package com.library.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String studentNumber;

    public User(int id, String username, String password, String name, String studentNumber, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentNumber = studentNumber;
        this.role = role;
    }

    public User() {
        this.username = "";
        this.password = "";
        this.name = "";
        this.studentNumber = "";
        this.role = "";
    }

    public User(String username, String password, String name, String studentNumber, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentNumber = studentNumber;
        this.role = role;
    }

    public User(String username, String password, String name, String studentNumber, String role, String phone, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentNumber = studentNumber;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // Getters
    public int getId() {
        return id;
    }

    ;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
} 