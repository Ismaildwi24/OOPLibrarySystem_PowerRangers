package com.library.views;

import com.library.models.User;
import com.library.utils.CSVHandler;
import com.library.views.admin.AdminDashboardView;
import com.library.views.student.StudentDashboardView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.sql.SQLException;

import com.library.dao.UserDAO;

public class LoginView {
    // Variabel untuk slideshow
    private List<String> imageList;
    private int currentImageIndex = 0;
    private ImageView mainImageView;
    private Timeline slideShowTimeline;
    private HBox indicatorBox;

    // Current scene components
    private Stage primaryStage;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showLoginScene() {
        primaryStage.setMaximized(true);
        // Left Section - Login Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(40, 60, 40, 60));
        formBox.setStyle("-fx-background-color: white;");
        formBox.setPrefWidth(450);

        // Title dan Subtitle
        Text title = new Text("LOGIN");
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 26));
        title.setFill(Color.web("#333333"));

        Text subtitle = new Text("Silahkan masukkan username dan student number");
        subtitle.setFont(Font.font("Poppins", 14));
        subtitle.setFill(Color.GRAY);

        // Role Selection
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton mahasiswaBtn = new RadioButton("Mahasiswa");
        mahasiswaBtn.setToggleGroup(roleGroup);
        mahasiswaBtn.setSelected(true);
        mahasiswaBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");
        RadioButton adminBtn = new RadioButton("Admin");
        adminBtn.setToggleGroup(roleGroup);
        adminBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");
        HBox roleBox = new HBox(30, mahasiswaBtn, adminBtn);
        roleBox.setAlignment(Pos.CENTER_LEFT);
        roleBox.setPadding(new Insets(5, 0, 5, 0));

        // Input Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        usernameField.setPrefHeight(45);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        passwordField.setPrefHeight(45);
        passwordField.setVisible(false);
        passwordField.setManaged(false);

        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Password");
        passwordVisibleField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        passwordVisibleField.setPrefHeight(45);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);

        // Eye icon toggle
        Button togglePasswordBtn = new Button("👁");
        togglePasswordBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16;");
        togglePasswordBtn.setFocusTraversable(false);
        togglePasswordBtn.setVisible(false);
        togglePasswordBtn.setManaged(false);

        // Sync password fields
        passwordField.textProperty().bindBidirectional(passwordVisibleField.textProperty());

        // Password field with eye icon in HBox
        HBox passwordBox = new HBox(passwordField, passwordVisibleField, togglePasswordBtn);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setSpacing(0);
        passwordBox.setPrefHeight(45);

        TextField studentNumberField = new TextField();
        studentNumberField.setPromptText("Student Number");
        studentNumberField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        studentNumberField.setPrefHeight(45);
        studentNumberField.setVisible(true);

        // Listener untuk mengubah form berdasarkan role
        roleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                if (selected == mahasiswaBtn) {
                    passwordField.setVisible(false);
                    passwordField.setManaged(false);
                    passwordVisibleField.setVisible(false);
                    passwordVisibleField.setManaged(false);
                    togglePasswordBtn.setVisible(false);
                    togglePasswordBtn.setManaged(false);
                    studentNumberField.setVisible(true);
                    studentNumberField.setManaged(true);
                    subtitle.setText("Silahkan masukkan username dan student number");
                } else {
                    passwordField.setVisible(true);
                    passwordField.setManaged(true);
                    passwordVisibleField.setVisible(false);
                    passwordVisibleField.setManaged(false);
                    togglePasswordBtn.setVisible(true);
                    togglePasswordBtn.setManaged(true);
                    studentNumberField.setVisible(false);
                    studentNumberField.setManaged(false);
                    subtitle.setText("Silahkan masukkan username dan password");
                }
            }
        });

        // Toggle logic
        togglePasswordBtn.setOnAction(e -> {
            boolean showing = passwordVisibleField.isVisible();
            if (showing) {
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                togglePasswordBtn.setText("👁");
            } else {
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                togglePasswordBtn.setText("🙈");
            }
        });

        // Login Button
        Button loginBtn = new Button("Login Now");
        loginBtn.setPrefWidth(250);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                "-fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-font-family: 'Poppins'; -fx-font-size: 16; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");

        // Hover effects
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #e53e3e, #dd2476); " +
                    "-fx-text-fill: white; -fx-background-radius: 10; " +
                    "-fx-font-family: 'Poppins'; -fx-font-size: 16; " +
                    "-fx-font-weight: bold; -fx-cursor: hand;");
        });

        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
                    "-fx-text-fill: white; -fx-background-radius: 10; " +
                    "-fx-font-family: 'Poppins'; -fx-font-size: 16; " +
                    "-fx-font-weight: bold; -fx-cursor: hand;");
        });

        // Login Action dengan validasi database
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            String role = selectedRole.getText();

            if (username.isEmpty()) {
                showAlert("Error", "Username tidak boleh kosong!");
                return;
            }

            try {
                if (role.equals("Mahasiswa")) {
                    String studentNumber = studentNumberField.getText().trim();

                    if (studentNumber.isEmpty()) {
                        showAlert("Error", "Student Number tidak boleh kosong!");
                        return;
                    }

                    if (!studentNumber.matches("\\d+")) {
                        showAlert("Error", "Student Number harus berupa angka!");
                        return;
                    }

                    // Gunakan student number sebagai password (atau sesuai yang disimpan di DB)
                    User user = UserDAO.loginUserMahasiswa(username, studentNumber);

                    if (user != null && "Mahasiswa".equalsIgnoreCase(user.getRole())) {
                        showAlert("Success", "Login berhasil sebagai Mahasiswa!\nUsername: " + username);
                        System.out.println("User");
                        System.out.println(user);
                        showDashboard(user);
                    } else {
                        showAlert("Error", "Username atau Student Number salah!");
                    }

                } else {
                    String password = passwordField.getText();
                    if (password.isEmpty()) {
                        showAlert("Error", "Password tidak boleh kosong!");
                        return;
                    }

                    User user = UserDAO.loginUserAdmin(username, password);

                    if (user != null && "Admin".equalsIgnoreCase(user.getRole())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Login berhasil sebagai Admin!\nUsername: " + username);
                        alert.setOnHidden(evt -> showDashboard(user));
                        alert.show();
                    } else {
                        showAlert("Error", "Username atau Password admin salah!");
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Terjadi kesalahan koneksi ke database:\n" + ex.getMessage());
            }
        });


        // Divider
        Label divider = new Label("Atau");
        divider.setPadding(new Insets(20, 0, 10, 0));
        divider.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 14));
        divider.setTextFill(Color.GRAY);
        divider.setAlignment(Pos.CENTER);

        // Register Button
        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(250);
        registerBtn.setPrefHeight(40);
        registerBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ff416c; " +
                "-fx-border-width: 2; -fx-text-fill: #ff416c; -fx-background-radius: 10; -fx-border-radius: 10; " +
                "-fx-font-family: 'Poppins'; -fx-font-size: 14; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");

        // Hover effect for register
        registerBtn.setOnMouseEntered(e -> {
            registerBtn.setStyle("-fx-background-color: #ff416c; -fx-border-color: #ff416c; " +
                    "-fx-border-width: 2; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-radius: 10; " +
                    "-fx-font-family: 'Poppins'; -fx-font-size: 14; " +
                    "-fx-font-weight: bold; -fx-cursor: hand;");
        });
        registerBtn.setOnMouseExited(e -> {
            registerBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ff416c; " +
                    "-fx-border-width: 2; -fx-text-fill: #ff416c; -fx-background-radius: 10; -fx-border-radius: 10; " +
                    "-fx-font-family: 'Poppins'; -fx-font-size: 14; " +
                    "-fx-font-weight: bold; -fx-cursor: hand;");
        });
        registerBtn.setOnAction(e -> {
            RegisterView registerView = new RegisterView(primaryStage);
            registerView.showRegisterScene();
        });

