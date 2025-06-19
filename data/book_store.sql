-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jun 19, 2025 at 12:55 PM
-- Server version: 8.0.30
-- PHP Version: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `book_store`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `year_published` year DEFAULT NULL,
  `isbn` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `title`, `author`, `publisher`, `year_published`, `isbn`, `created_at`, `updated_at`, `category_id`) VALUES
(1, 'Cantik itu Luka', 'Eka Kurniawan', 'Gramedia', 2018, '9786020312583', '2025-06-19 09:46:31', '2025-06-19 09:46:31', 3),
(3, '\"3726\" MDPL', 'Nurwina Sari', 'Romancious', 2024, '9786233102599', '2025-06-19 10:16:04', '2025-06-19 10:16:04', 3),
(4, 'Filosofi Teras', 'Henry Manampiring', 'Penerbit Buku Kompas', 2023, '582312136', '2025-06-19 10:20:04', '2025-06-19 10:20:04', 4),
(5, 'One Piece', 'Eiichiro Oda', 'Elex Media Komputindo', 2023, '9789792039504', '2025-06-19 10:22:27', '2025-06-19 10:22:27', 7),
(6, 'Si Juki Komik Strip', 'Faza Meonk', 'Kawah Media', 2014, '9786022201397', '2025-06-19 10:24:34', '2025-06-19 10:24:34', 8),
(7, 'The Garden of Words', 'Shinkai Makoto', 'm&c!', 2024, '9786230314957', '2025-06-19 10:28:34', '2025-06-19 10:28:34', 3),
(8, 'Your Name', 'Shinkai Makoto', 'HARU', 2020, '9786237351207', '2025-06-19 10:29:59', '2025-06-19 10:29:59', 3),
(9, 'Weathering with You', 'Shinkai Makoto', 'HARU', 2021, '9786237351627', '2025-06-19 10:31:10', '2025-06-19 12:07:00', 3);

-- --------------------------------------------------------

--
-- Table structure for table `book_items`
--

