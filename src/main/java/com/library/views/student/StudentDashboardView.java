package com.library.views.student;

import com.library.dao.BookDAO;
import com.library.dao.BorrowingDAO;
import com.library.dao.BorrowingDetailDAO;
import com.library.models.*;
import com.library.views.LoginView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import com.library.utils.CSVHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.StageStyle;

public class StudentDashboardView {
    private BorderPane root;
    private VBox sidebar;
    private VBox mainContent;
    private User user;
    private Stage primaryStage;
    private String currentPage = "Dashboard";
    private Label dateTimeLabel;

    public void show(Stage stage, User user) {
        this.primaryStage = stage;
        this.user = user;
        root = new BorderPane();
        sidebar = createSidebar();
        mainContent = createDashboardContent();
        root.setLeft(sidebar);
        root.setCenter(mainContent);
        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add("data:text/css," + getCSS());
        stage.setTitle("Library Student Portal");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED); // default
        stage.show();
        System.out.println("✅ Student dashboard stage should now be visible.");
        System.out.println("CSS applied: " + getCSS());
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#2980b9")),
                new Stop(0.5, Color.web("#3498db")),
                new Stop(1, Color.web("#5dade2"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        sidebar.setBackground(new Background(new BackgroundFill(gradient, null, null)));
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);
        Label logoIcon = new Label("🎓");
        logoIcon.setStyle("-fx-font-size: 40px;");
        Label logoText = new Label("StudentLib");
        logoText.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        logoSection.getChildren().addAll(logoIcon, logoText);
        VBox menuSection = new VBox(8);
        menuSection.setPadding(new Insets(30, 0, 0, 0));
        String[] menuItems = {
                "🏠 Dashboard",
                "📖 Pinjam Buku",
                "📋 Riwayat Peminjaman",
                "🚪 Logout"
        };
        for (String item : menuItems) {
            Label menuItem = createMenuItem(item);
            menuSection.getChildren().add(menuItem);
        }
        sidebar.getChildren().addAll(logoSection, menuSection);
        return sidebar;
    }

