CREATE DATABASE  IF NOT EXISTS `ocean_view_hotel_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ocean_view_hotel_db`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: ocean_view_hotel_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amenity`
--

DROP TABLE IF EXISTS `amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenity` (
  `amenity_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `cost` double(10,2) NOT NULL,
  PRIMARY KEY (`amenity_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenity`
--

LOCK TABLES `amenity` WRITE;
/*!40000 ALTER TABLE `amenity` DISABLE KEYS */;
INSERT INTO `amenity` VALUES (1,'Pool',15000.00),(2,'WiFi',2500.00),(5,'Breakfast',5000.00),(6,'Spa',20000.00),(7,'IronBoard',7000.00),(9,'Minibar',10000.00);
/*!40000 ALTER TABLE `amenity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_number` varchar(20) NOT NULL,
  `room_type_id` int NOT NULL,
  `floor_num` int NOT NULL,
  `room_status` enum('Available','Unavailable','Maintenance') DEFAULT 'Available',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_number` (`room_number`),
  KEY `fk_room_type` (`room_type_id`),
  CONSTRAINT `fk_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'101',1,1,'Available','2026-02-20 13:05:08','2026-02-20 13:05:08'),(2,'102',1,1,'Available','2026-02-20 13:05:08','2026-02-20 13:05:08'),(3,'201',2,2,'Available','2026-02-20 13:05:08','2026-02-20 13:05:08'),(4,'202',2,2,'Unavailable','2026-02-20 13:05:08','2026-02-20 13:05:08'),(5,'301',3,3,'Available','2026-02-20 13:05:08','2026-02-20 13:05:08'),(8,'508',1,5,'Available','2026-02-25 16:11:19','2026-02-25 16:11:19'),(9,'501',3,5,'Available','2026-02-26 10:35:11','2026-02-26 10:35:11'),(10,'110',1,1,'Maintenance','2026-02-26 11:46:00','2026-02-26 11:46:00'),(11,'1201',3,12,'Available','2026-02-27 14:25:21','2026-02-27 14:25:21'),(12,'1104',2,11,'Maintenance','2026-02-27 14:27:19','2026-02-27 14:27:19'),(14,'1102',2,11,'Maintenance','2026-02-27 14:29:03','2026-02-27 14:29:03'),(15,'104',1,1,'Available','2026-02-27 14:30:08','2026-02-27 14:30:08');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_img`
--

DROP TABLE IF EXISTS `room_img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_img` (
  `image_id` int NOT NULL AUTO_INCREMENT,
  `room_type_id` int NOT NULL,
  `image_path` varchar(255) NOT NULL,
  `alt` varchar(255) DEFAULT 'Room Image Not Available',
  PRIMARY KEY (`image_id`),
  KEY `fk_room_img_room_type` (`room_type_id`),
  CONSTRAINT `fk_room_img_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_img`
--

LOCK TABLES `room_img` WRITE;
/*!40000 ALTER TABLE `room_img` DISABLE KEYS */;
INSERT INTO `room_img` VALUES (23,1,'images/room-uploads/standard-room-1.jpg','Standard Room Image'),(24,2,'images/room-uploads/deluxe-room-2.jpg','Deluxe Room Image'),(25,3,'images/room-uploads/suite-room-3.jpg','Suite Room Image');
/*!40000 ALTER TABLE `room_img` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `room_type_id` int NOT NULL AUTO_INCREMENT,
  `name` enum('Standard','Deluxe','Suite') NOT NULL,
  `price_per_night` decimal(10,2) NOT NULL,
  `description` text,
  `bedding` enum('Single','Double','Twin','King') NOT NULL,
  `max_occupancy` int NOT NULL,
  `total_rooms` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`room_type_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` VALUES (1,'Standard',5000.00,'Cozy standard room','Double',2,10,'2026-02-20 13:05:05','2026-03-02 13:47:55'),(2,'Deluxe',12750.00,'Spacious deluxe room','King',3,5,'2026-02-20 13:05:05','2026-03-02 13:48:08'),(3,'Suite',25500.00,'Luxury suite with living area','King',4,2,'2026-02-20 13:05:05','2026-03-02 13:48:19');
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type_amenity`
--

DROP TABLE IF EXISTS `room_type_amenity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type_amenity` (
  `room_type_id` int NOT NULL,
  `amenity_id` int NOT NULL,
  PRIMARY KEY (`room_type_id`,`amenity_id`),
  KEY `fk_rta_amenity` (`amenity_id`),
  CONSTRAINT `fk_rta_amenity` FOREIGN KEY (`amenity_id`) REFERENCES `amenity` (`amenity_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rta_room_type` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type_amenity`
--

LOCK TABLES `room_type_amenity` WRITE;
/*!40000 ALTER TABLE `room_type_amenity` DISABLE KEYS */;
INSERT INTO `room_type_amenity` VALUES (1,1),(2,1),(3,1),(1,2),(2,2),(3,2),(2,7),(3,7),(3,9);
/*!40000 ALTER TABLE `room_type_amenity` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-04 23:06:06
