import javafx.application.Application;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main extends Application {
    private String currentPage = "dashboard";
    private VBox mainContent;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Main container
        BorderPane root = new BorderPane();

        // Create sidebar
        VBox sidebar = createSidebar();

        // Create main content area
        mainContent = createMainContent();

        // Set layout
        root.setLeft(sidebar);
        root.setCenter(mainContent);

        // Create scene with gradient background
        Scene scene = new Scene(root, 1400, 800);
        scene.getStylesheets().add("data:text/css," + getCSS());

        primaryStage.setTitle("Library Student Portal");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));

        // Gradient background for sidebar (Red theme like admin)
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#8B0000")),
                new Stop(0.5, Color.web("#CD5C5C")),
                new Stop(1, Color.web("#B22222"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        sidebar.setBackground(new Background(new BackgroundFill(gradient, null, null)));

        // Logo section
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);

        Label logoIcon = new Label("🎓");
        logoIcon.setStyle("-fx-font-size: 40px;");

        Label logoText = new Label("StudentLib");
        logoText.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        logoSection.getChildren().addAll(logoIcon, logoText);

        // Menu items - Only 3 menu items as requested
        VBox menuSection = new VBox(8);
        menuSection.setPadding(new Insets(30, 0, 0, 0));

        String[] menuItems = {
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
        item.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 20; " +
                "-fx-background-radius: 8; -fx-cursor: hand;");
        item.setPrefWidth(240);

        // Hover effect
        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 20; " +
                    "-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 8; -fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(100), item);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        item.setOnMouseExited(e -> {
            item.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15 20; " +
                    "-fx-background-radius: 8; -fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(100), item);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Add click handlers for menu items
        item.setOnMouseClicked(e -> {
            handleMenuClick(text);
        });

        return item;
    }

    private void handleMenuClick(String menuText) {
        // Handle menu clicks and change content
        switch (menuText) {
            case "📖 Pinjam Buku":
                currentPage = "pinjam";
                updateMainContent(createPinjamBukuPage());
                break;
            case "📋 Riwayat Peminjaman":
                currentPage = "riwayat";
                updateMainContent(createRiwayatPage());
                break;
            case "🚪 Logout":
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi Logout");
                alert.setHeaderText("Apakah Anda yakin ingin keluar?");
                alert.setContentText("Anda akan keluar dari sistem perpustakaan.");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    primaryStage.close();
                }
                break;
        }
    }

    private void updateMainContent(VBox newContent) {
        mainContent.getChildren().clear();

        // Header
        HBox header = createHeader();

        mainContent.getChildren().addAll(header, newContent);
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        HBox header = createHeader();

        // Initial dashboard content
        VBox dashboardContent = createDashboardPage();

        mainContent.getChildren().addAll(header, dashboardContent);
        return mainContent;
    }

    private VBox createDashboardPage() {
        VBox content = new VBox(20);

        // Stats cards for student
        HBox statsSection = createStatsSection();

        // Dashboard content
        VBox dashboardContent = createDashboardContent();

        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        content.getChildren().addAll(statsSection, scrollPane);
        return content;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        VBox titleSection = new VBox(5);
        Label title = new Label("Dashboard Mahasiswa");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitle = new Label("Selamat datang! Kelola peminjaman buku Anda dengan mudah");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        titleSection.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label dateTime = new Label("📅 7 Juni 2025 • 10:30 WIB");
        dateTime.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleSection, spacer, dateTime);
        return header;
    }

    private HBox createStatsSection() {
        HBox statsSection = new HBox(20);
        statsSection.setAlignment(Pos.CENTER);

        // Student-specific stats
        String[][] stats = {
                {"📚", "5", "Buku Dipinjam", "#3498db"},
                {"📋", "12", "Total Peminjaman", "#27ae60"},
                {"⏰", "2", "Hampir Jatuh Tempo", "#f39c12"},
                {"✅", "10", "Telah Dikembalikan", "#2ecc71"}
        };

        for (String[] stat : stats) {
            VBox card = createStatCard(stat[0], stat[1], stat[2], stat[3]);
            statsSection.getChildren().add(card);
        }

        return statsSection;
    }

    private VBox createStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(220, 120);

        // Gradient background
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web(color)),
                new Stop(1, Color.web(color).darker())
        };
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

    // Halaman Pinjam Buku
    private VBox createPinjamBukuPage() {
        VBox page = new VBox(20);

        // Title
        Label title = new Label("📖 Pinjam Buku");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Search section
        VBox searchSection = createSearchSection();

        // Available books table
        VBox booksSection = createAvailableBooksSection();

        page.getChildren().addAll(title, searchSection, booksSection);
        return page;
    }

    private VBox createSearchSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label searchLabel = new Label("🔍 Cari Buku");
        searchLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Masukkan judul buku, pengarang, atau ISBN...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        Button searchBtn = new Button("Cari");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");

        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("Semua Kategori", "Pemrograman", "Matematika", "Fisika", "Kimia", "Sastra");
        categoryFilter.setValue("Semua Kategori");
        categoryFilter.setStyle("-fx-font-size: 14px;");

        searchBox.getChildren().addAll(searchField, categoryFilter, searchBtn);
        section.getChildren().addAll(searchLabel, searchBox);

        return section;
    }

    private VBox createAvailableBooksSection() {
        VBox section = new VBox(15);

        Label title = new Label("📚 Buku Tersedia");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create table
        TableView<Book> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<Book, String> judulCol = new TableColumn<>("Judul");
        judulCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        judulCol.setPrefWidth(250);

        TableColumn<Book, String> pengarangCol = new TableColumn<>("Pengarang");
        pengarangCol.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        pengarangCol.setPrefWidth(200);

        TableColumn<Book, String> kategoriCol = new TableColumn<>("Kategori");
        kategoriCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        kategoriCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(150);

        TableColumn<Book, Integer> stokCol = new TableColumn<>("Stok");
        stokCol.setCellValueFactory(new PropertyValueFactory<>("stok"));
        stokCol.setPrefWidth(80);

        TableColumn<Book, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(100);

        actionCol.setCellFactory(param -> new TableCell<Book, Void>() {
            private final Button pinjamBtn = new Button("Pinjam");

            {
                pinjamBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
                pinjamBtn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handlePinjamBuku(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pinjamBtn);
                }
            }
        });

        table.getColumns().addAll(judulCol, pengarangCol, kategoriCol, isbnCol, stokCol, actionCol);

        // Sample data
        ObservableList<Book> books = FXCollections.observableArrayList(
                new Book("Algoritma dan Pemrograman", "John Doe", "Pemrograman", "978-123-456-789-0", 5),
                new Book("Matematika Diskrit", "Jane Smith", "Matematika", "978-123-456-789-1", 3),
                new Book("Fisika Dasar", "Bob Johnson", "Fisika", "978-123-456-789-2", 7),
                new Book("Struktur Data", "Alice Brown", "Pemrograman", "978-123-456-789-3", 2),
                new Book("Kalkulus", "Charlie Wilson", "Matematika", "978-123-456-789-4", 4)
        );

        table.setItems(books);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-padding: 20;");

        section.getChildren().addAll(title, tableContainer);
        return section;
    }

    private void handlePinjamBuku(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Peminjaman");
        alert.setHeaderText("Pinjam Buku: " + book.getJudul());
        alert.setContentText("Apakah Anda yakin ingin meminjam buku ini?\n\n" +
                "Batas waktu peminjaman: 14 hari\n" +
                "Tanggal kembali: " + LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        if (alert.showAndWait().get() == ButtonType.OK) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Peminjaman Berhasil");
            success.setHeaderText("Buku berhasil dipinjam!");
            success.setContentText("Buku '" + book.getJudul() + "' telah berhasil dipinjam.\n" +
                    "Harap mengembalikan sebelum tanggal jatuh tempo.");
            success.showAndWait();
        }
    }

    // Halaman Riwayat Peminjaman
    private VBox createRiwayatPage() {
        VBox page = new VBox(20);

        // Title
        Label title = new Label("📋 Riwayat Peminjaman");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Filter section
        VBox filterSection = createFilterSection();

        // History table
        VBox historySection = createHistorySection();

        page.getChildren().addAll(title, filterSection, historySection);
        return page;
    }

    private VBox createFilterSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label filterLabel = new Label("🔍 Filter Riwayat");
        filterLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Semua Status", "Sedang Dipinjam", "Sudah Dikembalikan", "Terlambat");
        statusFilter.setValue("Semua Status");
        statusFilter.setStyle("-fx-font-size: 14px;");

        DatePicker fromDate = new DatePicker();
        fromDate.setPromptText("Dari Tanggal");
        fromDate.setStyle("-fx-font-size: 14px;");

        DatePicker toDate = new DatePicker();
        toDate.setPromptText("Sampai Tanggal");
        toDate.setStyle("-fx-font-size: 14px;");

        Button filterBtn = new Button("Terapkan Filter");
        filterBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");

        filterBox.getChildren().addAll(
                new Label("Status:"), statusFilter,
                new Label("Dari:"), fromDate,
                new Label("Sampai:"), toDate,
                filterBtn
        );

        section.getChildren().addAll(filterLabel, filterBox);
        return section;
    }

    private VBox createHistorySection() {
        VBox section = new VBox(15);

        Label title = new Label("📚 Riwayat Peminjaman Anda");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create history table
        TableView<PinjamHistory> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<PinjamHistory, String> judulCol = new TableColumn<>("Judul Buku");
        judulCol.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));
        judulCol.setPrefWidth(200);

        TableColumn<PinjamHistory, String> tglPinjamCol = new TableColumn<>("Tanggal Pinjam");
        tglPinjamCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        tglPinjamCol.setPrefWidth(130);

        TableColumn<PinjamHistory, String> tglKembaliCol = new TableColumn<>("Tanggal Kembali");
        tglKembaliCol.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        tglKembaliCol.setPrefWidth(130);

        TableColumn<PinjamHistory, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<PinjamHistory, String> dendaCol = new TableColumn<>("Denda");
        dendaCol.setCellValueFactory(new PropertyValueFactory<>("denda"));
        dendaCol.setPrefWidth(100);

        TableColumn<PinjamHistory, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(120);

        actionCol.setCellFactory(param -> new TableCell<PinjamHistory, Void>() {
            private final Button perpanjangBtn = new Button("Perpanjang");

            {
                perpanjangBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; " +
                        "-fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 5;");
                perpanjangBtn.setOnAction(event -> {
                    PinjamHistory history = getTableView().getItems().get(getIndex());
                    handlePerpanjangPinjaman(history);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    PinjamHistory history = getTableView().getItems().get(getIndex());
                    if ("Sedang Dipinjam".equals(history.getStatus())) {
                        setGraphic(perpanjangBtn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        table.getColumns().addAll(judulCol, tglPinjamCol, tglKembaliCol, statusCol, dendaCol, actionCol);

        // Sample data
        ObservableList<PinjamHistory> history = FXCollections.observableArrayList(
                new PinjamHistory("Algoritma dan Pemrograman", "01 Jun 2025", "15 Jun 2025", "Sedang Dipinjam", "Rp 0"),
                new PinjamHistory("Basis Data", "25 Mei 2025", "08 Jun 2025", "Sudah Dikembalikan", "Rp 0"),
                new PinjamHistory("Rekayasa Perangkat Lunak", "20 Mei 2025", "05 Jun 2025", "Sudah Dikembalikan", "Rp 5,000"),
                new PinjamHistory("Matematika Diskrit", "15 Mei 2025", "29 Mei 2025", "Sudah Dikembalikan", "Rp 0"),
                new PinjamHistory("Struktur Data", "10 Mei 2025", "25 Mei 2025", "Sudah Dikembalikan", "Rp 0")
        );

        table.setItems(history);

        VBox tableContainer = new VBox(table);
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-padding: 20;");

        section.getChildren().addAll(title, tableContainer);
        return section;
    }

    private void handlePerpanjangPinjaman(PinjamHistory history) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Perpanjangan");
        alert.setHeaderText("Perpanjang Peminjaman: " + history.getJudulBuku());
        alert.setContentText("Apakah Anda yakin ingin memperpanjang peminjaman buku ini?\n\n" +
                "Perpanjangan: 7 hari\n" +
                "Tanggal kembali baru: " + LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        if (alert.showAndWait().get() == ButtonType.OK) {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Perpanjangan Berhasil");
            success.setHeaderText("Peminjaman berhasil diperpanjang!");
            success.setContentText("Peminjaman buku '" + history.getJudulBuku() + "' telah diperpanjang.\n" +
                    "Tanggal kembali baru: " + LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            success.showAndWait();
        }
    }

    // Data classes
    public static class Book {
        private String judul;
        private String pengarang;
        private String kategori;
        private String isbn;
        private int stok;

        public Book(String judul, String pengarang, String kategori, String isbn, int stok) {
            this.judul = judul;
            this.pengarang = pengarang;
            this.kategori = kategori;
            this.isbn = isbn;
            this.stok = stok;
        }

        // Getters
        public String getJudul() { return judul; }
        public String getPengarang() { return pengarang; }
        public String getKategori() { return kategori; }
        public String getIsbn() { return isbn; }
        public int getStok() { return stok; }
    }

    public static class PinjamHistory {
        private String judulBuku;
        private String tanggalPinjam;
        private String tanggalKembali;
        private String status;
        private String denda;

        public PinjamHistory(String judulBuku, String tanggalPinjam, String tanggalKembali, String status, String denda) {
            this.judulBuku = judulBuku;
            this.tanggalPinjam = tanggalPinjam;
            this.tanggalKembali = tanggalKembali;
            this.status = status;
            this.denda = denda;
        }

        // Getters
        public String getJudulBuku() { return judulBuku; }
        public String getTanggalPinjam() { return tanggalPinjam; }
        public String getTanggalKembali() { return tanggalKembali; }
        public String getStatus() { return status; }
        public String getDenda() { return denda; }
    }

    private VBox createDashboardContent() {
        VBox content = new VBox(30);

        // Current borrowed books section
        VBox borrowedBooksSection = createBorrowedBooksSection();

        // Quick actions section
        HBox quickActions = createQuickActionsSection();

        // Recommendations section
        VBox recommendationsSection = createRecommendationsSection();

        content.getChildren().addAll(borrowedBooksSection, quickActions, recommendationsSection);
        return content;
    }

    private VBox createBorrowedBooksSection() {
        VBox section = new VBox(15);

        Label title = new Label("📚 Buku yang Sedang Dipinjam");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox booksBox = new VBox(10);
        booksBox.setPadding(new Insets(20));
        booksBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        String[] borrowedBooks = {
                "📖 'Algoritma dan Pemrograman' - Jatuh tempo: 15 Juni 2025",
                "📖 'Basis Data' - Jatuh tempo: 20 Juni 2025",
                "📖 'Rekayasa Perangkat Lunak' - Jatuh tempo: 25 Juni 2025",
                "📖 'Matematika Diskrit' - Jatuh tempo: 30 Juni 2025",
                "📖 'Struktur Data dan Algoritma' - Jatuh tempo: 5 Juli 2025"
        };

        for (String book : borrowedBooks) {
            Label bookLabel = new Label(book);
            bookLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555; -fx-padding: 8 0;");
            booksBox.getChildren().add(bookLabel);
        }

        section.getChildren().addAll(title, booksBox);
        return section;
    }

    private HBox createQuickActionsSection() {
        HBox section = new HBox(20);
        section.setAlignment(Pos.CENTER);

        String[][] actions = {
                {"🔍 Cari Buku", "#3498db"},
                {"📖 Pinjam Buku Baru", "#27ae60"},
                {"📋 Lihat Riwayat", "#f39c12"},
                {"⏰ Perpanjang Pinjaman", "#e74c3c"}
        };

        for (String[] action : actions) {
            VBox actionCard = createActionCard(action[0], action[1]);
            section.getChildren().add(actionCard);
        }

        return section;
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

        // Hover effect
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

        Label title = new Label("💡 Rekomendasi Buku");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox recommendationsBox = new HBox(15);
        recommendationsBox.setPadding(new Insets(20));
        recommendationsBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        String[][] recommendations = {
                {"Pemrograman Java", "★★★★★", "#2ecc71"},
                {"Machine Learning", "★★★★☆", "#3498db"},
                {"Web Development", "★★★★★", "#e74c3c"}
        };

        for (String[] rec : recommendations) {
            VBox bookCard = createBookRecommendationCard(rec[0], rec[1], rec[2]);
            recommendationsBox.getChildren().add(bookCard);
        }

        section.getChildren().addAll(title, recommendationsBox);
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

        // Hover effect
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

    public static void main(String[] args) {
        launch(args);
    }
}