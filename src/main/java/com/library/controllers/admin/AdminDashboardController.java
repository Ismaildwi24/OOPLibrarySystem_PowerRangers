package com.library.controllers.admin;

import com.library.views.admin.AdminDashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminDashboardController extends Application {
    private AdminDashboardView view;

    @Override
    public void start(Stage primaryStage) {
        view = new AdminDashboardView();
        
        // Create scene with gradient background
        Scene scene = new Scene(view.getRoot(), 1400, 800);
        scene.getStylesheets().add("data:text/css," + getCSS());

        primaryStage.setTitle("Library Management System - Admin");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private String getCSS() {
        return """
            .root {
                -fx-font-family: 'Segoe UI', Arial, sans-serif;
            }
            
            .scroll-pane {
                -fx-background-color: transparent;
            }
            
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
            
            .scroll-pane .content {
                -fx-background-color: transparent;
            }
            
            .scroll-bar:vertical {
                -fx-background-color: transparent;
                -fx-pref-width: 8px;
            }
            
            .scroll-bar:horizontal {
                -fx-background-color: transparent;
                -fx-pref-height: 8px;
            }
            
            .scroll-bar .thumb {
                -fx-background-color: #bdc3c7;
                -fx-background-radius: 4px;
            }
            
            .scroll-bar .track {
                -fx-background-color: transparent;
            }
            
            .table-view {
                -fx-background-color: white;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
            }
            
            .table-view .column-header-background {
                -fx-background-color: #34495e;
                -fx-background-radius: 10 10 0 0;
            }
            
            .table-view .column-header {
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 15 10;
            }
            
            .table-view .table-cell {
                -fx-border-color: #ecf0f1;
                -fx-border-width: 0 0 1 0;
                -fx-padding: 12 10;
            }
            
            .table-row-cell:selected {
                -fx-background-color: #3498db;
                -fx-text-fill: white;
            }
            
            .table-row-cell:hover {
                -fx-background-color: #ecf0f1;
            }
            """;
    }

    public static void main(String[] args) {
        launch(args);
    }
} 