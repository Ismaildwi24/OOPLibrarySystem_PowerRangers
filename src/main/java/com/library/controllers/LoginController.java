package com.library.controllers;

import com.library.models.User;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    private Map<String, User> mahasiswaDatabase;
    private Map<String, User> adminDatabase;

    public LoginController() {
        // Inisialisasi database kosong
        mahasiswaDatabase = new HashMap<>();
        adminDatabase = new HashMap<>();
    }

    public boolean validateStudentLogin(String username, String studentNumber) {
        if (mahasiswaDatabase.containsKey(username)) {
            User user = mahasiswaDatabase.get(username);
            return user.getStudentNumber().equals(studentNumber);
        }
        return false;
    }

    public boolean validateAdminLogin(String username, String password) {
        if (adminDatabase.containsKey(username)) {
            User user = adminDatabase.get(username);
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User getStudentUser(String username) {
        return mahasiswaDatabase.get(username);
    }

    public User getAdminUser(String username) {
        return adminDatabase.get(username);
    }

    public boolean registerStudent(String username, String studentNumber) {
        // Cek apakah username sudah ada
        if (mahasiswaDatabase.containsKey(username)) {
            return false;
        }
        // Cek apakah student number sudah ada
        for (User user : mahasiswaDatabase.values()) {
            if (user.getStudentNumber().equals(studentNumber)) {
                return false;
            }
        }
        // Register mahasiswa baru
        mahasiswaDatabase.put(username, new User(username, null, username, studentNumber, "Mahasiswa"));
        return true;
    }

    public boolean registerAdmin(String username, String password) {
        // Cek apakah username sudah ada
        if (adminDatabase.containsKey(username)) {
            return false;
        }
        // Register admin baru
        adminDatabase.put(username, new User(username, password, username, null, "Admin"));
        return true;
    }
} 