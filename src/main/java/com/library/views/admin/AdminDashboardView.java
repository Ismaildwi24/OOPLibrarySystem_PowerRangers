package com.library.views.admin;

import com.library.dao.*;
import com.library.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.library.views.LoginView;
import com.library.utils.CSVHandler;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.StageStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

public class AdminDashboardView {
    private BorderPane root;
    private VBox sidebar;
    private String currentPage = "Dashboard";
    private Label currentPageIndicator;
    private Stage primaryStage;
    private TableView<Book> booksTable;
    private TableView<User> studentsTable;
    private TableView<Borrow> borrowsTable;
    private TableView<Return> returnsTable;
    private TableView<Fine> finesTable;
    private Label dateTimeLabel;
    private User user;
    private TableView<Member> membersTable;
//    private TableView<Book> booksTable;


    public void show(Stage primaryStage, User user) {
        try {
            this.primaryStage = primaryStage;
            System.out.println("Ini user" + user);
            this.user = user; // ⬅️ Simpan ke variabel instance

            // Pastikan root sudah diinisialisasi
            if (root == null) {
                root = new BorderPane();
                sidebar = createSidebar();
                VBox mainContent = createDashboardContent();
                root.setLeft(sidebar);
                root.setCenter(mainContent);
            }

            // Buat scene baru
            Scene scene = new Scene(root);

            // Atur stage
            primaryStage.setTitle("Library Management System - Admin Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);

            // Tampilkan stage
            primaryStage.show();

            System.out.println("Admin dashboard shown successfully");
        } catch (Exception e) {
            System.out.println("Error showing admin dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public AdminDashboardView() {
//        root = new BorderPane();
//        sidebar = createSidebar();
//        VBox mainContent = createDashboardContent();
//        root.setLeft(sidebar);
//        root.setCenter(mainContent);
//
//        // Inisialisasi tabel-tabel
//        booksTable = createBooksTable();
//        studentsTable = createStudentsTable();
//        borrowsTable = createBorrowsTable();
//        returnsTable = createReturnsTable();
//        finesTable = createFinesTable();
//    }

    public BorderPane getRoot() {
        return root;
    }

    public void setCurrentPage(String page) {
        currentPage = page;
        sidebar = createSidebar();
        root.setLeft(sidebar);

        VBox content;
        switch (page) {
            case "Dashboard":
                content = createDashboardContent();
                break;
            case "Members":
                content = createMembersContent();
                break;
            case "Books":
                content = createBooksContent();
                break;
            case "Returns":
                content = createReturnsContent();
                break;
            case "Students":
                content = createStudentsContent();
                break;
            case "Borrows":
                content = createBorrowsContent();
                break;
            case "Fines":
                content = createFinesContent();
                break;
            default:
                content = createDashboardContent();
        }
        root.setCenter(content);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));

        // Gradient background for sidebar
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#8B0000")), new Stop(0.5, Color.web("#CD5C5C")), new Stop(1, Color.web("#B22222"))};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        sidebar.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        // Logo section
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);

        Label logoIcon = new Label("📚");
        logoIcon.setStyle("-fx-font-size: 40px;");

        Label logoText = new Label("LibraryPro");
        logoText.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        logoSection.getChildren().addAll(logoIcon, logoText);

        // Menu items
        VBox menuSection = new VBox(8);
        menuSection.setPadding(new Insets(30, 0, 0, 0));

        String[] menuItems = {"🏠 Dashboard", "👥 Members", "📖 Books", "🔄 Returns", "🚪 Logout"};

        for (String item : menuItems) {
            Label menuItem = createMenuItem(item);
            menuSection.getChildren().add(menuItem);
        }

        sidebar.getChildren().addAll(logoSection, menuSection);
        return sidebar;
    }

    private Label createMenuItem(String text) {
        Label item = new Label(text);
        String baseStyle = "-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 20; " + "-fx-background-radius: 8; -fx-cursor: hand;";

        // Check if this is the current page
        String pageName = text.split(" ", 2)[1]; // Get text after emoji
        if (pageName.equals(currentPage)) {
            item.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.3);");
        } else {
            item.setStyle(baseStyle);
        }

        item.setPrefWidth(240);

        // Hover effect
        item.setOnMouseEntered(e -> {
            if (!pageName.equals(currentPage)) {
                item.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.2);");
            }
            ScaleTransition st = new ScaleTransition(Duration.millis(100), item);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        item.setOnMouseExited(e -> {
            if (pageName.equals(currentPage)) {
                item.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.3);");
            } else {
                item.setStyle(baseStyle);
            }
            ScaleTransition st = new ScaleTransition(Duration.millis(100), item);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Click event for navigation
        item.setOnMouseClicked(e -> {
            if (pageName.equals("Logout")) {
                new LoginView(primaryStage).showLoginScene();
            } else {
                setCurrentPage(pageName);
            }
        });

        return item;
    }

    private VBox createDashboardContent() {
        membersTable = createMembersTable(); // ⬅️ inisialisasi di sini
        booksTable = createBooksTable(); // ⬅️ inisialisasi di sini

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        String displayName = "Dendy"; // atau getUsername()
        HBox header = createHeader("Dashboard " + user.getRole(), "Selamat datang, " + displayName + "! Ini adalah dashboard milikmu");


        // Stats cards
        HBox statsSection = createStatsSection();

        // Main dashboard content
        VBox dashboardContent = createMainDashboardContent();

        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, statsSection, scrollPane);
        return mainContent;
    }

    private VBox createMembersContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Member Management", "Kelola data anggota perpustakaan");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

//        Button addButton = createActionButton("➕ Add Member", "#27ae60");
        Button editButton = createActionButton("✏️ Edit Member", "#3498db");
        Button deleteButton = createActionButton("🗑️ Delete Member", "#e74c3c");
        Button searchButton = createActionButton("🔍 Search", "#9b59b6");

        actionButtons.getChildren().addAll(editButton, deleteButton, searchButton);

        // Members table
        membersTable = createMembersTable();

        ScrollPane scrollPane = new ScrollPane(membersTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane, membersTable);
        return mainContent;
    }

