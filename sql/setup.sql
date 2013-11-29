# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 138.251.198.26 (MySQL 5.5.29-0ubuntu0.12.04.2)
# Database: RESOURCE_MONITORING
# Generation Time: 2013-11-29 17:39:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table DECISION
# ------------------------------------------------------------

CREATE TABLE `DECISION` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `host` varchar(255) DEFAULT NULL,
  `ready` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `host` (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table ENERGY
# ------------------------------------------------------------

CREATE TABLE `ENERGY` (
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `machine_id` varchar(255) DEFAULT NULL,
  `socket_num` int(11) DEFAULT NULL,
  `activePower` int(11) DEFAULT NULL,
  `voltage` int(11) DEFAULT NULL,
  `current` double DEFAULT NULL,
  `wattHours` int(11) DEFAULT NULL,
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table EXPERIMENTS
# ------------------------------------------------------------

CREATE TABLE `EXPERIMENTS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` varchar(255) DEFAULT NULL,
  `start_t` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_t` timestamp NULL DEFAULT NULL,
  `benchmarkURL` varchar(255) DEFAULT NULL,
  `output` varchar(255) DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `command` (`command`),
  KEY `host` (`host`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table FILE_SYSTEM
# ------------------------------------------------------------

CREATE TABLE `FILE_SYSTEM` (
  `machine_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fs_name` varchar(255) DEFAULT NULL,
  `fs_location` varchar(255) DEFAULT NULL,
  `fs_type` varchar(100) DEFAULT NULL,
  `fs_size` bigint(20) DEFAULT NULL,
  `fs_free` bigint(20) DEFAULT NULL,
  `fs_used` bigint(20) DEFAULT NULL,
  `fs_files` bigint(20) DEFAULT NULL,
  `fs_disk_reads` bigint(20) DEFAULT NULL,
  `fs_disk_read_bytes` bigint(20) DEFAULT NULL,
  `fs_disk_writes` bigint(20) DEFAULT NULL,
  `fs_disk_write_bytes` bigint(20) DEFAULT NULL,
  KEY `machine_id` (`machine_id`),
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table FILTER_DECISIONS
# ------------------------------------------------------------

CREATE TABLE `FILTER_DECISIONS` (
  `uuid` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `pass` int(1) DEFAULT NULL,
  UNIQUE KEY `uuid_2` (`uuid`,`host`),
  KEY `uuid` (`uuid`),
  KEY `host` (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table FILTER_EVALS
# ------------------------------------------------------------

CREATE TABLE `FILTER_EVALS` (
  `uuid` varchar(255) DEFAULT NULL,
  `complete` int(1) DEFAULT NULL,
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table MACHINE_PDU_MAP
# ------------------------------------------------------------

CREATE TABLE `MACHINE_PDU_MAP` (
  `socket_num` int(11) DEFAULT NULL,
  `machine_id` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table MACHINE_UTIL
# ------------------------------------------------------------

CREATE TABLE `MACHINE_UTIL` (
  `machine_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cpu_user` double DEFAULT NULL,
  `cpu_sys` double DEFAULT NULL,
  `cpu_idle` double DEFAULT NULL,
  `cpu_nice` double DEFAULT NULL,
  `cpu_wait` double DEFAULT NULL,
  `memory_used` bigint(20) DEFAULT NULL,
  `memory_free` bigint(20) DEFAULT NULL,
  `swap_used` bigint(20) DEFAULT NULL,
  `swap_free` bigint(20) DEFAULT NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table MEMORY_UTIL
# ------------------------------------------------------------

CREATE TABLE `MEMORY_UTIL` (
  `machine_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `memory_used` bigint(20) DEFAULT NULL,
  KEY `machine_id` (`machine_id`),
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table MixExperiments
# ------------------------------------------------------------

CREATE TABLE `MixExperiments` (
  `exp_id` varchar(255) NOT NULL DEFAULT '',
  `workload_mix_id` varchar(255) DEFAULT NULL,
  `host_ip` varchar(255) DEFAULT NULL,
  `vm_ip` varchar(255) DEFAULT NULL,
  `vm_cores` int(11) DEFAULT NULL,
  `vm_mem` int(11) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `host_energy` int(11) DEFAULT NULL,
  `start_t` timestamp NULL DEFAULT NULL,
  `end_t` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`exp_id`),
  KEY `workload_mix_id` (`workload_mix_id`),
  KEY `start_t` (`start_t`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table NETWORK
# ------------------------------------------------------------

CREATE TABLE `NETWORK` (
  `machine_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `device_name` varchar(100) DEFAULT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  `rx_bytes` bigint(20) DEFAULT NULL,
  `rx_packets` bigint(20) DEFAULT NULL,
  `rx_errors` bigint(20) DEFAULT NULL,
  `rx_dropped` bigint(20) DEFAULT NULL,
  `rx_overruns` bigint(20) DEFAULT NULL,
  `tx_bytes` bigint(20) DEFAULT NULL,
  `tx_packets` bigint(20) DEFAULT NULL,
  `tx_errors` bigint(20) DEFAULT NULL,
  `tx_dropped` bigint(20) DEFAULT NULL,
  `tx_overruns` bigint(20) DEFAULT NULL,
  KEY `machine_id` (`machine_id`),
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table POWER_PREDICTS
# ------------------------------------------------------------

CREATE TABLE `POWER_PREDICTS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` timestamp NULL DEFAULT NULL,
  `predicted` double DEFAULT NULL,
  `actual` double DEFAULT NULL,
  `error` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table PROCESS
# ------------------------------------------------------------

CREATE TABLE `PROCESS` (
  `machine_id` varchar(40) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `process_name` varchar(255) DEFAULT NULL,
  `process_start_time` bigint(20) DEFAULT NULL,
  `process_cpu_percent` double DEFAULT NULL,
  `process_mem` bigint(20) DEFAULT NULL,
  `process_resident` bigint(20) DEFAULT NULL,
  KEY `machine_id` (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table REQs_FILTER
# ------------------------------------------------------------

CREATE TABLE `REQs_FILTER` (
  `time` timestamp NULL DEFAULT NULL,
  `req_id` varchar(255) NOT NULL DEFAULT '',
  `host` varchar(255) NOT NULL DEFAULT '',
  `vm_type` varchar(255) DEFAULT NULL,
  `passed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`req_id`,`host`),
  KEY `req_id_2` (`req_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table REQs_TERMINATE
# ------------------------------------------------------------

CREATE TABLE `REQs_TERMINATE` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table REQs_WEIGH
# ------------------------------------------------------------

CREATE TABLE `REQs_WEIGH` (
  `req_id` varchar(255) NOT NULL DEFAULT '',
  `host` varchar(255) NOT NULL DEFAULT '',
  `time` timestamp NULL DEFAULT NULL,
  `vm_type` varchar(255) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  PRIMARY KEY (`req_id`,`host`),
  KEY `req_id` (`req_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table SCH_EXP
# ------------------------------------------------------------

CREATE TABLE `SCH_EXP` (
  `uuid` varchar(255) DEFAULT NULL,
  `start_t` timestamp NULL DEFAULT NULL,
  `end_t` timestamp NULL DEFAULT NULL,
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table SCH_PLACEMENTS
# ------------------------------------------------------------

CREATE TABLE `SCH_PLACEMENTS` (
  `host` varchar(255) NOT NULL,
  `vm_type` varchar(255) DEFAULT NULL,
  `cost` float DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `req_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`req_id`,`host`),
  KEY `req_id` (`req_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table SYS_INFO
# ------------------------------------------------------------

CREATE TABLE `SYS_INFO` (
  `machine_id` varchar(40) NOT NULL DEFAULT '',
  `hostname` varchar(255) DEFAULT NULL,
  `primary_ip` varchar(15) DEFAULT NULL,
  `cpu_vendor` varchar(100) DEFAULT NULL,
  `cpu_model` varchar(100) DEFAULT NULL,
  `num_cores` tinyint(2) DEFAULT NULL,
  `num_cpus` tinyint(2) DEFAULT NULL,
  `cpu_mhz` int(11) DEFAULT NULL,
  `cpu_cache_size` bigint(20) DEFAULT NULL,
  `os_name` varchar(255) DEFAULT NULL,
  `os_version` varchar(100) DEFAULT NULL,
  `default_gateway` varchar(15) DEFAULT NULL,
  `memory_total` bigint(20) DEFAULT NULL,
  `swap_total` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`machine_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table TASK_METADATA
# ------------------------------------------------------------

CREATE TABLE `TASK_METADATA` (
  `uuid` varbinary(16) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  UNIQUE KEY `unique` (`uuid`,`name`,`value`),
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table TASKS
# ------------------------------------------------------------

CREATE TABLE `TASKS` (
  `uuid` varbinary(16) NOT NULL,
  `start_t` timestamp NULL DEFAULT NULL,
  `end_t` timestamp NULL DEFAULT NULL,
  `task_name` varchar(45) DEFAULT NULL,
  `machine_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `start_t` (`start_t`),
  KEY `end_t` (`end_t`),
  KEY `machine_id` (`machine_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table VM_LAUNCHED
# ------------------------------------------------------------

CREATE TABLE `VM_LAUNCHED` (
  `uuid` varchar(255) DEFAULT NULL,
  `start_t` timestamp NULL DEFAULT NULL,
  `end_t` timestamp NULL DEFAULT NULL,
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table WEIGH_DECISIONS
# ------------------------------------------------------------

CREATE TABLE `WEIGH_DECISIONS` (
  `uuid` varchar(255) NOT NULL DEFAULT '',
  `host` varchar(255) DEFAULT NULL,
  `score` float DEFAULT NULL,
  UNIQUE KEY `uuid` (`uuid`,`host`),
  KEY `uuid_2` (`uuid`),
  KEY `host` (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table WEIGH_EVALS
# ------------------------------------------------------------

CREATE TABLE `WEIGH_EVALS` (
  `uuid` varchar(255) DEFAULT NULL,
  `complete` int(1) DEFAULT NULL,
  KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
