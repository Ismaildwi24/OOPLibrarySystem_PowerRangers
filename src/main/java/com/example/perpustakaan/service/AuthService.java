package com.example.perpustakaan.service;

import com.example.perpustakaan.models.User;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AuthService {
    private static final String ADMIN_CSV = "src/main/resources/data/admins.csv";
    private static final String STUDENT_CSV = "src/main/resources/data/students.csv";

    public boolean registerAdmin(String username, String password) {
        try {
            // Check if username already exists
            if (isUsernameExists(username)) {
                return false;
            }

            // Append new admin to CSV
            String newLine = String.format("%s,%s\n", username, password);
            Files.write(Paths.get(ADMIN_CSV), newLine.getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerStudent(String username, String studentNumber) {
        try {
            // Check if username or student number already exists
            if (isUsernameExists(username) || isStudentNumberExists(studentNumber)) {
                return false;
            }

            // Append new student to CSV
            String newLine = String.format("%s,%s\n", username, studentNumber);
            Files.write(Paths.get(STUDENT_CSV), newLine.getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String password) {
        try {
            // Check admin login
            List<String> adminLines = Files.readAllLines(Paths.get(ADMIN_CSV));
            for (int i = 1; i < adminLines.size(); i++) { // Skip header
                String[] parts = adminLines.get(i).split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return new User(username, "admin");
                }
            }

            // Check student login
            List<String> studentLines = Files.readAllLines(Paths.get(STUDENT_CSV));
            for (int i = 1; i < studentLines.size(); i++) { // Skip header
                String[] parts = studentLines.get(i).split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return new User(username, "student");
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isUsernameExists(String username) {
        try {
            // Check in admin CSV
            List<String> adminLines = Files.readAllLines(Paths.get(ADMIN_CSV));
            for (int i = 1; i < adminLines.size(); i++) {
                if (adminLines.get(i).split(",")[0].equals(username)) {
                    return true;
                }
            }

            // Check in student CSV
            List<String> studentLines = Files.readAllLines(Paths.get(STUDENT_CSV));
            for (int i = 1; i < studentLines.size(); i++) {
                if (studentLines.get(i).split(",")[0].equals(username)) {
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isStudentNumberExists(String studentNumber) {
        try {
            List<String> studentLines = Files.readAllLines(Paths.get(STUDENT_CSV));
            for (int i = 1; i < studentLines.size(); i++) {
                if (studentLines.get(i).split(",")[1].equals(studentNumber)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 