    private VBox createBooksContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Book Management", "Kelola koleksi buku perpustakaan");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("➕ Add Book", "#27ae60");
        Button editButton = createActionButton("✏️ Edit Book", "#3498db");
        Button deleteButton = createActionButton("🗑️ Delete Book", "#e74c3c");
        Button issueButton = createActionButton("📤 Book Detail", "#f39c12");
        Button searchButton = createActionButton("🔍 Search Book", "#9b59b6");

        actionButtons.getChildren().addAll(addButton, editButton, deleteButton, issueButton, searchButton);

        // Books table
        refreshBooksTable();
        booksTable = createBooksTable();

        ScrollPane scrollPane = new ScrollPane(booksTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane, booksTable);
        return mainContent;
    }

    private VBox createReturnsContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Return Management", "Kelola pengembalian buku");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

//        Button addButton = createActionButton("➕ Add Return", "#27ae60");
//        Button editButton = createActionButton("✏️ Edit Return", "#3498db");
//        Button deleteButton = createActionButton("🗑️ Delete Return", "#e74c3c");

//        actionButtons.getChildren().addAll(addButton, editButton, deleteButton);

        // Returns table
        returnsTable = createReturnsTable();
        refreshReturnsTable();

        ScrollPane scrollPane = new ScrollPane(returnsTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane);
        return mainContent;
    }

    private VBox createStudentsContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Student Management", "Kelola data mahasiswa");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("➕ Add Student", "#27ae60");
        Button editButton = createActionButton("✏️ Edit Student", "#3498db");
        Button deleteButton = createActionButton("🗑️ Delete Student", "#e74c3c");

        actionButtons.getChildren().addAll(addButton, editButton, deleteButton);

        // Students table
        studentsTable = createStudentsTable();
        refreshStudentsTable();

        ScrollPane scrollPane = new ScrollPane(studentsTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane);
        return mainContent;
    }

    private VBox createBorrowsContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Borrow Management", "Kelola peminjaman buku");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("➕ Add Borrow", "#27ae60");
        Button editButton = createActionButton("✏️ Edit Borrow", "#3498db");
        Button deleteButton = createActionButton("🗑️ Delete Borrow", "#e74c3c");

        actionButtons.getChildren().addAll(addButton, editButton, deleteButton);

        // Borrows table
        borrowsTable = createBorrowsTable();
        refreshBorrowsTable();

        ScrollPane scrollPane = new ScrollPane(borrowsTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane);
        return mainContent;
    }

    private VBox createFinesContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader("Fine Management", "Kelola denda keterlambatan");

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        actionButtons.setPadding(new Insets(0, 0, 20, 0));

        Button addButton = createActionButton("➕ Add Fine", "#27ae60");
        Button editButton = createActionButton("✏️ Edit Fine", "#3498db");
        Button deleteButton = createActionButton("🗑️ Delete Fine", "#e74c3c");
        Button calculateButton = createActionButton("💰 Calculate Fine", "#f39c12");

        actionButtons.getChildren().addAll(addButton, editButton, deleteButton, calculateButton);

        // Fines table
        finesTable = createFinesTable();
        refreshFinesTable();

        ScrollPane scrollPane = new ScrollPane(finesTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainContent.getChildren().addAll(header, actionButtons, scrollPane);
        return mainContent;
    }

    private TableView<Member> createMembersTable() {
        TableView<Member> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Buat kolom
        TableColumn<Member, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Member, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Member, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(200);

        TableColumn<Member, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(120);

        TableColumn<Member, String> adressCol = new TableColumn<>("Address");
        adressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        adressCol.setPrefWidth(120);

        TableColumn<Member, String> joinCol = new TableColumn<>("Join Date");
        joinCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        joinCol.setPrefWidth(200);

        table.getColumns().addAll(idCol, nameCol, usernameCol, phoneCol, adressCol, joinCol);

        try {
            List<Member> membersFromDB = MemberDAO.getAllMembers();
            table.setItems(FXCollections.observableArrayList(membersFromDB));
        } catch (SQLException e) {
            e.printStackTrace();
            // Kalau mau: tampilkan alert juga
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal memuat data member dari database:\n" + e.getMessage());
            alert.showAndWait();
        }

        return table;
    }


    private TableView<Book> createBooksTable() {
        TableView<Book> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Create columns
        TableColumn<Book, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(120);

        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryCol.setPrefWidth(120);

        TableColumn<Book, String> statusCol = new TableColumn<>("Available");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        statusCol.setPrefWidth(100);

        TableColumn<Book, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        qtyCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, categoryCol, statusCol, qtyCol);

        try {
            List<Book> booksFromDB = BookDAO.getAllBooksWithAvailability();
            System.out.println(booksFromDB);
            table.setItems(FXCollections.observableArrayList(booksFromDB));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal memuat data buku:\n" + e.getMessage());
            alert.showAndWait();
        }
        return table;
    }

    private TableView<Return> createReturnsTable() {
        TableView<Return> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Kolom data
        TableColumn<Return, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("no"));

        TableColumn<Return, String> borrowIdCol = new TableColumn<>("Borrow Date");
        borrowIdCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<Return, String> studentIdCol = new TableColumn<>("Student Name");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<Return, String> borrowDueCol = new TableColumn<>("Due Date");
        borrowDueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Return, String> bookIdCol = new TableColumn<>("Book Name");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<Return, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Return, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Return, Double> fineCol = new TableColumn<>("Fine");
        fineCol.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Kolom aksi
        TableColumn<Return, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Return");

            {
                btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 8;");
                btn.setOnAction(e -> {
                    Return selected = getTableView().getItems().get(getIndex());

                    TextInputDialog dialog = new TextInputDialog("good");
                    dialog.setTitle("Konfirmasi Pengembalian");
                    dialog.setHeaderText("Masukkan kondisi buku:");
                    dialog.setContentText("Kondisi (good/damaged):");
                    dialog.showAndWait().ifPresent(condition -> {
                        try {
                            LocalDate today = LocalDate.now();
                            double fine = 0;

                            if (today.isAfter(selected.getDueDate())) {
                                long daysLate = ChronoUnit.DAYS.between(selected.getDueDate(), today);
                                fine = daysLate * 1000; // Rp1000 per hari
                            }

                            ReturnDAO.returnBook(selected.getBorrowId(), selected.getBookItemId(), condition, today);

                            if (fine > 0) {
                                FineDAO.createFine(selected.getBorrowId(), fine);
                            }

                            showAlert("Sukses", "Buku berhasil dikembalikan." + (fine > 0 ? "\nDenda: Rp" + fine : ""));
                            refreshReturnsTablez(table);

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            showAlert("Error", "Gagal mengembalikan buku:\n" + ex.getMessage());
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getReturnDate() != null) {
                    setGraphic(null); // Jangan tampilkan tombol jika sudah dikembalikan
                } else {
                    setGraphic(btn);
                }
            }
        });

        table.getColumns().addAll(idCol, borrowIdCol, studentIdCol, borrowDueCol, bookIdCol, returnDateCol, conditionCol, fineCol, actionCol);
        refreshReturnsTablez(table);
        return table;
    }

    private void refreshReturnsTablez(TableView<Return> table) {
        try {
            List<Return> returns = ReturnDAO.getAllReturnHistory(); // atau getPendingReturns() jika hanya menampilkan yang belum dikembalikan
            table.setItems(FXCollections.observableArrayList(returns));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data pengembalian:\n" + e.getMessage());
        }
    }

    private TableView<User> createStudentsTable() {
        TableView<User> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Create columns
        TableColumn<User, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nimCol.setPrefWidth(100);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(150);

        TableColumn<User, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(200);

        table.getColumns().addAll(nimCol, nameCol, emailCol, phoneCol, addressCol);
        return table;
    }

    private TableView<Borrow> createBorrowsTable() {
        TableView<Borrow> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Create columns
        TableColumn<Borrow, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Borrow, String> bookIdCol = new TableColumn<>("Book ID");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookIdCol.setPrefWidth(100);

        TableColumn<Borrow, String> studentIdCol = new TableColumn<>("Student ID");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdCol.setPrefWidth(100);

        TableColumn<Borrow, LocalDate> borrowDateCol = new TableColumn<>("Borrow Date");
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        borrowDateCol.setPrefWidth(120);

        TableColumn<Borrow, LocalDate> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateCol.setPrefWidth(120);

        TableColumn<Borrow, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(120);

        TableColumn<Borrow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, bookIdCol, studentIdCol, borrowDateCol, dueDateCol, returnDateCol, statusCol);
        return table;
    }

    private TableView<Fine> createFinesTable() {
        TableView<Fine> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setPrefHeight(500);

        // Create columns
        TableColumn<Fine, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Fine, String> borrowIdCol = new TableColumn<>("Borrow ID");
        borrowIdCol.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        borrowIdCol.setPrefWidth(100);

        TableColumn<Fine, String> studentIdCol = new TableColumn<>("Student ID");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentIdCol.setPrefWidth(100);

        TableColumn<Fine, LocalDate> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateCol.setPrefWidth(120);

        TableColumn<Fine, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(120);

        TableColumn<Fine, Integer> daysLateCol = new TableColumn<>("Days Late");
        daysLateCol.setCellValueFactory(new PropertyValueFactory<>("daysLate"));
        daysLateCol.setPrefWidth(100);

        TableColumn<Fine, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(100);

        TableColumn<Fine, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, borrowIdCol, studentIdCol, dueDateCol, returnDateCol, daysLateCol, amountCol, statusCol);
        return table;
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; " + "-fx-text-fill: white; " + "-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-padding: 10 20; " + "-fx-background-radius: 8; " + "-fx-cursor: hand;");
        // Hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: derive(" + color + ", -10%); " + "-fx-text-fill: white; " + "-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-padding: 10 20; " + "-fx-background-radius: 8; " + "-fx-cursor: hand;");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + color + "; " + "-fx-text-fill: white; " + "-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-padding: 10 20; " + "-fx-background-radius: 8; " + "-fx-cursor: hand;");
        });
        // Action handler
        button.setOnAction(e -> {
            switch (text) {
//                case "➕ Add Member":
//                    handleAddMember();
//                    break;
                case "✏️ Edit Member":
                    handleEditMember();
                    break;
                case "🗑️ Delete Member":
                    handleDeleteMember();
                    break;
                case "🔍 Search":
                    handleSearchMember();
                    break;
                case "➕ Add Book":
                    handleAddBook();
                    break;
                case "✏️ Edit Book":
                    handleEditBook();
                    break;
                case "🗑️ Delete Book":
                    handleDeleteBook();
                    break;
                case "📤 Book Detail":
                    handleIssueBook();
                    break;
                case "🔍 Search Book":
                    handleSearchBook();
                    break;
                case "✅ Process Return":
                    handleProcessReturn();
                    break;
                case "⚠️ View Overdue":
                    handleViewOverdue();
                    break;
                case "💰 Manage Fines":
                    handleManageFines();
                    break;
                case "📊 Generate Report":
                    handleGenerateReport();
                    break;
                case "➕ Add Student":
                    handleAddStudent();
                    break;
                case "✏️ Edit Student":
                    handleEditStudent();
                    break;
                case "🗑️ Delete Student":
                    handleDeleteStudent();
                    break;
                case "➕ Add Borrow":
                    handleAddBorrow();
                    break;
                case "✏️ Edit Borrow":
                    handleEditBorrow();
                    break;
                case "🗑️ Delete Borrow":
                    handleDeleteBorrow();
                    break;
//                case "➕ Add Return":
//                    handleAddReturn();
//                    break;
//                case "✏️ Edit Return":
//                    handleEditReturn();
//                    break;
//                case "🗑️ Delete Return":
//                    handleDeleteReturn();
//                    break;
                case "➕ Add Fine":
                    handleAddFine();
                    break;
                case "✏️ Edit Fine":
                    handleEditFine();
                    break;
                case "🗑️ Delete Fine":
                    handleDeleteFine();
                    break;
                case "💰 Calculate Fine":
                    handleCalculateFine();
                    break;
                default:
                    showActionAlert(text);
            }
        });
        return button;
    }

    // Handler methods for each action
    private void handleAddMember() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Member");
        dialog.setHeaderText("Tambah Member Baru");
        dialog.setContentText("Nama Member:");
        dialog.showAndWait().ifPresent(name -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Member '" + name + "' berhasil ditambahkan (dummy).", ButtonType.OK);
            alert.showAndWait();
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleEditMember() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Selected item: " + membersTable.getSelectionModel().getSelectedItem());
            showAlert("Peringatan", "Pilih member yang ingin diedit terlebih dahulu.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Edit Member");
        dialog.setHeaderText("Edit Nama Member");
        dialog.setContentText("Nama Member Baru:");

        dialog.showAndWait().ifPresent(newName -> {
            try {
                // Update di database
                MemberDAO.updateMemberName(selected.getId(), newName);

                // Update lokal & refresh table
                selected.setName(newName);
                membersTable.refresh();

                showAlert("Sukses", "Nama member berhasil diubah.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal mengubah nama member:\n" + e.getMessage());
            }
        });
    }

    private void handleDeleteMember() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Peringatan", "Pilih member yang ingin dihapus terlebih dahulu.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Konfirmasi Hapus");
        confirmDialog.setHeaderText("Apakah Anda yakin ingin menghapus member?");
        confirmDialog.setContentText("Nama: " + selected.getName());

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Hapus dari database
                    MemberDAO.deleteMemberById(selected.getId());

                    // Hapus dari TableView
                    membersTable.getItems().remove(selected);

                    showAlert("Sukses", "Member berhasil dihapus.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Gagal menghapus member:\n" + e.getMessage());
                }
            }
        });
    }

    private void handleSearchMember() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Member");
        dialog.setHeaderText("Cari Member");
        dialog.setContentText("Masukkan nama member:");

        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert("Peringatan", "Nama tidak boleh kosong!");
                return;
            }
            try {
                List<Member> foundMembers = MemberDAO.searchMembersByName(name);
                if (foundMembers.isEmpty()) {
                    showAlert("Info", "Tidak ditemukan member dengan nama: " + name);
                } else {
                    membersTable.setItems(FXCollections.observableArrayList(foundMembers));
                    showAlert("Sukses", "Ditemukan " + foundMembers.size() + " member dengan nama mengandung: '" + name + "'");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal mencari member:\n" + e.getMessage());
            }
        });
    }

    private void handleAddBook() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");
        dialog.setHeaderText("Tambah Buku Baru");

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        try {
            List<Category> categories = CategoryDAO.getAllCategories();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));

            // Optional: pilih default jika edit
