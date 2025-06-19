package com.library;

import javafx.application.Application;
import javafx.stage.Stage;
import com.library.views.LoginView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
        loginView.showLoginScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}