# 📚 OOPLibrarySystem_PowerRangers

**Final Project — Object Oriented Programming (OOP)**  
Universitas Muhammadiyah Malang  
Dosen: Ir. Galih Wasis Wicaksono, S.Kom., M.Cs.  
Kelas: 2A / 2B  
Deadline: 19 Juni 2025  
Presentasi: 20–26 Juni 2025

---

## 👨‍👩‍👦 Team Members

| No | Name                    | NIM              | Role                          |
|----|-------------------------|------------------|-------------------------------|
| 1  | Ismail Dwi M. Anugerah  | 2024103703110013 | Lead Developer & Documentation|
| 2  | Abi Danadhyaksa         | 2024103703110047 | Backend Specialist            |
| 3  | Mahligai Arsya Nanda    | 2024103703110056 | Frontend Specialist           |

---

## 🎥 Link Video Instalasi dan Pengenalan Fitur-Fitur Web

- **Link Video Instalasi:** (https://drive.google.com/file/d/1sVCrX3h98OWWl9HDXAuODxVdPgMEac-r/view?usp=drive_link)  
- **Link Fitur Web:** (https://drive.google.com/file/d/19NX8QzkebLeOaTnm6Al3q95F6mNvZxFJ/view?usp=drive_link)


---

## 📌 Project Overview

**OOPLibrarySystem** adalah sistem informasi perpustakaan berbasis desktop yang dibangun dengan Java dan JavaFX. Aplikasi ini dirancang untuk membantu pengelolaan perpustakaan secara digital, termasuk pencatatan buku, proses peminjaman-pengembalian, dan pembuatan laporan.  
Semua data tersimpan dalam **database MySQL**, sehingga lebih aman, efisien, dan mudah dikembangkan.

---

## 🧩 Features

- 🔐 Login terpisah untuk Admin dan Mahasiswa (User)
- 📖 CRUD Buku (Tambah, Edit, Hapus, Cari)
- 🧑‍🎓 Registrasi & manajemen data anggota
- 📚 Peminjaman dan Pengembalian otomatis dengan batas waktu 7 hari
- 💸 Kalkulasi denda keterlambatan
- 📊 Laporan aktivitas peminjaman berbasis tanggal
- 🔔 Notifikasi JavaFX (alert, konfirmasi, validasi input)

---

## 🏗️ System Architecture

Aplikasi ini menggunakan pola desain **MVC (Model-View-Controller)**:

- **Model**: Representasi data seperti `Book`, `Member`, `Borrowing`
- **View**: Antarmuka pengguna menggunakan JavaFX (.fxml)
- **Controller**: Mengatur logika interaksi pengguna dan koneksi ke model

Semua data (buku, user, transaksi) disimpan di **MySQL** dan diakses melalui **JDBC (Java Database Connectivity)** dengan class helper khusus `DatabaseUtil.java`.

---

## 🛠️ Built With

- **Java 11+**
- **JavaFX** (FXML & SceneBuilder)
- **MySQL** (dengan JDBC)
- **Maven** untuk dependency management
- **IntelliJ IDEA / Eclipse** sebagai IDE
  
---

## 🧠 Learning Outcomes

✅ Struktur kendali, fungsi, dan tipe data  
✅ Konsep OOP: class, inheritance, polymorphism  
✅ Penerapan MVC dalam proyek nyata  
✅ Penggunaan JavaFX sebagai UI desktop  
✅ **Integrasi database MySQL via JDBC**  
✅ Debugging dan pengujian modular  

