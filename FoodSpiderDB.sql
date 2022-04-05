CREATE DATABASE  IF NOT EXISTS `assignment_2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `assignment_2`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: assignment_2
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `food_item`
--

DROP TABLE IF EXISTS `food_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_item` (
  `id` varchar(255) NOT NULL,
  `category` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `restaurantid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ndiw4mxsoyibm2l3dqlwhwh` (`restaurantid`),
  CONSTRAINT `FK3ndiw4mxsoyibm2l3dqlwhwh` FOREIGN KEY (`restaurantid`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_item`
--

LOCK TABLES `food_item` WRITE;
/*!40000 ALTER TABLE `food_item` DISABLE KEYS */;
INSERT INTO `food_item` VALUES ('07cbc854-0d3e-4e8d-bed1-99af5e7728e5',2,'Paste gustoase','https://retete.unica.ro/wp-content/uploads/2010/09/paste-bolognese.jpg','Paste bologneze',30.5,'eb1629fb-0814-44e7-885f-cb7deb032e28'),('5c864482-db23-4db2-afca-20fdc7e6ae96',2,'Foarte Gustosi','https://jamilacuisine.ro/wp-content/uploads/2016/03/Papanasi-prajiti-500x478.jpg','Papanasi cu Gem',20,'77ac5e04-9607-44e2-810b-d7747ea8125b'),('6a5e535b-33e3-4192-9b7f-f6b59f9cea41',0,'Din alea scumpe de la Auchan','https://assets.sport.ro/assets/foodstory2019/2014/07/23/image_galleries/4761/de-ce-laptele-cu-cereale-nu-este-cel-mai-sanatos-mic-dejun_size1.jpg','Cereale cu lapte',20.59,'eb1629fb-0814-44e7-885f-cb7deb032e28'),('95fb88b9-b028-4b0f-ac8f-dfaa40c95425',1,'Yummy','https://jamilacuisine.ro/wp-content/uploads/2019/07/Ciorba-de-pui-olteneasca.jpg','Ciorba de pui',50.99,'eb1629fb-0814-44e7-885f-cb7deb032e28'),('bf95faee-b283-4241-b258-040bf07f3ebf',2,'Very Giuly','https://retete.unica.ro/wp-content/uploads/2013/10/lasagna.jpg','Lasagna bolognese',9999,'eb1629fb-0814-44e7-885f-cb7deb032e28'),('d34de84a-cd72-4026-b08c-b04fe1384731',1,'Ceva Gustos','https://savoriurbane.com/wp-content/uploads/2014/07/spaghetti-bolognese-rapide-1.jpg','Paste Bologneze',29.99,'77ac5e04-9607-44e2-810b-d7747ea8125b'),('d3b73754-4643-418f-bab2-f499c62c6994',0,'Oua smechere','https://www.simplyrecipes.com/thmb/pPVeaZf3ASDmLn7APVX7wmdkEvc=/2832x2832/smart/filters:no_upscale()/Simply-Recipes-Eggs-Benedict-LEAD-2-a0ba4b628b214482973087b6bbce93a2.jpg','Eggs Benedict',29.39,'eb1629fb-0814-44e7-885f-cb7deb032e28'),('f83e30d7-aafa-4224-90e9-409204cf4f72',0,'Yummy','https://cdn.clickpoftabuna.ro/unsafe/840x630/top/https://i0.1616.ro/media/501/2841/34911/18419087/1/clatite-cu-gem.jpg','Clatite cu gem',19.99,'eb1629fb-0814-44e7-885f-cb7deb032e28');
/*!40000 ALTER TABLE `food_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_item_order`
--

DROP TABLE IF EXISTS `food_item_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_item_order` (
  `order_id` varchar(255) NOT NULL,
  `food_item_id` varchar(255) NOT NULL,
  KEY `FKm5tr7ydjdyvsuc7m4xt757jk8` (`food_item_id`),
  KEY `FKhmfub3uifysjmxmnocrattbk1` (`order_id`),
  CONSTRAINT `FKhmfub3uifysjmxmnocrattbk1` FOREIGN KEY (`order_id`) REFERENCES `orderuru` (`id`),
  CONSTRAINT `FKm5tr7ydjdyvsuc7m4xt757jk8` FOREIGN KEY (`food_item_id`) REFERENCES `food_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_item_order`
--

LOCK TABLES `food_item_order` WRITE;
/*!40000 ALTER TABLE `food_item_order` DISABLE KEYS */;
INSERT INTO `food_item_order` VALUES ('4ebdb489-b9aa-4f43-a76a-839399124213','f83e30d7-aafa-4224-90e9-409204cf4f72'),('4ebdb489-b9aa-4f43-a76a-839399124213','d3b73754-4643-418f-bab2-f499c62c6994'),('4ebdb489-b9aa-4f43-a76a-839399124213','95fb88b9-b028-4b0f-ac8f-dfaa40c95425'),('9239a010-6052-4d6a-ad31-089857012762','6a5e535b-33e3-4192-9b7f-f6b59f9cea41'),('9239a010-6052-4d6a-ad31-089857012762','d3b73754-4643-418f-bab2-f499c62c6994'),('9239a010-6052-4d6a-ad31-089857012762','f83e30d7-aafa-4224-90e9-409204cf4f72'),('9239a010-6052-4d6a-ad31-089857012762','95fb88b9-b028-4b0f-ac8f-dfaa40c95425'),('00cb703c-193e-4181-9061-6f9caed72cc1','6a5e535b-33e3-4192-9b7f-f6b59f9cea41'),('00cb703c-193e-4181-9061-6f9caed72cc1','6a5e535b-33e3-4192-9b7f-f6b59f9cea41'),('00cb703c-193e-4181-9061-6f9caed72cc1','95fb88b9-b028-4b0f-ac8f-dfaa40c95425'),('00cb703c-193e-4181-9061-6f9caed72cc1','07cbc854-0d3e-4e8d-bed1-99af5e7728e5'),('46643181-6237-451f-a1d8-f20d9b9d64a8','95fb88b9-b028-4b0f-ac8f-dfaa40c95425'),('46643181-6237-451f-a1d8-f20d9b9d64a8','6a5e535b-33e3-4192-9b7f-f6b59f9cea41'),('06b35eab-1640-49b6-b40b-a62d3e7220e2','d34de84a-cd72-4026-b08c-b04fe1384731'),('06b35eab-1640-49b6-b40b-a62d3e7220e2','d34de84a-cd72-4026-b08c-b04fe1384731'),('06b35eab-1640-49b6-b40b-a62d3e7220e2','5c864482-db23-4db2-afca-20fdc7e6ae96'),('0df5fc54-8c07-4d0b-a9cd-276e1bf401a7','d3b73754-4643-418f-bab2-f499c62c6994'),('0df5fc54-8c07-4d0b-a9cd-276e1bf401a7','95fb88b9-b028-4b0f-ac8f-dfaa40c95425'),('0df5fc54-8c07-4d0b-a9cd-276e1bf401a7','07cbc854-0d3e-4e8d-bed1-99af5e7728e5'),('0df5fc54-8c07-4d0b-a9cd-276e1bf401a7','bf95faee-b283-4241-b258-040bf07f3ebf');
/*!40000 ALTER TABLE `food_item_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderuru`
--

DROP TABLE IF EXISTS `orderuru`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderuru` (
  `id` varchar(255) NOT NULL,
  `order_status` int NOT NULL,
  `customerid` varchar(255) DEFAULT NULL,
  `restaurantid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdft8fs2063hnqxdjh3cpj8mox` (`customerid`),
  KEY `FKer9qwl5i48wv96jkvwwm1o87w` (`restaurantid`),
  CONSTRAINT `FKdft8fs2063hnqxdjh3cpj8mox` FOREIGN KEY (`customerid`) REFERENCES `user_base` (`id`),
  CONSTRAINT `FKer9qwl5i48wv96jkvwwm1o87w` FOREIGN KEY (`restaurantid`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderuru`
--

LOCK TABLES `orderuru` WRITE;
/*!40000 ALTER TABLE `orderuru` DISABLE KEYS */;
INSERT INTO `orderuru` VALUES ('00cb703c-193e-4181-9061-6f9caed72cc1',3,'5c49d688-3c37-47d2-8e67-ba79dcfee3ff','eb1629fb-0814-44e7-885f-cb7deb032e28'),('06b35eab-1640-49b6-b40b-a62d3e7220e2',3,'5c49d688-3c37-47d2-8e67-ba79dcfee3ff','77ac5e04-9607-44e2-810b-d7747ea8125b'),('0df5fc54-8c07-4d0b-a9cd-276e1bf401a7',3,'5c49d688-3c37-47d2-8e67-ba79dcfee3ff','eb1629fb-0814-44e7-885f-cb7deb032e28'),('46643181-6237-451f-a1d8-f20d9b9d64a8',3,'44c22ead-7dc8-4516-9157-0661a65388e8','eb1629fb-0814-44e7-885f-cb7deb032e28'),('4ebdb489-b9aa-4f43-a76a-839399124213',4,'5c49d688-3c37-47d2-8e67-ba79dcfee3ff','eb1629fb-0814-44e7-885f-cb7deb032e28'),('9239a010-6052-4d6a-ad31-089857012762',3,'5c49d688-3c37-47d2-8e67-ba79dcfee3ff','eb1629fb-0814-44e7-885f-cb7deb032e28');
/*!40000 ALTER TABLE `orderuru` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` varchar(255) NOT NULL,
  `delivery_zones` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `administratorid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_i6u3x7opncroyhd755ejknses` (`name`),
  KEY `FKgaskqsam9h84yrykd3twh384i` (`administratorid`),
  CONSTRAINT `FKgaskqsam9h84yrykd3twh384i` FOREIGN KEY (`administratorid`) REFERENCES `user_base` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES ('77ac5e04-9607-44e2-810b-d7747ea8125b','Gheorgheni,Intre_Lacuri,Marasti','Cluj-Napoca','Alfie','804239fd-a00b-48f2-9acd-0ea179e9b974'),('c8d38ee8-fbc8-4f5f-8992-60644a2a6433','Gheorgheni,Intre Lacuri,Undeva Interesant','La mama manu\'','Crishpy Shtore','3b355e25-4769-43aa-a3f8-26d7fc1802ec'),('eb1629fb-0814-44e7-885f-cb7deb032e28','Berceni,Feleac,Floresti','Undeva nice','BOwl','70c7d3b5-4408-4c57-885e-a7e19a1dfc58');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant_categories`
--

DROP TABLE IF EXISTS `restaurant_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_categories` (
  `restaurant_id` varchar(255) NOT NULL,
  `categories` int NOT NULL,
  KEY `FKqtq0jri202b1jscqcpp8pywt2` (`restaurant_id`),
  CONSTRAINT `FKqtq0jri202b1jscqcpp8pywt2` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_categories`
--

LOCK TABLES `restaurant_categories` WRITE;
/*!40000 ALTER TABLE `restaurant_categories` DISABLE KEYS */;
INSERT INTO `restaurant_categories` VALUES ('eb1629fb-0814-44e7-885f-cb7deb032e28',0),('eb1629fb-0814-44e7-885f-cb7deb032e28',1),('eb1629fb-0814-44e7-885f-cb7deb032e28',2),('77ac5e04-9607-44e2-810b-d7747ea8125b',1),('77ac5e04-9607-44e2-810b-d7747ea8125b',2),('c8d38ee8-fbc8-4f5f-8992-60644a2a6433',0),('c8d38ee8-fbc8-4f5f-8992-60644a2a6433',3),('c8d38ee8-fbc8-4f5f-8992-60644a2a6433',4),('c8d38ee8-fbc8-4f5f-8992-60644a2a6433',5);
/*!40000 ALTER TABLE `restaurant_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_base`
--

DROP TABLE IF EXISTS `user_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_base` (
  `dtype` varchar(31) NOT NULL,
  `id` varchar(255) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kahemtx24qrkfr91oaruh78an` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_base`
--

LOCK TABLES `user_base` WRITE;
/*!40000 ALTER TABLE `user_base` DISABLE KEYS */;
INSERT INTO `user_base` VALUES ('Administrator','3b355e25-4769-43aa-a3f8-26d7fc1802ec','TestRestaurant@test.test','TestRestaurant','TestRestaurant','A0U+yUekGC4=','TestRestaurant'),('Administrator','3b70e903-391e-4cee-b194-e4351287450c','test@testettete.tete','Test','test','QsdYP4UBqzU=','tetetetete'),('Customer','44c22ead-7dc8-4516-9157-0661a65388e8','anonim@ceva.mail','Anonim','Anonimus','XOtuP+GRwTI=','anonim'),('Customer','5c49d688-3c37-47d2-8e67-ba79dcfee3ff','man_rares2001@yahoo.com','Raresucu','Manucu','cPxGpeLmIFg=','raresman'),('Administrator','612c2e34-b644-4051-bb09-17404a9a85a8','man_rares2001@yahoo.com','Rares','Man','oV1/VpGeeMV8fNv5I8w7JQ==','raresman2'),('Administrator','70c7d3b5-4408-4c57-885e-a7e19a1dfc58','manrares2001@gmail.com','RaresucuAdminucu','Manucu','kHtaKRf8jFnBe+p05QBsMw==','raresman1'),('Administrator','804239fd-a00b-48f2-9acd-0ea179e9b974','iulia.varady@yahoo.com','Iulia','Varady','354gZmxN9oQ=','iuliavarady'),('Customer','8c3e1bb7-4b2c-4798-89bf-b053832eb9fe','tetete@tete.tete','tete','tetet','tYf605mhlXM=','tetete'),('Administrator','aedd599d-0f21-4a7a-8f76-90178a94187a','test@test.test','Test','tet','N14wKEV4h+E=','tete');
/*!40000 ALTER TABLE `user_base` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-06  0:57:47
