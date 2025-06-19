<<<<<<< HEAD
# 📚 OOPLibrarySystem_TeamX

**Final Project — Object Oriented Programming (OOP)**  
Universitas Muhammadiyah Malang  
Dosen: Ir. Galih Wasis Wicaksono, S.Kom., M.Cs.  
Kelas: 2A / 2B  
Deadline: 19 Juni 2025  
Presentasi: 20–26 Juni 2025

## 👨‍👩‍👦 Team Members

| No | Name                   | Role                     |
|----|------------------------|--------------------------|
| 1  | Ismail Dwi M. Anugerah | Lead Developer & Documentation |
| 2  | Abi Danadhyaksa        | Backend Specialist       |
| 3  | Mahligai Arsya Nanda   | Frontend Specialist      |

## 📌 Project Overview

**OOPLibrarySystem** adalah sistem informasi perpustakaan kampus berbasis Java yang dirancang untuk menggantikan proses manual dengan antarmuka pengguna grafis menggunakan JavaFX. Sistem ini mencakup manajemen koleksi buku, proses peminjaman dan pengembalian, serta laporan aktivitas perpustakaan.

## 🧩 Features

- 🔐 **Login** untuk Admin dan Member
- 📖 **Manajemen Buku** (CRUD, pencarian)
- 🧑‍🎓 **Registrasi Member** dengan validasi ID/email unik
- 📚 **Peminjaman & Pengembalian Buku** otomatis 7 hari, kalkulasi denda
- 📊 **Laporan & Statistik** bulanan
- ⚠️ **Dialog Konfirmasi & Notifikasi** berbasis JavaFX

## 🏗️ Architecture

Sistem dikembangkan dengan arsitektur **MVC (Model-View-Controller)**:
- **Model**: Book, Member, Transaction, LibraryManager
- **View**: JavaFX (FXML/SceneBuilder)
- **Controller**: Mengatur interaksi pengguna & logika sistem

## 📂 Folder Structure


## 🛠️ Technologies

- Java 11+
- JavaFX (FXML)
- CSV File I/O
- OOP Principles (Encapsulation, Inheritance, Polymorphism)
- IDE: IntelliJ IDEA / Eclipse

## 📅 Milestones

| Phase                    | Output                                 |
|--------------------------|----------------------------------------|
| Week 1: Setup & Design   | IDE setup, mockup UI, class diagram    |
| Week 2: Core & CRUD      | Book/Member CRUD, model classes        |
| Week 3: GUI Integration  | JavaFX UI, borrow-return logic         |
| Week 4: Testing & Docs   | Unit tests, dokumentasi, presentasi    |

## 📈 Learning Outcomes Covered

✅ Control structures, methods, data types  
✅ OOP concepts: classes, inheritance, polymorphism  
✅ JavaFX UI & Event Handling  
✅ File I/O with CSV, Exception Handling  
✅ Spiral model development & debugging

## 📥 How to Run

1. Clone repo:  
   ```bash
   [git clone [https://github.com/Ismaildwi24/OOPLibrarySystem_PowerRangers.git]
](https://github.com/Ismaildwi24/OOPLibrarySystem_PowerRangers.git)
=======
# Portal Perpustakaan Mahasiswa

Aplikasi desktop untuk mengelola peminjaman buku perpustakaan oleh mahasiswa.

## Persyaratan Sistem

- Java Development Kit (JDK) 17 atau lebih baru
- Maven 3.6.0 atau lebih baru

## Cara Menjalankan Aplikasi

1. Pastikan JDK dan Maven sudah terinstal di sistem Anda
2. Buka terminal/command prompt
3. Masuk ke direktori proyek
4. Jalankan perintah berikut untuk mengunduh dependensi:
   ```
   mvn clean install
   ```
5. Jalankan aplikasi dengan perintah:
   ```
   mvn javafx:run
   ```

## Fitur Aplikasi

- Dashboard dengan statistik peminjaman
- Peminjaman buku
- Riwayat peminjaman
- Perpanjangan peminjaman
- Rekomendasi buku

## Struktur Proyek

- `src/Main.java` - File utama aplikasi
- `pom.xml` - Konfigurasi Maven dan dependensi
>>>>>>> e2c9a35 (Final Project OOP Team Power Rangers)