//            for (Category cat : categories) {
//                if (cat.getName().equalsIgnoreCase(selected.getCategoryName())) {
//                    categoryComboBox.getSelectionModel().select(cat);
//                    break;
//                }
//            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat kategori:\n" + e.getMessage());
        }

        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField publisherField = new TextField();
        publisherField.setPromptText("Publisher");
        TextField yearField = new TextField();
        yearField.setPromptText("Year Published (yyyy)");
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        TextField categoryIdField = new TextField();
        categoryIdField.setPromptText("Category ID");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Publisher:"), 0, 2);
        grid.add(publisherField, 1, 2);
        grid.add(new Label("Year Published:"), 0, 3);
        grid.add(yearField, 1, 3);
        grid.add(new Label("ISBN:"), 0, 4);
        grid.add(isbnField, 1, 4);
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryComboBox, 1, 5);
        grid.add(new Label("Quantity:"), 0, 6);
        grid.add(qtyField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                try {
                    int available = 0;
                    int quantity = Integer.parseInt(qtyField.getText());
                    int year = Integer.parseInt(yearField.getText());
                    Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
                    int categoryId = selectedCategory != null ? selectedCategory.getId() : -1;

                    return new Book(null, // ID auto-increment
                            titleField.getText(), authorField.getText(), publisherField.getText(), year, isbnField.getText(), categoryId, "", available, quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "Pastikan semua input valid: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                int bookId = BookDAO.insertBook(book); // Simpan buku dan dapatkan ID
                System.out.println("BookID" + bookId);
                BookDAO.insertBookItems(bookId, book.getQuantity()); // Tambahkan item buku

                showAlert("Sukses", "Buku berhasil ditambahkan!");
                booksTable.refresh();

                refreshBooksTable(); // reload tabel
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal menyimpan buku:\n" + e.getMessage());
            }
        });
    }

    private void handleEditBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih buku yang ingin diedit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        try {
            List<Category> categories = CategoryDAO.getAllCategories();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));

            // Optional: pilih default jika edit
            for (Category cat : categories) {
                if (cat.getName().equalsIgnoreCase(selected.getCategoryName())) {
                    categoryComboBox.getSelectionModel().select(cat);
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat kategori:\n" + e.getMessage());
        }

        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Edit Data Buku");
        ButtonType editBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(selected.getId());
        idField.setDisable(true);
        TextField titleField = new TextField(selected.getTitle());
        TextField authorField = new TextField(selected.getAuthor());
        TextField publisherField = new TextField(selected.getPublisher());
        TextField isbnField = new TextField(selected.getIsbn());
        TextField yearField = new TextField(String.valueOf(selected.getYear()));
        TextField categoryNameField = new TextField(selected.getCategoryName());

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("ISBN:"), 0, 4);
        grid.add(isbnField, 1, 4);
        grid.add(new Label("Year Published:"), 0, 5);
        grid.add(yearField, 1, 5);
        grid.add(new Label("Category:"), 0, 6);
        grid.add(categoryComboBox, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editBtnType) {
                try {
                    Category selectedCategory = categoryComboBox.getValue();
                    if (selectedCategory == null) return null;

                    return new Book(idField.getText(), titleField.getText(), authorField.getText(), publisherField.getText(), Integer.parseInt(yearField.getText()), isbnField.getText(), selectedCategory.getId(), selectedCategory.getName(), selected.getStatus(), selected.getQuantity());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });


        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                BookDAO.updateBook(book);  // DAO method untuk update ke database
                refreshBooksTable();
                showAlert("Sukses", "Buku berhasil diperbarui.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal memperbarui buku:\n" + e.getMessage());
            }
        });
    }

    private void handleDeleteBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih buku yang ingin dihapus.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus buku '" + selected.getTitle() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                BookDAO.deleteBook(selected.getId());
                refreshBooksTable(); // refresh agar tabel update
                showAlert("Sukses", "Buku berhasil dihapus.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal menghapus buku:\n" + e.getMessage());
            }
        }
    }

    private void handleIssueBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Peringatan", "Pilih buku terlebih dahulu untuk melihat item.");
            return;
        }

        try {
            List<BookItem> items = BookDAO.getBookItemsByBookId(selected.getId());

            if (items.isEmpty()) {
                showAlert("Informasi", "Tidak ada item untuk buku ini.");
                return;
            }

            StringBuilder detail = new StringBuilder("Daftar Book Items untuk buku: " + selected.getTitle() + "\n\n");
            for (BookItem item : items) {
                detail.append("Kode: ").append(item.getInventoryCode()).append("\nKondisi: ").append(item.getCondition()).append("\nTersedia: ").append(item.isAvailable() ? "Ya" : "Tidak").append("\n\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, detail.toString(), ButtonType.OK);
            alert.setTitle("Book Items");
            alert.setHeaderText("Detail Book Items");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengambil data book items:\n" + e.getMessage());
        }
    }

    private void handleSearchBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Book");
        dialog.setHeaderText("Cari Buku");
        dialog.setContentText("Masukkan judul buku:");

        dialog.showAndWait().ifPresent(title -> {
            if (title.trim().isEmpty()) {
                showAlert("Peringatan", "Judul tidak boleh kosong!");
                return;
            }
            try {
                List<Book> foundBooks = BookDAO.searchBooksByTitle(title);
                if (foundBooks.isEmpty()) {
                    showAlert("Info", "Tidak ditemukan buku dengan judul: " + title);
                } else {
                    booksTable.setItems(FXCollections.observableArrayList(foundBooks));
                    showAlert("Sukses", "Ditemukan " + foundBooks.size() + " buku dengan judul mengandung: '" + title + "'");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal mencari buku:\n" + e.getMessage());
            }
        });
    }


    private void handleProcessReturn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pengembalian buku berhasil diproses (dummy).", ButtonType.OK);
        alert.showAndWait();
    }

    private void handleViewOverdue() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Menampilkan daftar buku yang terlambat (dummy).", ButtonType.OK);
        alert.showAndWait();
    }

    private void handleManageFines() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Manajemen denda berhasil (dummy).", ButtonType.OK);
        alert.showAndWait();
    }

    private void handleGenerateReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Laporan berhasil digenerate (dummy).", ButtonType.OK);
        alert.showAndWait();
    }

    private void handleAddStudent() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Tambah Mahasiswa Baru");
        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nimField = new TextField();
        nimField.setPromptText("NIM");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        grid.add(new Label("NIM:"), 0, 0);
        grid.add(nimField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                return new User(nimField.getText(), "password123", // Default password
                        nameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(), "Mahasiswa");
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(student -> {
            CSVHandler.addStudent(student);
            refreshStudentsTable();
        });
    }

    private void handleEditStudent() {
        User selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih mahasiswa yang ingin diedit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit Student");
        dialog.setHeaderText("Edit Data Mahasiswa");
        ButtonType editBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nimField = new TextField(selected.getUsername());
        nimField.setDisable(true);
        TextField nameField = new TextField(selected.getName());
        TextField emailField = new TextField(selected.getEmail());
        TextField phoneField = new TextField(selected.getPhone());
        TextField addressField = new TextField(selected.getAddress());

        grid.add(new Label("NIM:"), 0, 0);
        grid.add(nimField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editBtnType) {
                return new User(nimField.getText(), selected.getPassword(), nameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(), selected.getRole());
            }
            return null;
        });
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(student -> {
            CSVHandler.updateStudent(student);
            refreshStudentsTable();
        });
    }

    private void handleDeleteStudent() {
        User selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih mahasiswa yang ingin dihapus.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus mahasiswa '" + selected.getName() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            CSVHandler.deleteStudent(selected.getUsername());
            refreshStudentsTable();
        }
    }

    private void handleAddBorrow() {
        Dialog<Borrow> dialog = new Dialog<>();
        dialog.setTitle("Add Borrow");
        dialog.setHeaderText("Tambah Peminjaman Baru");
        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");
        DatePicker borrowDatePicker = new DatePicker(LocalDate.now());
        DatePicker dueDatePicker = new DatePicker(LocalDate.now().plusDays(7));
        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setDisable(true);
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("BORROWED", "RETURNED", "OVERDUE"));
        statusCombo.setValue("BORROWED");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Student ID:"), 0, 2);
        grid.add(studentIdField, 1, 2);
        grid.add(new Label("Borrow Date:"), 0, 3);
        grid.add(borrowDatePicker, 1, 3);
        grid.add(new Label("Due Date:"), 0, 4);
        grid.add(dueDatePicker, 1, 4);
        grid.add(new Label("Return Date:"), 0, 5);
        grid.add(returnDatePicker, 1, 5);
        grid.add(new Label("Status:"), 0, 6);
        grid.add(statusCombo, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                return new Borrow(idField.getText(), bookIdField.getText(), studentIdField.getText(), borrowDatePicker.getValue(), dueDatePicker.getValue(), null, statusCombo.getValue());
            }
            return null;
        });
        Optional<Borrow> result = dialog.showAndWait();
        result.ifPresent(borrow -> {
            CSVHandler.addBorrow(borrow);
            refreshBorrowsTable();
        });
    }

    private void handleEditBorrow() {
        Borrow selected = borrowsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih peminjaman yang ingin diedit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Dialog<Borrow> dialog = new Dialog<>();
        dialog.setTitle("Edit Borrow");
        dialog.setHeaderText("Edit Data Peminjaman");
        ButtonType editBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(selected.getId());
        idField.setDisable(true);
        TextField bookIdField = new TextField(selected.getBookId());
        bookIdField.setDisable(true);
        TextField studentIdField = new TextField(selected.getStudentId());
        studentIdField.setDisable(true);
        DatePicker borrowDatePicker = new DatePicker(selected.getBorrowDate());
        borrowDatePicker.setDisable(true);
        DatePicker dueDatePicker = new DatePicker(selected.getDueDate());
        dueDatePicker.setDisable(true);
        DatePicker returnDatePicker = new DatePicker(selected.getReturnDate());
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("BORROWED", "RETURNED", "OVERDUE"));
        statusCombo.setValue(selected.getStatus());

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Student ID:"), 0, 2);
        grid.add(studentIdField, 1, 2);
        grid.add(new Label("Borrow Date:"), 0, 3);
        grid.add(borrowDatePicker, 1, 3);
        grid.add(new Label("Due Date:"), 0, 4);
        grid.add(dueDatePicker, 1, 4);
        grid.add(new Label("Return Date:"), 0, 5);
        grid.add(returnDatePicker, 1, 5);
        grid.add(new Label("Status:"), 0, 6);
        grid.add(statusCombo, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editBtnType) {
                return new Borrow(idField.getText(), bookIdField.getText(), studentIdField.getText(), borrowDatePicker.getValue(), dueDatePicker.getValue(), returnDatePicker.getValue(), statusCombo.getValue());
            }
            return null;
        });
        Optional<Borrow> result = dialog.showAndWait();
        result.ifPresent(borrow -> {
            CSVHandler.updateBorrow(borrow);
            refreshBorrowsTable();
        });
    }

    private void handleDeleteBorrow() {
        Borrow selected = borrowsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih peminjaman yang ingin dihapus.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus peminjaman ID '" + selected.getId() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            CSVHandler.deleteBorrow(selected.getId());
            refreshBorrowsTable();
        }
    }

