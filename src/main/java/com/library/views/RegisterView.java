package com.library.views;

import com.library.models.User;
import com.library.utils.CSVHandler;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
//import com.mysql.cj.jdbc.Driver;


import java.util.Arrays;
import java.util.List;

import com.library.dao.UserDAO;
import javafx.util.Duration;

public class RegisterView {
    public Stage primaryStage;
    public TextField usernameField;
    public TextField nameField;
    public PasswordField passwordField;
    //    public PasswordField confirmPasswordField;
    public TextField studentNumberField;
    public TextField studentPhoneNumberField;
    public TextField studentAddressField;
    //    public ComboBox<String> roleComboBox;
    public Timeline slideShowTimeline;
    public HBox indicatorBox;
    public List<String> imageList;
    public ImageView mainImageView;
    public int currentImageIndex = 0;
    private ToggleGroup roleGroup;


    public RegisterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void stopAutoSlide() {
        if (slideShowTimeline != null) {
            slideShowTimeline.stop();
        }
    }

    private void updateDotStyle(Button dot, boolean isActive) {
        if (isActive) {
            dot.setStyle("-fx-background-color: white; -fx-background-radius: 6; -fx-cursor: hand;");
        } else {
            dot.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 6; -fx-cursor: hand;");
        }
    }

    private void updateIndicators() {
        for (int i = 0; i < indicatorBox.getChildren().size(); i++) {
            Button dot = (Button) indicatorBox.getChildren().get(i);
            updateDotStyle(dot, i == currentImageIndex);
        }
    }

    private void nextImage() {
        currentImageIndex = (currentImageIndex + 1) % imageList.size();
        loadCurrentImage();
    }

    private void previousImage() {
        currentImageIndex = (currentImageIndex - 1 + imageList.size()) % imageList.size();
        loadCurrentImage();
    }

    private void startAutoSlide() {
        slideShowTimeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> nextImage()));
        slideShowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideShowTimeline.play();
    }

    private void goToImage(int index) {
        currentImageIndex = index;
        loadCurrentImage();
    }

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

    public void showRegisterScene() {
        stopAutoSlide(); // Stop slideshow saat pindah scene

        // Left Section - Register Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.TOP_LEFT);
        formBox.setPadding(new Insets(200, 60, 40, 60));
        formBox.setStyle("-fx-background-color: white;");
        formBox.setPrefWidth(450);

        // Title dan Subtitle
        Text title = new Text("REGISTER");
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 26));
        title.setFill(Color.web("#333333"));

        Text subtitle = new Text("Buat akun baru untuk mengakses sistem");
        subtitle.setFont(Font.font("Poppins", 14));
        subtitle.setFill(Color.GRAY);

        // Input Fields
        nameField = new TextField(); // ini mengisi field global
        nameField.setPromptText("Name");
        nameField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        nameField.setPrefHeight(45);

        usernameField = new TextField(); // ini mengisi field global
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        usernameField.setPrefHeight(45);

        passwordField = new PasswordField(); // ✅
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

        studentNumberField = new TextField();
        studentNumberField.setPromptText("Student Number");
        studentNumberField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        studentNumberField.setPrefHeight(45);
        studentNumberField.setVisible(true);

        studentPhoneNumberField = new TextField();
        studentPhoneNumberField.setPromptText("Phone Number");
        studentPhoneNumberField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        studentPhoneNumberField.setPrefHeight(45);
        studentPhoneNumberField.setVisible(true);

        studentAddressField = new TextField();
        studentAddressField.setPromptText("Address");
        studentAddressField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 12; " +
                "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-font-size: 14;");
        studentAddressField.setPrefHeight(45);
        studentAddressField.setVisible(true);

        // Role Selection
        roleGroup = new ToggleGroup(); // ✅ class field

        RadioButton mahasiswaBtn = new RadioButton("Mahasiswa");
        mahasiswaBtn.setToggleGroup(roleGroup);
        mahasiswaBtn.setSelected(true);
        mahasiswaBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");

        RadioButton adminBtn = new RadioButton("Admin");
        adminBtn.setToggleGroup(roleGroup);
        adminBtn.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 14;");

        HBox roleBox = new HBox(30, mahasiswaBtn);
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
            handleRegister();
        });

        // Back to Login Button
        Button backToLoginBtn = new Button("Back to Login");
        backToLoginBtn.setPrefWidth(250);
        backToLoginBtn.setPrefHeight(40);
        backToLoginBtn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #6C63FF; " +
                "-fx-border-width: 2; -fx-text-fill: #6C63FF; -fx-background-radius: 10; -fx-border-radius: 10; " +
                "-fx-font-family: 'Poppins'; -fx-font-size: 14; " +
                "-fx-font-weight: bold; -fx-cursor: hand;");
        backToLoginBtn.setOnAction(e -> {
            LoginView loginView = new LoginView(primaryStage);
            loginView.showLoginScene();
        });


        // Add all to formBox
        formBox.getChildren().addAll(
                title, subtitle, nameField, usernameField, passwordField, passwordVisibleField,
                studentNumberField, studentPhoneNumberField, studentAddressField, roleBox, registerBtn, backToLoginBtn
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

    private void handleRegister() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String phone = studentPhoneNumberField.getText();
        String address = studentAddressField.getText();
        String studentNumber = studentNumberField.getText().trim();
        RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
        String role = selectedRole.getText(); // ini akan "Mahasiswa" atau "Admin"
        StringBuilder message = new StringBuilder();
        message.append("Berikut data yang Anda input:\n\n");
        message.append("Username: ").append(username).append("\n");
//        message.append("Password: ").append(password).append("\n");
        message.append("Role: ").append(role).append("\n");
        if ("Mahasiswa".equals(role)) {
            message.append("Student Number: ").append(studentNumber).append("\n");
        }

// Tampilkan di alert
//        showAlert("Data Pendaftaran", message.toString());


//        if (!password.equals(confirmPassword)) {
//            showAlert("Error", "Password tidak cocok!");
//            return;
//        }

        if ("Mahasiswa".equals(role) && studentNumber.isEmpty()) {
            showAlert("Error", "Nomor mahasiswa harus diisi!");
            return;
        }

        if (UserDAO.isUsernameExists(username)) {
            showAlert("Error", "Username sudah digunakan!");
            return;
        }

        if ("Mahasiswa".equals(role) && UserDAO.isStudentNumberExists(studentNumber)) {
            showAlert("Error", "Nomor mahasiswa sudah terdaftar!");
            return;
        }
        User newUser = new User();
        if (role.equals("Mahasiswa")) {
            if (username.isEmpty() || studentNumber.isEmpty()) {
                showAlert("Error", "Semua field harus diisi!");
                return;
            }
            newUser = new User(username, "", name, "Mahasiswa".equals(role) ? studentNumber : null, role, phone, address);
        } else {
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Semua field harus diisi!");
                return;
            }
            newUser = new User(username, "Admin".equals(role) ? password : null, name, "", role, phone, address);
        }

        try {
            UserDAO.addUser(newUser);
            showAlert("Success", "Registrasi berhasil!");
            new LoginView(primaryStage).showLoginScene();
        } catch (SQLException e) {
            showAlert("Error", "Gagal menyimpan data:\n" + e.getMessage());
            e.printStackTrace(); // agar error tetap muncul di console log
        }


    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 