//        registerBtn.setOnAction(e -> showRegisterScene());

        Hyperlink forgotPassword = new Hyperlink("Lupa Password?");
        forgotPassword.setFont(Font.font("Poppins", 12));
        forgotPassword.setStyle("-fx-text-fill: #ff416c;");
        forgotPassword.setOnAction(e -> showAlert("Info", "Fitur Lupa Password belum diimplementasi"));

        // Add all to formBox
        formBox.getChildren().addAll(
                title, subtitle, usernameField, passwordBox, studentNumberField,
                roleBox, loginBtn, divider, registerBtn
        );

        // Create slideshow section
        StackPane imagePane = createSlideshowSection();

        // Combine sections
        HBox root = new HBox(formBox, imagePane);
        Scene scene = new Scene(root, 1200, 700);

        // Setup animations
        setupSceneAnimations(formBox, imagePane);

        // Setup Stage
        primaryStage.setTitle("Student Login System");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void showRegisterScene() {
        stopAutoSlide(); // Stop slideshow saat pindah scene

        // Left Section - Register Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(40, 60, 40, 60));
        formBox.setStyle("-fx-background-color: white;");
        formBox.setPrefWidth(450);

        // Title dan Subtitle
        Text title = new Text("REGISTERz");
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 26));
        title.setFill(Color.web("#333333"));

        Text subtitle = new Text("Buat akun baru untuk mengakses sistem");
        subtitle.setFont(Font.font("Poppins", 14));
        subtitle.setFill(Color.GRAY);

        // Input Fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        usernameField.setPrefHeight(45);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        passwordField.setPrefHeight(45);
        passwordField.setVisible(false);
        passwordField.setManaged(false);

        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Password");
        passwordVisibleField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        passwordVisibleField.setPrefHeight(45);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);

        // Eye icon toggle
        Button togglePasswordBtn = new Button("👁");
        togglePasswordBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16;");
        togglePasswordBtn.setFocusTraversable(false);
        togglePasswordBtn.setVisible(false);
        togglePasswordBtn.setManaged(false);

        // Sync password fields
        passwordField.textProperty().bindBidirectional(passwordVisibleField.textProperty());

        // Toggle logic
        togglePasswordBtn.setOnAction(e -> {
            boolean showing = passwordVisibleField.isVisible();
            if (showing) {
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                togglePasswordBtn.setText("👁");
            } else {
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                togglePasswordBtn.setText("🙈");
            }
        });

        TextField studentNumberField = new TextField();
        studentNumberField.setPromptText("Student Number");
        studentNumberField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        studentNumberField.setPrefHeight(45);
        studentNumberField.setVisible(true);

        // Role Selection
        ToggleGroup roleGroup = new ToggleGroup();

        RadioButton mahasiswaBtn = new RadioButton("Mahasiswa");
        mahasiswaBtn.setToggleGroup(roleGroup);
        mahasiswaBtn.setSelected(true);
        mahasiswaBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");

        RadioButton adminBtn = new RadioButton("Admin");
        adminBtn.setToggleGroup(roleGroup);
        adminBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");

        HBox roleBox = new HBox(30, mahasiswaBtn, adminBtn);
        roleBox.setAlignment(Pos.CENTER_LEFT);
        roleBox.setPadding(new Insets(5, 0, 5, 0));

        // Listener untuk mengubah form berdasarkan role
        roleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selected = (RadioButton) newToggle;
                if (selected == mahasiswaBtn) {
                    passwordField.setVisible(false);
                    passwordField.setManaged(false);
                    passwordVisibleField.setVisible(false);
                    passwordVisibleField.setManaged(false);
                    togglePasswordBtn.setVisible(false);
                    togglePasswordBtn.setManaged(false);
                    studentNumberField.setVisible(true);
                    studentNumberField.setManaged(true);
                    subtitle.setText("Masukkan username dan student number");
                } else {
                    passwordField.setVisible(true);
                    passwordField.setManaged(true);
                    passwordVisibleField.setVisible(false);
                    passwordVisibleField.setManaged(false);
                    togglePasswordBtn.setVisible(true);
                    togglePasswordBtn.setManaged(true);
                    studentNumberField.setVisible(false);
                    studentNumberField.setManaged(false);
                    subtitle.setText("Masukkan username dan password");
                }
            }
        });

        // Register Button
        Button registerBtn = new Button("Register Now");
        registerBtn.setPrefWidth(250);
        registerBtn.setPrefHeight(45);
        registerBtn.setStyle("-fx-background-color: linear-gradient(to right, #6C63FF, #5A52D5); " +
                "-fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-font-family: 'Poppins'; -fx-font-size: 16; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");

        // Register Action
        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
            String role = selectedRole.getText();

            if (username.isEmpty()) {
                showAlert("Error", "Username tidak boleh kosong!");
                return;
            }

            if (role.equals("Mahasiswa")) {
                String studentNumber = studentNumberField.getText().trim();
                if (studentNumber.isEmpty()) {
                    showAlert("Error", "Student Number tidak boleh kosong!");
                    return;
                }
                if (!studentNumber.matches("\\d+")) {
                    showAlert("Error", "Student Number harus berupa angka!");
                    return;
                }
                // Cek username dan student number sudah ada di CSV
                boolean usernameExists = false, studentNumberExists = false;
                for (User u : CSVHandler.readStudents()) {
                    if (u.getUsername().equals(username)) usernameExists = true;
                    if (u.getStudentNumber().equals(studentNumber)) studentNumberExists = true;
                }
                if (usernameExists) {
                    showAlert("Error", "Username sudah terdaftar!");
                    return;
                }
                if (studentNumberExists) {
                    showAlert("Error", "Student Number sudah terdaftar!");
                    return;
                }
                CSVHandler.addStudent(new User(username, null, username, studentNumber, "Mahasiswa"));
                showAlert("Success", "Registrasi berhasil!\nUsername: " + username + "\nStudent Number: " + studentNumber + "\n\nSilahkan login dengan akun baru Anda.");
                showLoginScene();
                return;
            } else {
                String password = passwordField.getText();
                String confirmPassword = passwordVisibleField.getText();

                if (password.isEmpty()) {
                    showAlert("Error", "Password tidak boleh kosong!");
                    return;
                }
                if (password.length() < 6) {
                    showAlert("Error", "Password minimal 6 karakter!");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    showAlert("Error", "Password dan konfirmasi password tidak sama!");
                    return;
                }
                // Cek username sudah ada di CSV
                boolean usernameExists = false;
                for (User u : CSVHandler.readAdmins()) {
                    if (u.getUsername().equals(username)) usernameExists = true;
                }
                if (usernameExists) {
                    showAlert("Error", "Username admin sudah terdaftar!");
                    return;
                }
                CSVHandler.addAdmin(new User(username, password, username, null, "Admin"));
                showAlert("Success", "Registrasi berhasil!\nUsername: " + username + "\n\nSilahkan login dengan akun baru Anda.");
                showLoginScene();
                return;
            }
        });

        // Back to Login Button
        Button backToLoginBtn = new Button("Back to Login");
        backToLoginBtn.setPrefWidth(250);
        backToLoginBtn.setPrefHeight(40);
        backToLoginBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #6C63FF; " +
                "-fx-border-width: 2; -fx-text-fill: #6C63FF; -fx-background-radius: 10; -fx-border-radius: 10; " +
                "-fx-font-family: 'Poppins'; -fx-font-size: 14; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");
        backToLoginBtn.setOnAction(e -> showLoginScene());

        // Add all to formBox
        formBox.getChildren().addAll(
                title, subtitle, usernameField, passwordField, passwordVisibleField,
                studentNumberField, roleBox, registerBtn, backToLoginBtn
        );

        // Create slideshow section
        StackPane imagePane = createSlideshowSection();

        // Combine sections
        HBox root = new HBox(formBox, imagePane);
        Scene scene = new Scene(root, 1200, 700);

        // Setup animations
        setupSceneAnimations(formBox, imagePane);

        primaryStage.setScene(scene);
    }

    private void showDashboard(User user) {
        try {
            stopAutoSlide();
            System.out.println("Showing dashboard for user: " + user.getUsername() + " with role: " + user.getRole());

            Stage dashboardStage = new Stage();

            dashboardStage.setOnShown(e -> {
                System.out.println("✅ Dashboard shown, closing login window...");
                primaryStage.close();
            });

            if ("Mahasiswa".equalsIgnoreCase(user.getRole())) {
                StudentDashboardView studentView = new StudentDashboardView();
                studentView.show(dashboardStage, user); // sudah otomatis .show()
            } else if ("Admin".equalsIgnoreCase(user.getRole())) {
                AdminDashboardView adminView = new AdminDashboardView();
                adminView.show(dashboardStage, user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal menampilkan dashboard: " + e.getMessage());
        }
    }


    private StackPane createSlideshowSection() {
        StackPane imagePane = new StackPane();
        imagePane.setPrefWidth(750);

        // Daftar gambar untuk slideshow
        imageList = Arrays.asList(
                getClass().getResource("/images/brosur1.jpg").toExternalForm(),
                getClass().getResource("/images/brosur2.jpg").toExternalForm(),
                getClass().getResource("/images/brosur3.jpg").toExternalForm()
        );

        // Gradient background sebagai fallback
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#ff416c")),
                        new Stop(0.5, Color.web("#ff4b2b")),
                        new Stop(1, Color.web("#ff9a9e"))
                ),
                new CornerRadii(0), Insets.EMPTY
        );
        imagePane.setBackground(new Background(bgFill));

        // Main ImageView untuk slideshow
        mainImageView = new ImageView();
        mainImageView.setFitWidth(750);
        mainImageView.setPreserveRatio(true);
        mainImageView.setSmooth(true);

        // Drop shadow effect
        javafx.scene.effect.DropShadow shadow = new javafx.scene.effect.DropShadow();
        shadow.setColor(Color.color(0, 0, 0, 0.3));
        shadow.setOffsetX(10);
        shadow.setOffsetY(10);
        shadow.setRadius(20);
        mainImageView.setEffect(shadow);

        // Load gambar pertama
        loadCurrentImage();

        // Tombol navigasi
        Button prevButton = new Button("❮");
        Button nextButton = new Button("❯");

        String buttonStyle = "-fx-background-color: rgba(255,255,255,0.9); " +
                "-fx-text-fill: #333; -fx-font-size: 18; -fx-font-weight: bold; " +
                "-fx-background-radius: 25; -fx-min-width: 50; -fx-min-height: 50; " +
                "-fx-cursor: hand; -fx-border-color: rgba(0,0,0,0.1); -fx-border-width: 1;";

        prevButton.setStyle(buttonStyle);
        nextButton.setStyle(buttonStyle);

        // Event handlers
        prevButton.setOnAction(e -> {
            stopAutoSlide();
            previousImage();
            startAutoSlide();
        });

        nextButton.setOnAction(e -> {
            stopAutoSlide();
            nextImage();
            startAutoSlide();
        });

        // Posisi tombol
        StackPane.setAlignment(prevButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(nextButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(prevButton, new Insets(0, 0, 0, 20));
        StackPane.setMargin(nextButton, new Insets(0, 20, 0, 0));

        // Indicator dots
        indicatorBox = createIndicators();
        StackPane.setAlignment(indicatorBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(indicatorBox, new Insets(0, 0, 20, 0));

        // Add elements to imagePane
        imagePane.getChildren().addAll(mainImageView, prevButton, nextButton, indicatorBox);

        // Start auto slideshow
        startAutoSlide();

        return imagePane;
    }

    private void setupSceneAnimations(VBox formBox, StackPane imagePane) {
        // Animations
        formBox.setTranslateX(-500);
        imagePane.setTranslateX(500);
        formBox.setOpacity(0);
        imagePane.setOpacity(0);

        TranslateTransition slideForm = new TranslateTransition(Duration.millis(1200), formBox);
        slideForm.setToX(0);
        slideForm.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeForm = new FadeTransition(Duration.millis(1200), formBox);
        fadeForm.setToValue(1.0);

        TranslateTransition slideImage = new TranslateTransition(Duration.millis(1200), imagePane);
        slideImage.setToX(0);
        slideImage.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeImage = new FadeTransition(Duration.millis(1200), imagePane);
        fadeImage.setToValue(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(
                slideForm, fadeForm, slideImage, fadeImage
        );
        parallelTransition.setDelay(Duration.millis(200));
        parallelTransition.play();
    }

    // Method untuk membuat indicator dots
    private HBox createIndicators() {
        HBox indicators = new HBox(8);
        indicators.setAlignment(Pos.CENTER);

        for (int i = 0; i < imageList.size(); i++) {
            Button dot = new Button();
            dot.setPrefSize(12, 12);
            dot.setMinSize(12, 12);
            dot.setMaxSize(12, 12);

            updateDotStyle(dot, i == currentImageIndex);

            final int index = i;
            dot.setOnAction(e -> {
                stopAutoSlide();
                goToImage(index);
                startAutoSlide();
            });

            indicators.getChildren().add(dot);
        }

        return indicators;
    }

    // Update style dot
    private void updateDotStyle(Button dot, boolean isActive) {
        if (isActive) {
            dot.setStyle("-fx-background-color: white; -fx-background-radius: 6; -fx-cursor: hand;");
        } else {
            dot.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 6; -fx-cursor: hand;");
        }
    }

    // Load gambar saat ini
    private void loadCurrentImage() {
        try {
            String imagePath = imageList.get(currentImageIndex);
            Image image = new Image(imagePath);

            // Fade transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), mainImageView);
            fadeOut.setToValue(0.3);
            fadeOut.setOnFinished(e -> {
                mainImageView.setImage(image);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), mainImageView);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();

            updateIndicators();

        } catch (Exception e) {
            System.out.println("Error loading image: " + imageList.get(currentImageIndex));
        }
    }

    // Update indicators
    private void updateIndicators() {
        for (int i = 0; i < indicatorBox.getChildren().size(); i++) {
            Button dot = (Button) indicatorBox.getChildren().get(i);
            updateDotStyle(dot, i == currentImageIndex);
        }
    }

    // Navigation methods
    private void nextImage() {
        currentImageIndex = (currentImageIndex + 1) % imageList.size();
        loadCurrentImage();
    }

    private void previousImage() {
        currentImageIndex = (currentImageIndex - 1 + imageList.size()) % imageList.size();
        loadCurrentImage();
    }

    private void goToImage(int index) {
        currentImageIndex = index;
        loadCurrentImage();
    }

    // Auto slideshow control
    private void startAutoSlide() {
        slideShowTimeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> nextImage()));
        slideShowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideShowTimeline.play();
    }

    private void stopAutoSlide() {
        if (slideShowTimeline != null) {
            slideShowTimeline.stop();
        }
    }

    // Helper method untuk alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");

        alert.showAndWait();
    }
}