//    private void handleAddReturn() {
//        Dialog<Return> dialog = new Dialog<>();
//        dialog.setTitle("Add Return");
//        dialog.setHeaderText("Tambah Pengembalian Baru");
//        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));
//
//        TextField idField = new TextField();
//        idField.setPromptText("ID");
//        TextField borrowIdField = new TextField();
//        borrowIdField.setPromptText("Borrow ID");
//        TextField bookIdField = new TextField();
//        bookIdField.setPromptText("Book ID");
//        TextField studentIdField = new TextField();
//        studentIdField.setPromptText("Student ID");
//        DatePicker returnDatePicker = new DatePicker(LocalDate.now());
//        ComboBox<String> conditionCombo = new ComboBox<>(FXCollections.observableArrayList("GOOD", "DAMAGED", "LOST"));
//        conditionCombo.setValue("GOOD");
//        TextField fineField = new TextField("0.0");
//        fineField.setPromptText("Fine");
//
//        grid.add(new Label("ID:"), 0, 0);
//        grid.add(idField, 1, 0);
//        grid.add(new Label("Borrow ID:"), 0, 1);
//        grid.add(borrowIdField, 1, 1);
//        grid.add(new Label("Book ID:"), 0, 2);
//        grid.add(bookIdField, 1, 2);
//        grid.add(new Label("Student ID:"), 0, 3);
//        grid.add(studentIdField, 1, 3);
//        grid.add(new Label("Return Date:"), 0, 4);
//        grid.add(returnDatePicker, 1, 4);
//        grid.add(new Label("Condition:"), 0, 5);
//        grid.add(conditionCombo, 1, 5);
//        grid.add(new Label("Fine:"), 0, 6);
//        grid.add(fineField, 1, 6);
//
//        dialog.getDialogPane().setContent(grid);
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == addBtnType) {
//                try {
//                    return new Return(
//                            idField.getText(),
//                            borrowIdField.getText(),
//                            bookIdField.getText(),
//                            studentIdField.getText(),
//                            returnDatePicker.getValue(),
//                            conditionCombo.getValue(),
//                            Double.parseDouble(fineField.getText())
//                    );
//                } catch (NumberFormatException e) {
//                    return null;
//                }
//            }
//            return null;
//        });
//        Optional<Return> result = dialog.showAndWait();
//        result.ifPresent(ret -> {
//            CSVHandler.addReturn(ret);
//            refreshReturnsTable();
//        });
//    }

//    private void handleEditReturn() {
//        Return selected = returnsTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih pengembalian yang ingin diedit.", ButtonType.OK);
//            alert.showAndWait();
//            return;
//        }
//        Dialog<Return> dialog = new Dialog<>();
//        dialog.setTitle("Edit Return");
//        dialog.setHeaderText("Edit Data Pengembalian");
//        ButtonType editBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, ButtonType.CANCEL);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 150, 10, 10));
//
//        TextField idField = new TextField(selected.getId());
//        idField.setDisable(true);
//        TextField borrowIdField = new TextField(selected.getBorrowId());
//        borrowIdField.setDisable(true);
//        TextField bookIdField = new TextField(selected.getBookId());
//        bookIdField.setDisable(true);
//        TextField studentIdField = new TextField(selected.getStudentId());
//        studentIdField.setDisable(true);
//        DatePicker returnDatePicker = new DatePicker(selected.getReturnDate());
//        ComboBox<String> conditionCombo = new ComboBox<>(FXCollections.observableArrayList("GOOD", "DAMAGED", "LOST"));
//        conditionCombo.setValue(selected.getCondition());
//        TextField fineField = new TextField(String.valueOf(selected.getFine()));
//
//        grid.add(new Label("ID:"), 0, 0);
//        grid.add(idField, 1, 0);
//        grid.add(new Label("Borrow ID:"), 0, 1);
//        grid.add(borrowIdField, 1, 1);
//        grid.add(new Label("Book ID:"), 0, 2);
//        grid.add(bookIdField, 1, 2);
//        grid.add(new Label("Student ID:"), 0, 3);
//        grid.add(studentIdField, 1, 3);
//        grid.add(new Label("Return Date:"), 0, 4);
//        grid.add(returnDatePicker, 1, 4);
//        grid.add(new Label("Condition:"), 0, 5);
//        grid.add(conditionCombo, 1, 5);
//        grid.add(new Label("Fine:"), 0, 6);
//        grid.add(fineField, 1, 6);
//
//        dialog.getDialogPane().setContent(grid);
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == editBtnType) {
//                try {
//                    return new Return(
//                            idField.getText(),
//                            borrowIdField.getText(),
//                            bookIdField.getText(),
//                            studentIdField.getText(),
//                            returnDatePicker.getValue(),
//                            conditionCombo.getValue(),
//                            Double.parseDouble(fineField.getText())
//                    );
//                } catch (NumberFormatException e) {
//                    return null;
//                }
//            }
//            return null;
//        });
//        Optional<Return> result = dialog.showAndWait();
//        result.ifPresent(ret -> {
//            CSVHandler.updateReturn(ret);
//            refreshReturnsTable();
//        });
//    }

//    private void handleDeleteReturn() {
//        Return selected = returnsTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih pengembalian yang ingin dihapus.", ButtonType.OK);
//            alert.showAndWait();
//            return;
//        }
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus pengembalian ID '" + selected.getId() + "'?", ButtonType.YES, ButtonType.NO);
//        Optional<ButtonType> result = confirm.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.YES) {
//            CSVHandler.deleteReturn(selected.getId());
//            refreshReturnsTable();
//        }
//    }

    private void handleAddFine() {
        Dialog<Fine> dialog = new Dialog<>();
        dialog.setTitle("Add Fine");
        dialog.setHeaderText("Tambah Denda Baru");
        ButtonType addBtnType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField borrowIdField = new TextField();
        borrowIdField.setPromptText("Borrow ID");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");
        DatePicker dueDatePicker = new DatePicker();
        DatePicker returnDatePicker = new DatePicker();
        TextField daysLateField = new TextField("0");
        daysLateField.setPromptText("Days Late");
        TextField amountField = new TextField("0.0");
        amountField.setPromptText("Amount");
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("UNPAID", "PAID"));
        statusCombo.setValue("UNPAID");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Borrow ID:"), 0, 1);
        grid.add(borrowIdField, 1, 1);
        grid.add(new Label("Student ID:"), 0, 2);
        grid.add(studentIdField, 1, 2);
        grid.add(new Label("Due Date:"), 0, 3);
        grid.add(dueDatePicker, 1, 3);
        grid.add(new Label("Return Date:"), 0, 4);
        grid.add(returnDatePicker, 1, 4);
        grid.add(new Label("Days Late:"), 0, 5);
        grid.add(daysLateField, 1, 5);
        grid.add(new Label("Amount:"), 0, 6);
        grid.add(amountField, 1, 6);
        grid.add(new Label("Status:"), 0, 7);
        grid.add(statusCombo, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addBtnType) {
                try {
                    return new Fine(idField.getText(), borrowIdField.getText(), studentIdField.getText(), dueDatePicker.getValue(), returnDatePicker.getValue(), Integer.parseInt(daysLateField.getText()), Double.parseDouble(amountField.getText()), statusCombo.getValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        Optional<Fine> result = dialog.showAndWait();
        result.ifPresent(fine -> {
            CSVHandler.addFine(fine);
            refreshFinesTable();
        });
    }

    private void handleEditFine() {
        Fine selected = finesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih denda yang ingin diedit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Dialog<Fine> dialog = new Dialog<>();
        dialog.setTitle("Edit Fine");
        dialog.setHeaderText("Edit Data Denda");
        ButtonType editBtnType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(selected.getId());
        idField.setDisable(true);
        TextField borrowIdField = new TextField(selected.getBorrowId());
        borrowIdField.setDisable(true);
        TextField studentIdField = new TextField(selected.getStudentId());
        studentIdField.setDisable(true);
        DatePicker dueDatePicker = new DatePicker(selected.getDueDate());
        dueDatePicker.setDisable(true);
        DatePicker returnDatePicker = new DatePicker(selected.getReturnDate());
        returnDatePicker.setDisable(true);
        TextField daysLateField = new TextField(String.valueOf(selected.getDaysLate()));
        daysLateField.setDisable(true);
        TextField amountField = new TextField(String.valueOf(selected.getAmount()));
        amountField.setDisable(true);
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("UNPAID", "PAID"));
        statusCombo.setValue(selected.getStatus());

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Borrow ID:"), 0, 1);
        grid.add(borrowIdField, 1, 1);
        grid.add(new Label("Student ID:"), 0, 2);
        grid.add(studentIdField, 1, 2);
        grid.add(new Label("Due Date:"), 0, 3);
        grid.add(dueDatePicker, 1, 3);
        grid.add(new Label("Return Date:"), 0, 4);
        grid.add(returnDatePicker, 1, 4);
        grid.add(new Label("Days Late:"), 0, 5);
        grid.add(daysLateField, 1, 5);
        grid.add(new Label("Amount:"), 0, 6);
        grid.add(amountField, 1, 6);
        grid.add(new Label("Status:"), 0, 7);
        grid.add(statusCombo, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editBtnType) {
                return new Fine(idField.getText(), borrowIdField.getText(), studentIdField.getText(), dueDatePicker.getValue(), returnDatePicker.getValue(), Integer.parseInt(daysLateField.getText()), Double.parseDouble(amountField.getText()), statusCombo.getValue());
            }
            return null;
        });
        Optional<Fine> result = dialog.showAndWait();
        result.ifPresent(fine -> {
            CSVHandler.updateFine(fine);
            refreshFinesTable();
        });
    }

    private void handleDeleteFine() {
        Fine selected = finesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih denda yang ingin dihapus.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus denda ID '" + selected.getId() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            CSVHandler.deleteFine(selected.getId());
            refreshFinesTable();
        }
    }

    private void handleCalculateFine() {
        Dialog<Fine> dialog = new Dialog<>();
        dialog.setTitle("Calculate Fine");
        dialog.setHeaderText("Hitung Denda Keterlambatan");
        ButtonType calculateBtnType = new ButtonType("Calculate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(calculateBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField borrowIdField = new TextField();
        borrowIdField.setPromptText("Borrow ID");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");
        DatePicker dueDatePicker = new DatePicker();
        DatePicker returnDatePicker = new DatePicker(LocalDate.now());
        TextField daysLateField = new TextField();
        daysLateField.setDisable(true);
        TextField amountField = new TextField();
        amountField.setDisable(true);
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("UNPAID", "PAID"));
        statusCombo.setValue("UNPAID");

        // Add listener to calculate fine when dates change
        dueDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && returnDatePicker.getValue() != null) {
                int daysLate = (int) java.time.temporal.ChronoUnit.DAYS.between(newVal, returnDatePicker.getValue());
                daysLateField.setText(String.valueOf(Math.max(0, daysLate)));
                amountField.setText(String.valueOf(Math.max(0, daysLate) * 1000.0));
            }
        });
        returnDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && dueDatePicker.getValue() != null) {
                int daysLate = (int) java.time.temporal.ChronoUnit.DAYS.between(dueDatePicker.getValue(), newVal);
                daysLateField.setText(String.valueOf(Math.max(0, daysLate)));
                amountField.setText(String.valueOf(Math.max(0, daysLate) * 1000.0));
            }
        });

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Borrow ID:"), 0, 1);
        grid.add(borrowIdField, 1, 1);
        grid.add(new Label("Student ID:"), 0, 2);
        grid.add(studentIdField, 1, 2);
        grid.add(new Label("Due Date:"), 0, 3);
        grid.add(dueDatePicker, 1, 3);
        grid.add(new Label("Return Date:"), 0, 4);
        grid.add(returnDatePicker, 1, 4);
        grid.add(new Label("Days Late:"), 0, 5);
        grid.add(daysLateField, 1, 5);
        grid.add(new Label("Amount:"), 0, 6);
        grid.add(amountField, 1, 6);
        grid.add(new Label("Status:"), 0, 7);
        grid.add(statusCombo, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == calculateBtnType) {
                try {
                    return new Fine(idField.getText(), borrowIdField.getText(), studentIdField.getText(), dueDatePicker.getValue(), returnDatePicker.getValue(), Integer.parseInt(daysLateField.getText()), Double.parseDouble(amountField.getText()), statusCombo.getValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        Optional<Fine> result = dialog.showAndWait();
        result.ifPresent(fine -> {
            CSVHandler.addFine(fine);
            refreshFinesTable();
        });
    }

    private void showActionAlert(String action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Action");
        alert.setHeaderText(null);
        alert.setContentText("Button '" + action + "' diklik. Fitur ini bisa dikembangkan lebih lanjut.");
        alert.showAndWait();
    }

    private HBox createHeader(String title, String subtitle) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        VBox titleSection = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        titleSection.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Dynamic date & time
        if (dateTimeLabel == null) {
            dateTimeLabel = new Label();
            dateTimeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e -> {
                dateTimeLabel.setText("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + " • " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }));
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();
        }

        header.getChildren().addAll(titleSection, spacer, dateTimeLabel);
        return header;
    }

    private HBox createStatsSection() {
        HBox statsSection = new HBox(20);
        statsSection.setAlignment(Pos.CENTER);

        // Ambil data statistik dari CSV
        // Ganti pemanggilan dari CSVHandler dengan DAO yang akses database
        try {
            int totalStudents = UserDAO.countByRole("mahasiswa"); // Asumsikan ada method untuk hitung by role
            int totalBooks = BookDAO.countAllBooks();             // Hitung dari tabel `books`
            int totalBorrows = BorrowingDAO.countActiveBorrows(); // Hitung `borrowings` dengan status = 'borrowed'
            double totalFines = FineDAO.getTotalFines();          // SUM(amount) dari tabel `fines`

            VBox studentsCard = createStatCard("👥", String.valueOf(totalStudents), "Total Mahasiswa", "#e74c3c");
            VBox booksCard = createStatCard("📚", String.valueOf(totalBooks), "Total Buku", "#3498db");
            VBox borrowsCard = createStatCard("🔄", String.valueOf(totalBorrows), "Pinjaman Aktif", "#e67e22");
            VBox finesCard = createStatCard("💰", String.format("Rp %,d", (long) totalFines), "Total Denda", "#16a085");
            statsSection.getChildren().addAll(studentsCard, booksCard, borrowsCard, finesCard);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Gagal memuat data statistik:\n" + e.getMessage());
        }

        return statsSection;
    }

    private VBox createStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(180, 120);

        // Gradient background
        Stop[] stops = new Stop[]{new Stop(0, Color.web(color)), new Stop(1, Color.web(color).darker())};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        card.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(15), null)));

        // Drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetY(5);
        shadow.setRadius(10);
        card.setEffect(shadow);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 30px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label labelText = new Label(label);
        labelText.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 12px;");

        card.getChildren().addAll(iconLabel, valueLabel, labelText);

        // Hover effect
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
            card.setCursor(Cursor.HAND);
        });

        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return card;
    }

    private VBox createMainDashboardContent() {
        VBox content = new VBox(30);

        // Recent activities section
        VBox recentSection = createRecentActivitiesSection();

        content.getChildren().addAll(recentSection);
        return content;
    }

    private VBox createRecentActivitiesSection() {
        VBox section = new VBox(15);

//        Label title = new Label("📊 Recent Activities");
//        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
//
//        VBox activitiesBox = new VBox(10);
//        activitiesBox.setPadding(new Insets(20));
//        activitiesBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
//
//        String[] activities = {"📖 'The Great Gatsby' issued to John Doe - 2 hours ago", "👤 New member Sarah Johnson registered - 4 hours ago", "🔄 'To Kill a Mockingbird' returned by Alice Smith - 6 hours ago", "📚 New book 'Java Programming' added to inventory - 1 day ago", "⚠️ Overdue reminder sent to 15 members - 1 day ago"};
//
//        for (String activity : activities) {
//            Label activityLabel = new Label(activity);
//            activityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555; -fx-padding: 8 0;");
//            activitiesBox.getChildren().add(activityLabel);
//        }

//        section.getChildren().addAll(title, activitiesBox);
        return section;
    }

    private void refreshBooksTable() {
        try {
            List<Book> booksFromDB = BookDAO.getAllBooksWithAvailability(); // Ambil dari database
            booksTable.getItems().setAll(booksFromDB);      // Tampilkan di TableView
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal memuat data buku dari database:\n" + e.getMessage());
            alert.showAndWait();
        }
    }


    private void refreshStudentsTable() {
        studentsTable.getItems().setAll(CSVHandler.readStudents());
    }

    private void refreshBorrowsTable() {
        borrowsTable.getItems().setAll(CSVHandler.readBorrows());
    }

    private void refreshReturnsTable() {
        try {
            List<Return> returns = ReturnDAO.getAllReturnHistory();
            returnsTable.setItems(FXCollections.observableArrayList(returns));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Gagal Memuat Data");
            alert.setContentText("Terjadi kesalahan saat mengambil data pengembalian:\n" + e.getMessage());
            alert.showAndWait();
        }
    }


    private void refreshFinesTable() {
        finesTable.getItems().setAll(CSVHandler.readFines());
    }

}