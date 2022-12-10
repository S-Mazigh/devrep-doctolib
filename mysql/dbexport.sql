-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: localhost    Database: devrepDoctolib
-- ------------------------------------------------------
-- Server version	8.0.31-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (27);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rdv`
--

DROP TABLE IF EXISTS `rdv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rdv` (
  `id` bigint NOT NULL,
  `daterdv` datetime DEFAULT NULL,
  `patient_id` bigint DEFAULT NULL,
  `pro_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeulhdwfo9l47hk12ymjkxuq83` (`patient_id`),
  KEY `FK8k2880io7rpnh6mvwojx0t2dc` (`pro_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rdv`
--

LOCK TABLES `rdv` WRITE;
/*!40000 ALTER TABLE `rdv` DISABLE KEYS */;
INSERT INTO `rdv` VALUES (3,'2022-12-09 10:00:00',1,2),(4,'2022-12-12 10:00:00',1,2),(5,'2022-12-15 09:00:00',1,2),(6,'2022-12-15 11:00:00',1,2),(7,'2022-12-09 15:00:00',1,2),(8,'2022-12-13 15:00:00',1,2),(9,'2022-12-14 11:00:00',1,2),(10,'2022-12-14 14:00:00',1,2),(11,'2022-12-14 15:00:00',1,2),(12,'2022-12-14 16:00:00',1,2),(13,'2022-12-14 13:00:00',1,2),(14,'2022-12-14 17:00:00',1,2),(15,'2022-12-14 12:00:00',1,2),(16,'2022-12-14 10:00:00',1,2),(24,'2022-12-13 10:00:00',17,22),(25,'2022-12-16 11:00:00',17,22),(26,'2022-12-15 13:00:00',17,2);
/*!40000 ALTER TABLE `rdv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur` (
  `id` bigint NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `authority` varchar(10) NOT NULL,
  `domaine` varchar(25) DEFAULT NULL,
  `duree_rdv` int DEFAULT NULL,
  `email` varchar(70) NOT NULL,
  `horaires` varchar(500) DEFAULT NULL,
  `motdepasse` varchar(64) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `tel` varchar(16) DEFAULT NULL,
  `pays` varchar(255) DEFAULT NULL,
  `prenom` varchar(50) NOT NULL,
  `ville` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

LOCK TABLES `utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` VALUES (1,NULL,'PATIENT',NULL,0,'mazigh.saoudi@patient.com',NULL,'$2a$10$yQQywOB9RpOBIIMYHFq36OeyhR0gLXfRX7oaekBVfIfO7R.tzBwCK','Saoudi',NULL,NULL,'Mazigh',NULL),(2,'45 avenue maréchal foch','PRO','Diabetologue',1,'mazigh.saoudi@pro.com','Lundi[08:00>16:00]&Mardi[09:00>14:00]&Mercredi[10:00>17:00]&Jeudi[09:00>18:00]&Vendredi[08:00>13:00]','$2a$10$lBg8EbaxYIjkaFlumjJoHu1q5Oj1zXbvGu/x6fabQpdakUhTTG9Ei','Saoudi','','France','Mazigh','Argenteuil'),(17,NULL,'PATIENT',NULL,0,'polit.titouan@patient.com',NULL,'$2a$10$ICSLE3UzWaLkYG8INzJCOOEKolt0XeDu2NqfbC08zvZpjSxNcmf96','Polit',NULL,NULL,'Titouan',NULL),(18,NULL,'PRO','Generaliste',1,'ronald@pro.com','Lundi[00:00>00:00]&Mardi[00:00>00:00]&Mercredi[00:00>00:00]&Jeudi[00:00>00:00]&Vendredi[00:00>00:00]','$2a$10$Ey4CH/cmARjT.Mr.bLIfh.CQbCanfD2UJiEXwnlx2QS6EXjBqLQSe','Cristiano',NULL,NULL,'Ronaldo',NULL),(19,'Parc des prince','PRO','Ophtalmologue',1,'jesuisleplusfort@pro.com','Lundi[10:00>16:00]&Mardi[10:00>14:00]&Mercredi[11:00>15:00]&Jeudi[00:00>00:00]&Vendredi[10:00>13:00]','$2a$10$tM6l9hjmbD/LpVI1v1Q24OeAb74EHuthlq3.CCWm8jWNK3i2pqxmG','Kylian','','France','Mbappe','Paris'),(20,'Parc des prince','PRO','Hematologue',1,'handgod@pro.com','Lundi[09:00>12:00]&Mardi[11:00>15:00]&Mercredi[11:00>16:00]&Jeudi[00:00>00:00]&Vendredi[14:00>16:00]','$2a$10$ANgqww0teOcmSOKCbAPIj.PHRN45.FS1/n80mkQaRNheheBcddAGm','Lionel','10','France','Messi','Paris'),(21,'Stanford bridge','PRO','Gynecologue',1,'ziyech@pro.com','Lundi[10:00>12:00]&Mardi[11:00>14:00]&Mercredi[15:00>17:00]&Jeudi[10:00>14:00]&Vendredi[12:00>10:00]','$2a$10$A8TfN0ScjBdKoSXo39icDeuyUuFuFGZP6tOjIZ/OhQWfI/qO0Qgli','Hakim','22','France','Ziyech','LondreFrancais'),(22,'','PRO','Allergologue',1,'polit@pro.fr','Lundi[12:00>12:00]&Mardi[08:00>15:00]&Mercredi[08:00>15:00]&Jeudi[09:00>16:00]&Vendredi[09:00>13:00]','$2a$10$h6B5MKLfPVcbocg1mjELzuyRyrGkDdLmU6hzgOiYqBuiX2BvrvAHO','Polit','','France','Titouan','Paris');
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'devrepDoctolib'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-10 15:58:06
