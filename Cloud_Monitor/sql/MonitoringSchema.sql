-- MySQL dump 10.11
--
-- Host: localhost    Database: RESOURCE_MONITORING
-- ------------------------------------------------------
-- Server version	5.0.77

CREATE DATABASE IF NOT EXISTS RESOURCE_MONITORING;

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
-- Table structure for table `ENERGY`
--

DROP TABLE IF EXISTS `ENERGY`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ENERGY` (
  `time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `machine_id` varchar(255) default NULL,
  `socket_num` int(11) default NULL,
  `activePower` int(11) default NULL,
  `voltage` int(11) default NULL,
  `current` double default NULL,
  `wattHours` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `EXPERIMENTS`
--

DROP TABLE IF EXISTS `EXPERIMENTS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `EXPERIMENTS` (
  `id` int(11) NOT NULL auto_increment,
  `command` varchar(255) default NULL,
  `start_t` timestamp NOT NULL default '0000-00-00 00:00:00',
  `end_t` timestamp NOT NULL default '0000-00-00 00:00:00',
  `benchmarkURL` varchar(255) default NULL,
  `output` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `FILE_SYSTEM`
--

DROP TABLE IF EXISTS `FILE_SYSTEM`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `FILE_SYSTEM` (
  `machine_id` varchar(40) default NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `fs_name` varchar(255) default NULL,
  `fs_location` varchar(255) default NULL,
  `fs_type` varchar(100) default NULL,
  `fs_size` bigint(20) default NULL,
  `fs_free` bigint(20) default NULL,
  `fs_used` bigint(20) default NULL,
  `fs_files` bigint(20) default NULL,
  `fs_disk_reads` bigint(20) default NULL,
  `fs_disk_read_bytes` bigint(20) default NULL,
  `fs_disk_writes` bigint(20) default NULL,
  `fs_disk_write_bytes` bigint(20) default NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MACHINE_PDU_MAP`
--

DROP TABLE IF EXISTS `MACHINE_PDU_MAP`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MACHINE_PDU_MAP` (
  `socket_num` int(11) default NULL,
  `machine_id` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MACHINE_UTIL`
--

DROP TABLE IF EXISTS `MACHINE_UTIL`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MACHINE_UTIL` (
  `machine_id` varchar(40) default NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `cpu_user` double default NULL,
  `cpu_sys` double default NULL,
  `cpu_idle` double default NULL,
  `cpu_nice` double default NULL,
  `cpu_wait` double default NULL,
  `memory_used` bigint(20) default NULL,
  `memory_free` bigint(20) default NULL,
  `swap_used` bigint(20) default NULL,
  `swap_free` bigint(20) default NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `NETWORK`
--

DROP TABLE IF EXISTS `NETWORK`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `NETWORK` (
  `machine_id` varchar(40) default NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `device_name` varchar(100) default NULL,
  `ip_address` varchar(15) default NULL,
  `rx_bytes` bigint(20) default NULL,
  `rx_packets` bigint(20) default NULL,
  `rx_errors` bigint(20) default NULL,
  `rx_dropped` bigint(20) default NULL,
  `rx_overruns` bigint(20) default NULL,
  `tx_bytes` bigint(20) default NULL,
  `tx_packets` bigint(20) default NULL,
  `tx_errors` bigint(20) default NULL,
  `tx_dropped` bigint(20) default NULL,
  `tx_overruns` bigint(20) default NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `POWER_PREDICTS`
--

DROP TABLE IF EXISTS `POWER_PREDICTS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `POWER_PREDICTS` (
  `id` int(11) NOT NULL auto_increment,
  `time` timestamp NULL default NULL,
  `predicted` double default NULL,
  `actual` double default NULL,
  `error` double default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=606 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `PROCESS`
--

DROP TABLE IF EXISTS `PROCESS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PROCESS` (
  `machine_id` varchar(40) default NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `process_name` varchar(255) default NULL,
  `process_start_time` bigint(20) default NULL,
  `process_cpu_percent` double default NULL,
  `process_mem` bigint(20) default NULL,
  `process_resident` bigint(20) default NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `SYS_INFO`
--

DROP TABLE IF EXISTS `SYS_INFO`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `SYS_INFO` (
  `machine_id` varchar(40) NOT NULL default '',
  `hostname` varchar(255) default NULL,
  `primary_ip` varchar(15) default NULL,
  `cpu_vendor` varchar(100) default NULL,
  `cpu_model` varchar(100) default NULL,
  `num_cores` tinyint(2) default NULL,
  `num_cpus` tinyint(2) default NULL,
  `cpu_mhz` int(11) default NULL,
  `cpu_cache_size` bigint(20) default NULL,
  `os_name` varchar(255) default NULL,
  `os_version` varchar(100) default NULL,
  `default_gateway` varchar(15) default NULL,
  `memory_total` bigint(20) default NULL,
  `swap_total` bigint(20) default NULL,
  PRIMARY KEY  (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-09-27  8:57:24