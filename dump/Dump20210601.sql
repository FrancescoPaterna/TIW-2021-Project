-- MySQL dump 10.13  Distrib 5.7.34, for Linux (x86_64)
--
-- Host: localhost    Database: projectdb
-- ------------------------------------------------------
-- Server version	5.7.34-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `IDprofessor` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDprofessor_idx` (`IDprofessor`),
  CONSTRAINT `IDprofessor` FOREIGN KEY (`IDprofessor`) REFERENCES `user` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'TECNOLOGIE INFORMATICHE PER IL WEB',10123),(2,'ADVANCED WEB TECHNOLOGIES',10123),(3,'FONDAMENTI DI INFORMATICA',10123),(4,'INGEGNERIA DEL SOFTWARE',10125),(5,'DATABASE 2',10125),(6,'INGEGNERIA DEL SOFTWARE 2',10125),(11,'ANALISI 1',10124),(12,'ANALISI 2',10124),(13,'ANALISI 3',10124);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enroll`
--

DROP TABLE IF EXISTS `enroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enroll` (
  `IDStudent` int(11) NOT NULL,
  `IDExamDate` int(11) NOT NULL,
  `mark` varchar(15) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `IDRecord` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDStudent`,`IDExamDate`),
  KEY `IDExamDate_idx` (`IDExamDate`),
  KEY `IDRecord_idx` (`IDRecord`),
  CONSTRAINT `IDRecord` FOREIGN KEY (`IDRecord`) REFERENCES `record` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `IDStudent` FOREIGN KEY (`IDStudent`) REFERENCES `user` (`ID`) ON UPDATE CASCADE,
  CONSTRAINT `ID_ExamDate` FOREIGN KEY (`IDExamDate`) REFERENCES `examdate` (`IDExam`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enroll`
--

LOCK TABLES `enroll` WRITE;
/*!40000 ALTER TABLE `enroll` DISABLE KEYS */;
INSERT INTO `enroll` VALUES (900123,101,NULL,'NOT_INSERTED',NULL),(900123,1001,NULL,'NOT_INSERTED',NULL),(900123,1003,NULL,'NOT_INSERTED',NULL),(900123,1004,NULL,'NOT_INSERTED',NULL),(900123,1006,NULL,'NOT_INSERTED',NULL),(900123,1009,NULL,'NOT_INSERTED',NULL),(900124,101,NULL,'NOT_INSERTED',NULL),(900124,1001,NULL,'NOT_INSERTED',NULL),(900124,1003,NULL,'NOT_INSERTED',NULL),(900124,1005,NULL,'NOT_INSERTED',NULL),(900124,1006,NULL,'NOT_INSERTED',NULL),(900124,1009,NULL,'NOT_INSERTED',NULL),(900125,101,NULL,'NOT_INSERTED',NULL),(900125,1002,NULL,'NOT_INSERTED',NULL),(900125,1003,NULL,'NOT_INSERTED',NULL),(900125,1004,NULL,'NOT_INSERTED',NULL),(900125,1005,NULL,'NOT_INSERTED',NULL),(900125,1006,NULL,'NOT_INSERTED',NULL),(900125,1009,NULL,'NOT_INSERTED',NULL),(900126,101,NULL,'NOT_INSERTED',NULL),(900126,1008,NULL,'NOT_INSERTED',NULL),(900126,1010,NULL,'NOT_INSERTED',NULL),(900127,101,NULL,'NOT_INSERTED',NULL),(900127,1007,NULL,'NOT_INSERTED',NULL),(900127,1008,NULL,'NOT_INSERTED',NULL),(900128,101,NULL,'NOT_INSERTED',NULL),(900128,1007,NULL,'NOT_INSERTED',NULL),(900128,1011,NULL,'NOT_INSERTED',NULL),(900129,101,NULL,'NOT_INSERTED',NULL),(900129,1010,NULL,'NOT_INSERTED',NULL),(900129,1011,NULL,'NOT_INSERTED',NULL),(900130,101,NULL,'NOT_INSERTED',NULL),(900131,101,NULL,'NOT_INSERTED',NULL),(900131,1001,NULL,'NOT_INSERTED',NULL),(900132,101,NULL,'NOT_INSERTED',NULL),(900132,1001,NULL,'NOT_INSERTED',NULL),(900133,101,NULL,'NOT_INSERTED',NULL),(900133,1001,NULL,'NOT_INSERTED',NULL);
/*!40000 ALTER TABLE `enroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `examdate`
--

DROP TABLE IF EXISTS `examdate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `examdate` (
  `IDExam` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `IDCourse` int(11) NOT NULL,
  PRIMARY KEY (`IDExam`),
  KEY `IDCourse_idx` (`IDCourse`),
  CONSTRAINT `IDCourse` FOREIGN KEY (`IDCourse`) REFERENCES `course` (`ID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1012 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `examdate`
--

LOCK TABLES `examdate` WRITE;
/*!40000 ALTER TABLE `examdate` DISABLE KEYS */;
INSERT INTO `examdate` VALUES (101,'2021-06-10',3),(1001,'2021-02-15',3),(1002,'2021-06-25',3),(1003,'2021-06-24',12),(1004,'2021-06-30',11),(1005,'2021-07-20',11),(1006,'2021-06-24',1),(1007,'2021-07-14',1),(1008,'2021-07-25',12),(1009,'2021-07-01',2),(1010,'2021-07-29',2),(1011,'2021-07-28',13);
/*!40000 ALTER TABLE `examdate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IDExamDate` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDExamDate_idx` (`IDExamDate`)
) ENGINE=InnoDB AUTO_INCREMENT=4277 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signup`
--

DROP TABLE IF EXISTS `signup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signup` (
  `IDStudent` int(11) NOT NULL,
  `IDCourse` int(11) NOT NULL,
  PRIMARY KEY (`IDStudent`,`IDCourse`),
  KEY `ID_Course_idx` (`IDCourse`),
  CONSTRAINT `ID_Course` FOREIGN KEY (`IDCourse`) REFERENCES `course` (`ID`),
  CONSTRAINT `ID_Student` FOREIGN KEY (`IDStudent`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signup`
--

LOCK TABLES `signup` WRITE;
/*!40000 ALTER TABLE `signup` DISABLE KEYS */;
INSERT INTO `signup` VALUES (900123,1),(900124,1),(900125,1),(900125,2),(900123,3),(900124,3),(900125,3),(900126,3),(900132,3),(900133,3),(900123,11),(900124,11),(900125,11),(900123,12),(900124,12),(900125,12),(900126,12),(900127,12),(900128,13),(900129,13);
/*!40000 ALTER TABLE `signup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` varchar(45) CHARACTER SET armscii8 NOT NULL,
  `coursedeg` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10123,'Piero','Fraternali','piero.fraternali@ncuni.com','aafdc23870ecbcd3d557b6423a8982134e17927e','professor',NULL),(10124,'Filippo','Gazzola','filippo.gazzola@ncuni.com','e923d8dcfcfbb3736dbfc7d630a80d64abec8107','professor',''),(10125,'Sebastian','Linus','sebastian.linus@ncuni.com','fbed9ad24a679c28943615bc6c5e7fb6c8a104d8','professor',''),(10126,'Antonio','Conte','antonio.conte@ncuni.com','610009f594fd63c6de0eb466bc84fa9d32a06a59','professor',''),(900123,'Francesco','Paterna','francesco.paterna@ncuni.com','aafdc23870ecbcd3d557b6423a8982134e17927e','student','inf'),(900124,'Andrea','Restelli','andrea.restelli@ncuni.com','e923d8dcfcfbb3736dbfc7d630a80d64abec8107','student','inf'),(900125,'Andrea','Sanchini','andrea.sanchini@ncuni.com','fbed9ad24a679c28943615bc6c5e7fb6c8a104d8','student','inf'),(900126,'Domenico ','Putignano','domenico.putignano@ncuni.com','610009f594fd63c6de0eb466bc84fa9d32a06a59','student','inf'),(900127,'Chris','Martin','chris.martin@ncuni.com','35be8966bf8565a5c2843df98f4fa561fe272140','student','mat'),(900128,'Bill ','Gates','bill.gates@ncuni.com','3bfecb53b58863a808c66f10f91eea4c12ca327b','student','inf'),(900129,'Davide','Calabria','davide.calabria@ncuni.com','f0d9061dceed6d10afec14619c562a4a3443f2e0','student','tlc'),(900130,'Lautaro','Martinez','lautaro.martinez@ncuni.com','1b34c6c0987a49a8cea741933c59c4d4813fd571','student','fis'),(900131,'Piero','Rendina','piero.rendina@ncuni.com','c97f560ed6df360f344216b16dfa4ce58e3a6218','student','inf'),(900132,'Lentini','Pietro','pietro.lentini@ncuni.com','120d2469ce8d0b756edb093d9c11817483f346d1','student','inf'),(900133,'Giorgio','DellaPosta','giorigo.dellaposta@ncuni.com','3baa6ed9114f3662bc9567e31c448a83344920f2','student','lm'),(900134,'Dexter','DeShawn','dexter.deshawn@ncuni.com','18db98a00464a973a04252dbd3fc58d5350807a8','student','gest');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-01  2:08:40
