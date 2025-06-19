package com.example.perpustakaan.views;

import com.example.perpustakaan.models.User;
import com.example.perpustakaan.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private final Stage stage;
    private final AuthService authService;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authService = new AuthService();
    }

    public void show() {
        // Create main container
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: white;");

        // Logo
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        logoView.setFitWidth(200);
        logoView.setPreserveRatio(true);

        // Title
        Label titleLabel = new Label("Perpustakaan Digital");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Login form
        VBox loginForm = new VBox(10);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setMaxWidth(300);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password/Student Number");
        passwordField.setStyle("-fx-padding: 10px;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px;");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 10px 20px;");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        loginForm.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);

        // Add components to main container
        mainContainer.getChildren().addAll(logoView, titleLabel, loginForm);

        // Create scene
        Scene scene = new Scene(mainContainer, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Login - Perpustakaan Digital");
        stage.show();

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
                return;
            }

            User user = authService.login(username, password);
            if (user != null) {
                if (user.getRole().equals("admin")) {
                    new AdminDashboard(stage).show();
                } else {
                    new StudentDashboard(stage).show();
                }
            } else {
                showAlert("Error", "Invalid username or password", Alert.AlertType.ERROR);
            }
        });

        // Register button action
        registerButton.setOnAction(e -> showRegistrationDialog());
    }

    private void showRegistrationDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Register");
        dialog.setHeaderText("Choose registration type");

        ButtonType adminButton = new ButtonType("Admin");
        ButtonType studentButton = new ButtonType("Student");
        ButtonType cancelButton = ButtonType.CANCEL;

        dialog.getDialogPane().getButtonTypes().addAll(adminButton, studentButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == adminButton) {
                showAdminRegistration();
            } else if (dialogButton == studentButton) {
                showStudentRegistration();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAdminRegistration() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Admin Registration");
        dialog.setHeaderText("Enter admin credentials");

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                if (username.getText().isEmpty() || password.getText().isEmpty()) {
                    showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
                    return null;
                }
                if (authService.registerAdmin(username.getText(), password.getText())) {
                    showAlert("Success", "Admin registration successful", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Username already exists", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showStudentRegistration() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Student Registration");
        dialog.setHeaderText("Enter student credentials");

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        TextField studentNumber = new TextField();
        studentNumber.setPromptText("Student Number");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Student Number:"), 0, 1);
        grid.add(studentNumber, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                if (username.getText().isEmpty() || studentNumber.getText().isEmpty()) {
                    showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
                    return null;
                }
                if (authService.registerStudent(username.getText(), studentNumber.getText())) {
                    showAlert("Success", "Student registration successful", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Username or student number already exists", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}