CREATE TABLE `book_items` (
  `id` int NOT NULL,
  `book_id` int NOT NULL,
  `inventory_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `book_condition` enum('new','good','damaged') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'good',
  `is_available` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book_items`
--

INSERT INTO `book_items` (`id`, `book_id`, `inventory_code`, `book_condition`, `is_available`) VALUES
(1, 1, 'BOOK-1-809F0D2D', 'good', 1),
(2, 1, 'BOOK-1-E5F0974D', 'good', 1),
(3, 1, 'BOOK-1-F8D463F8', 'good', 1),
(4, 1, 'BOOK-1-08E7D80D', 'good', 1),
(5, 1, 'BOOK-1-B588988C', 'good', 1),
(6, 1, 'BOOK-1-CB32C8B4', 'good', 1),
(7, 1, 'BOOK-1-C9B26AAD', 'good', 1),
(8, 1, 'BOOK-1-66722330', 'good', 1),
(9, 1, 'BOOK-1-79E0B780', 'good', 1),
(10, 1, 'BOOK-1-5FDD6330', 'good', 1),
(21, 3, 'BOOK-3-4E06698B', 'damaged', 1),
(22, 3, 'BOOK-3-2BE2499F', 'good', 1),
(23, 3, 'BOOK-3-9F3621F2', 'good', 1),
(24, 3, 'BOOK-3-0E82E1CD', 'good', 1),
(25, 3, 'BOOK-3-6A84BC9D', 'good', 1),
(26, 3, 'BOOK-3-33839BE3', 'good', 1),
(27, 3, 'BOOK-3-A4CBB7BF', 'good', 1),
(28, 3, 'BOOK-3-CDD8525A', 'good', 1),
(29, 3, 'BOOK-3-0AF60D07', 'good', 1),
(30, 3, 'BOOK-3-2A84FA07', 'good', 1),
(31, 4, 'BOOK-4-C6C93B2A', 'good', 1),
(32, 4, 'BOOK-4-1D61DB39', 'good', 1),
(33, 4, 'BOOK-4-01F59EDE', 'good', 1),
(34, 4, 'BOOK-4-3B2D152C', 'good', 1),
(35, 4, 'BOOK-4-5D51D2B3', 'good', 1),
(36, 4, 'BOOK-4-736054A6', 'good', 1),
(37, 4, 'BOOK-4-92E51335', 'good', 1),
(38, 4, 'BOOK-4-FBBA924F', 'good', 1),
(39, 4, 'BOOK-4-61CBCF0C', 'good', 1),
(40, 4, 'BOOK-4-CB889648', 'good', 1),
(41, 5, 'BOOK-5-A4DB2A69', 'good', 1),
(42, 5, 'BOOK-5-39F588EE', 'good', 1),
(43, 5, 'BOOK-5-E5E2C030', 'good', 1),
(44, 5, 'BOOK-5-7D8BB7EB', 'good', 1),
(45, 5, 'BOOK-5-63E8F2CB', 'good', 1),
(46, 5, 'BOOK-5-8D4EF492', 'good', 1),
(47, 5, 'BOOK-5-BBF24DFE', 'good', 1),
(48, 5, 'BOOK-5-3CFF2028', 'good', 1),
(49, 5, 'BOOK-5-95BD022F', 'good', 1),
(50, 5, 'BOOK-5-9675E809', 'good', 1),
(51, 6, 'BOOK-6-7B3E5AE1', 'good', 1),
(52, 6, 'BOOK-6-6593B7A0', 'good', 1),
(53, 6, 'BOOK-6-22C5A4BE', 'good', 1),
(54, 6, 'BOOK-6-FAF46C68', 'good', 1),
(55, 6, 'BOOK-6-B7CBE45C', 'good', 1),
(56, 6, 'BOOK-6-9CABE2AE', 'good', 1),
(57, 6, 'BOOK-6-513E3AB2', 'good', 1),
(58, 6, 'BOOK-6-C2B0FBD7', 'good', 1),
(59, 6, 'BOOK-6-46901232', 'good', 1),
(60, 6, 'BOOK-6-A54D9978', 'good', 1),
(61, 7, 'BOOK-7-C2DE73BD', 'good', 0),
(62, 7, 'BOOK-7-8CE9A9B1', 'good', 1),
(63, 7, 'BOOK-7-486430AF', 'good', 1),
(64, 7, 'BOOK-7-2634490E', 'good', 1),
(65, 7, 'BOOK-7-7158DF51', 'good', 1),
(66, 7, 'BOOK-7-2E53AE85', 'good', 1),
(67, 7, 'BOOK-7-E54EDDFA', 'good', 1),
(68, 7, 'BOOK-7-2C8AFF39', 'good', 1),
(69, 7, 'BOOK-7-8EB67EDA', 'good', 1),
(70, 7, 'BOOK-7-BAE4BE29', 'good', 1),
(71, 8, 'BOOK-8-48D832E1', 'good', 1),
(72, 8, 'BOOK-8-AA3C74F6', 'good', 1),
(73, 8, 'BOOK-8-2C413DA9', 'good', 1),
(74, 8, 'BOOK-8-B18E25DA', 'good', 1),
(75, 8, 'BOOK-8-917DCE23', 'good', 1),
(76, 8, 'BOOK-8-D8F9C47F', 'good', 1),
(77, 8, 'BOOK-8-06DB38E4', 'good', 1),
(78, 8, 'BOOK-8-B72E6B66', 'good', 1),
(79, 8, 'BOOK-8-4F463ABB', 'good', 1),
(80, 8, 'BOOK-8-F7C732E0', 'good', 1),
(81, 9, 'BOOK-9-4C8E5C9F', 'good', 1),
(82, 9, 'BOOK-9-90B80B88', 'good', 1),
(83, 9, 'BOOK-9-B4667B2B', 'good', 1),
(84, 9, 'BOOK-9-A269EE18', 'good', 1),
(85, 9, 'BOOK-9-6794D1E3', 'good', 1),
(86, 9, 'BOOK-9-8F3109C1', 'good', 1),
(87, 9, 'BOOK-9-3CABD91A', 'good', 1),
(88, 9, 'BOOK-9-3BE682A2', 'good', 1),
(89, 9, 'BOOK-9-2ADF20B1', 'good', 1),
(90, 9, 'BOOK-9-FD4A218E', 'good', 1);

-- --------------------------------------------------------

--
-- Table structure for table `borrowings`
--

CREATE TABLE `borrowings` (
  `id` int NOT NULL,
  `users_id` int NOT NULL,
  `borrow_date` date NOT NULL,
  `return_due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('borrowed','returned','late') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'borrowed',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowings`
--

INSERT INTO `borrowings` (`id`, `users_id`, `borrow_date`, `return_due_date`, `return_date`, `status`, `created_at`, `updated_at`) VALUES
(1, 7, '2025-06-19', '2025-06-11', '2025-06-19', 'returned', '2025-06-19 09:47:01', '2025-06-19 11:35:48'),
(2, 7, '2025-06-19', '2025-06-11', '2025-06-19', 'returned', '2025-06-19 10:11:46', '2025-06-19 11:35:55'),
(3, 8, '2025-06-19', '2025-07-03', '2025-06-19', 'returned', '2025-06-19 10:34:43', '2025-06-19 10:41:32'),
(4, 7, '2025-06-19', '2025-06-10', '2025-06-19', 'returned', '2025-06-19 10:36:12', '2025-06-19 11:38:26'),
(5, 7, '2025-06-19', '2025-07-03', NULL, 'borrowed', '2025-06-19 10:36:23', '2025-06-19 10:36:23'),
(6, 7, '2025-06-19', '2025-07-03', NULL, 'borrowed', '2025-06-19 10:36:28', '2025-06-19 10:36:28');

-- --------------------------------------------------------

--
-- Table structure for table `borrowing_details`
--

CREATE TABLE `borrowing_details` (
  `id` int NOT NULL,
  `borrowing_id` int NOT NULL,
  `book_item_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowing_details`
--

INSERT INTO `borrowing_details` (`id`, `borrowing_id`, `book_item_id`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 31),
(4, 4, 21),
(5, 5, 1),
(6, 6, 61);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Cerita'),
(2, 'Baru'),
(3, 'Novel'),
(4, 'Psikologi'),
(5, 'Fiksi'),
(6, 'Non-Fiksi'),
(7, 'Manga'),
(8, 'Komik'),
(9, 'Pendidikan');

-- --------------------------------------------------------

--
-- Table structure for table `fines`
--

CREATE TABLE `fines` (
  `id` int NOT NULL,
  `borrowing_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `paid` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fines`
--

INSERT INTO `fines` (`id`, `borrowing_id`, `amount`, `paid`, `created_at`) VALUES
(2, 1, '8000.00', 0, '2025-06-19 11:35:48'),
(3, 2, '8000.00', 0, '2025-06-19 11:35:55'),
(4, 4, '9000.00', 0, '2025-06-19 11:38:26');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `student_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` enum('admin','mahasiswa') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'mahasiswa',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `username`, `student_number`, `phone`, `address`, `password`, `role`, `created_at`, `updated_at`) VALUES
(2, 'Dendy', 'biaachan3', '', NULL, NULL, 'dendy2001', 'admin', '2025-06-17 05:38:35', '2025-06-17 05:38:35'),
(5, 'Dhyaksa', 'Mori', NULL, NULL, NULL, 'CrosszBuild', 'admin', '2025-06-18 09:43:15', '2025-06-19 11:24:31'),
(7, 'Arsya', 'Ayase', '056', '+62 812-7052-8831', 'Malang', '', 'mahasiswa', '2025-06-19 08:33:33', '2025-06-19 10:35:42'),
(8, 'Ismail', 'Mail', '013', '+62 853-9720-6533', 'Malang', '', 'mahasiswa', '2025-06-19 10:34:20', '2025-06-19 10:34:20'),
(9, 'Arsya', 'Ayasa Yoasobi', NULL, NULL, NULL, 'sya1234', 'admin', '2025-06-19 10:46:01', '2025-06-19 10:47:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `book_items`
--
ALTER TABLE `book_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `inventory_code` (`inventory_code`),
  ADD KEY `book_id` (`book_id`);

--
-- Indexes for table `borrowings`
--
ALTER TABLE `borrowings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `borrow_user` (`users_id`);

--
-- Indexes for table `borrowing_details`
--
ALTER TABLE `borrowing_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `borrowing_id` (`borrowing_id`),
  ADD KEY `book_item_id` (`book_item_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fines`
--
ALTER TABLE `fines`
  ADD PRIMARY KEY (`id`),
  ADD KEY `borrowing_id` (`borrowing_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `book_items`
--
ALTER TABLE `book_items`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `borrowings`
--
ALTER TABLE `borrowings`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `borrowing_details`
--
ALTER TABLE `borrowing_details`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `fines`
--
ALTER TABLE `fines`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `book_items`
--
ALTER TABLE `book_items`
  ADD CONSTRAINT `book_items_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`);

--
-- Constraints for table `borrowings`
--
ALTER TABLE `borrowings`
  ADD CONSTRAINT `borrow_user` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `borrowing_details`
--
ALTER TABLE `borrowing_details`
  ADD CONSTRAINT `borrowing_details_ibfk_1` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`),
  ADD CONSTRAINT `borrowing_details_ibfk_2` FOREIGN KEY (`book_item_id`) REFERENCES `book_items` (`id`);

--
-- Constraints for table `fines`
--
ALTER TABLE `fines`
  ADD CONSTRAINT `fines_ibfk_1` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