    private Label createMenuItem(String text) {
        Label item = new Label(text);
        String baseStyle = "-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 20; " +
                "-fx-background-radius: 8; -fx-cursor: hand;";
        String pageName = text.split(" ", 2)[1];
        if (pageName.equals(currentPage)) {
            item.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.3);");
        } else {
            item.setStyle(baseStyle);
        }
        item.setPrefWidth(240);
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
        item.setOnMouseClicked(e -> {
            navigateToPage(pageName);
        });
        return item;
    }

    private void navigateToPage(String pageName) {
        currentPage = pageName;
        sidebar = createSidebar();
        root.setLeft(sidebar);
        switch (pageName) {
            case "Dashboard":
                mainContent = createDashboardContent();
                break;
            case "Pinjam Buku":
                mainContent = createPinjamBukuContent();
                break;
            case "Riwayat Peminjaman":
                mainContent = createRiwayatContent();
                break;
            case "Logout":
                new LoginView(primaryStage).showLoginScene();
                return;
            default:
                mainContent = createDashboardContent();
        }
        root.setCenter(mainContent);
    }

    private VBox createDashboardContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");
        HBox header = createHeader();
        HBox statsSection = createStatsSection();
        VBox dashboardContent = createDashboardMainContent();
        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        mainContent.getChildren().addAll(header, statsSection, scrollPane);
        return mainContent;
    }

    private VBox createPinjamBukuContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(20));

        Label title = new Label("📖 Pinjam Buku");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        TableView<Book> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        table.setPrefHeight(350);

        // Kolom
        TableColumn<Book, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Integer> qtyCol = new TableColumn<>("Stok");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));

        TableColumn<Book, String> actionCol = new TableColumn<>("Aksi");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Pinjam");

            {
                btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
                btn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());

                    try {
                        // Ambil satu item tersedia dari book_items
                        BookItem item = BookDAO.getAvailableBookItem(book.getId());
                        if (item == null) {
                            showAlert("Info", "Tidak ada salinan buku yang tersedia.");
                            return;
                        }

                        // Buat entri di tabel borrowings
                        LocalDate now = LocalDate.now();
                        LocalDate due = now.plusWeeks(2);

                        Borrowing borrowing = new Borrowing();
                        borrowing.setUserId(user.getId()); // Pastikan User memiliki getId()
                        borrowing.setBorrowDate(now);
                        borrowing.setReturnDueDate(due);
                        borrowing.setStatus("borrowed");

                        borrowing = BorrowingDAO.createBorrowing(borrowing); // overwrite dengan return-nya
                        int borrowingId = borrowing.getId(); // ambil ID yang sudah diset dari dalam DAO

                        // Tambah ke tabel borrowing_details
                        BorrowingDetail detail = new BorrowingDetail();
                        detail.setBorrowingId(borrowingId);
                        detail.setBookItemId(item.getId());
                        BorrowingDetailDAO.createBorrowingDetail(detail);

                        // Update status is_available book_item
                        BookDAO.updateBookItemAvailability(item.getId(), false);

                        showAlert("Sukses", "Buku berhasil dipinjam!");
                        refreshPinjamBukuTable(table);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Gagal meminjam buku:\n" + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(idCol, titleCol, authorCol, qtyCol, actionCol);

        refreshPinjamBukuTable(table);
        content.getChildren().addAll(title, table);
        return content;
    }

    private void refreshPinjamBukuTable(TableView<Book> table) {
        try {
            List<Book> books = BookDAO.getBooksWithAvailableQuantity();
            table.setItems(FXCollections.observableArrayList(books));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat buku dari database:\n" + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createRiwayatContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(20));

        Label title = new Label("📋 Riwayat Peminjaman");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        TableView<Borrowing> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        table.setPrefHeight(350);

        TableColumn<Borrowing, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Borrowing, LocalDate> borrowDateCol = new TableColumn<>("Tanggal Pinjam");
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<Borrowing, LocalDate> dueDateCol = new TableColumn<>("Jatuh Tempo");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDueDate"));

        TableColumn<Borrowing, LocalDate> returnDateCol = new TableColumn<>("Tanggal Kembali");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Borrowing, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, borrowDateCol, dueDateCol, returnDateCol, statusCol);

        try {
            List<Borrowing> borrowings = BorrowingDAO.getBorrowingsByUserId(user.getId());
            table.setItems(FXCollections.observableArrayList(borrowings));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat riwayat peminjaman:\n" + e.getMessage());
        }

        content.getChildren().addAll(title, table);
        return content;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));
        VBox titleSection = new VBox(5);
        Label title = new Label("Dashboard Mahasiswa");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label subtitle = new Label("Selamat datang, " + user.getUsername() + "! Kelola peminjaman buku Anda dengan mudah");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        titleSection.getChildren().addAll(title, subtitle);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (dateTimeLabel == null) {
            dateTimeLabel = new Label();
            dateTimeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e -> {
                dateTimeLabel.setText("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) +
                        " • " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
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
        String studentId = user.getUsername();

        try {
            Map<String, Integer> stats = BorrowingDAO.getBorrowStatsByUsername(studentId);
            VBox card1 = createStatCard("📚", String.valueOf(stats.get("bukuDipinjam")), "Buku Dipinjam", "#3498db");
            VBox card2 = createStatCard("📋", String.valueOf(stats.get("totalPeminjaman")), "Total Peminjaman", "#27ae60");
            VBox card3 = createStatCard("⏰", String.valueOf(stats.get("hampirJatuhTempo")), "Hampir Jatuh Tempo", "#f39c12");
            VBox card4 = createStatCard("✅", String.valueOf(stats.get("telahDikembalikan")), "Telah Dikembalikan", "#2ecc71");
            statsSection.getChildren().addAll(card1, card2, card3, card4);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal mengambil statistik peminjaman:\n" + e.getMessage());
        }

        return statsSection;
    }


    private VBox createStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(220, 120);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web(color)),
                new Stop(1, Color.web(color).darker())
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        card.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(15), null)));
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

    private VBox createDashboardMainContent() {
        VBox content = new VBox(30);
        VBox borrowedBooksSection = createBorrowedBooksSection();
        HBox quickActions = createQuickActionsSection();
        VBox recommendationsSection = createRecommendationsSection();
        content.getChildren().addAll(borrowedBooksSection, quickActions, recommendationsSection);
        return content;
    }

    private VBox createBorrowedBooksSection() {
        VBox section = new VBox(15);
        Label title = new Label("📚 Buku yang Sedang Dipinjam");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TableView<Borrow> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        table.setPrefHeight(200);

        TableColumn<Borrow, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Borrow, String> bookTitleCol = new TableColumn<>("Judul Buku");
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<Borrow, LocalDate> dueDateCol = new TableColumn<>("Jatuh Tempo");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Borrow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, bookTitleCol, dueDateCol, statusCol);

        try {
            List<Borrow> borrowedBooks = BorrowingDAO.getBorrowedBooksByUsername(user.getUsername());
            table.setItems(FXCollections.observableArrayList(borrowedBooks));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data pinjaman buku dari database:\n" + e.getMessage());
        }

        section.getChildren().addAll(title, table);
        return section;
    }

    private HBox createQuickActionsSection() {
        HBox section = new HBox(20);
        section.setAlignment(Pos.CENTER);
        String[][] actions = {
                {"📖 Pinjam Buku Baru", "#27ae60"},
                {"📋 Lihat Riwayat", "#f39c12"},
        };
        for (String[] action : actions) {
            VBox actionCard = createActionCard(action[0], action[1]);
            actionCard.setOnMouseClicked(e -> handleQuickAction(action[0]));
            section.getChildren().add(actionCard);
        }
        return section;
    }

    private void handleQuickAction(String action) {
        switch (action) {
            case "🔍 Cari Buku":
                showCariBukuDialog();
                break;
            case "📖 Pinjam Buku Baru":
                navigateToPage("Pinjam Buku");
                break;
            case "📋 Lihat Riwayat":
                navigateToPage("Riwayat Peminjaman");
                break;
            case "⏰ Perpanjang Pinjaman":
                showPerpanjangPinjamanDialog();
                break;
        }
    }

    private void showCariBukuDialog() {
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Cari Buku");
        dialog.setHeaderText("Pencarian Buku di Perpustakaan");
        javafx.scene.control.ButtonType closeBtn = new javafx.scene.control.ButtonType("Tutup", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeBtn);
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        javafx.scene.control.TextField searchField = new javafx.scene.control.TextField();
        searchField.setPromptText("Cari judul, penulis, atau kategori...");
        TableView<Book> table = new TableView<>();
        TableColumn<Book, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, String> catCol = new TableColumn<>("Kategori");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        table.getColumns().addAll(idCol, titleCol, authorCol, catCol);
        ObservableList<Book> allBooks = FXCollections.observableArrayList(CSVHandler.readBooks());
        table.setItems(allBooks);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String q = newVal.toLowerCase();
            table.setItems(allBooks.filtered(b ->
                    b.getTitle().toLowerCase().contains(q) ||
                            b.getAuthor().toLowerCase().contains(q)
            ));
        });
        content.getChildren().addAll(searchField, table);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void showPerpanjangPinjamanDialog() {
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Perpanjang Pinjaman");
        dialog.setHeaderText("Daftar Pinjaman Aktif Anda");
        javafx.scene.control.ButtonType closeBtn = new javafx.scene.control.ButtonType("Tutup", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(closeBtn);
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        TableView<Borrow> table = new TableView<>();
        TableColumn<Borrow, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Borrow, String> bookIdCol = new TableColumn<>("Book ID");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        TableColumn<Borrow, LocalDate> dueDateCol = new TableColumn<>("Jatuh Tempo");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        TableColumn<Borrow, String> actionCol = new TableColumn<>("Aksi");
        actionCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Perpanjang");

            {
                btn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
                btn.setOnAction(e -> {
                    Borrow borrow = getTableView().getItems().get(getIndex());
                    borrow.setDueDate(borrow.getDueDate().plusWeeks(1));
                    CSVHandler.updateBorrow(borrow);
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Pinjaman berhasil diperpanjang 7 hari!", javafx.scene.control.ButtonType.OK);
                    alert.showAndWait();
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        });
        table.getColumns().addAll(idCol, bookIdCol, dueDateCol, actionCol);
        ObservableList<Borrow> data = FXCollections.observableArrayList();
        String studentId = user.getUsername();
        for (Borrow b : CSVHandler.readBorrows()) {
            if (b.getStudentId().equals(studentId) && b.getStatus().equalsIgnoreCase("BORROWED")) {
                data.add(b);
            }
        }
        table.setItems(data);
        content.getChildren().addAll(table);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private VBox createActionCard(String text, String color) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefSize(200, 80);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; " +
                "-fx-cursor: hand;");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        shadow.setOffsetY(3);
        shadow.setRadius(8);
        card.setEffect(shadow);
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        card.getChildren().add(label);
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: derive(" + color + ", -10%); " +
                    "-fx-background-radius: 10; -fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; " +
                    "-fx-cursor: hand;");
        });
        return card;
    }

    private VBox createRecommendationsSection() {
        VBox section = new VBox(15);
//        Label title = new Label("💡 Rekomendasi Buku");
//        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
//        HBox recommendationsBox = new HBox(15);
//        recommendationsBox.setPadding(new Insets(20));
//        recommendationsBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
//                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
//        String[][] recommendations = {
//                {"Pemrograman Java", "★★★★★", "#2ecc71"},
//                {"Machine Learning", "★★★★☆", "#3498db"},
//                {"Web Development", "★★★★★", "#e74c3c"}
//        };
//        for (String[] rec : recommendations) {
//            VBox bookCard = createBookRecommendationCard(rec[0], rec[1], rec[2]);
//            recommendationsBox.getChildren().add(bookCard);
//        }
//        section.getChildren().addAll(title, recommendationsBox);
        return section;
    }

    private VBox createBookRecommendationCard(String title, String rating, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setPrefSize(200, 120);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; " +
                "-fx-cursor: hand;");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        shadow.setOffsetY(3);
        shadow.setRadius(8);
        card.setEffect(shadow);
        Label bookIcon = new Label("📚");
        bookIcon.setStyle("-fx-font-size: 30px;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label ratingLabel = new Label(rating);
        ratingLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 12px;");
        card.getChildren().addAll(bookIcon, titleLabel, ratingLabel);
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: derive(" + color + ", -10%); " +
                    "-fx-background-radius: 10; -fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; " +
                    "-fx-cursor: hand;");
        });
        return card;
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
                """;
    }
} 