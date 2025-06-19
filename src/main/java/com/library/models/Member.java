package com.library.models;

public class Member {
    private String id;
    private String name;
    private String username;
    private String phone;
    private String address;
    private String joinDate;

    public Member(String id, String name, String username, String phone, String address, String joinDate) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.joinDate = joinDate;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setName(String name) {
        this.name = name;
    }

}