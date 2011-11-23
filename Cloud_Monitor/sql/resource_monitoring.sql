SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `resource_monitoring` DEFAULT CHARACTER SET latin1 ;
USE `resource_monitoring` ;

-- -----------------------------------------------------
-- Table `resource_monitoring`.`energy`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`energy` (
  `time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `machine_id` VARCHAR(255) NULL DEFAULT NULL ,
  `socket_num` INT(11) NULL DEFAULT NULL ,
  `activePower` INT(11) NULL DEFAULT NULL ,
  `voltage` INT(11) NULL DEFAULT NULL ,
  `current` DOUBLE NULL DEFAULT NULL ,
  `wattHours` INT(11) NULL DEFAULT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`experiments`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`experiments` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `command` VARCHAR(255) NULL DEFAULT NULL ,
  `start_t` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `end_t` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `benchmarkURL` VARCHAR(255) NULL DEFAULT NULL ,
  `output` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
AUTO_INCREMENT = 53
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`file_system`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`file_system` (
  `machine_id` VARCHAR(40) NULL DEFAULT NULL ,
  `time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `fs_name` VARCHAR(255) NULL DEFAULT NULL ,
  `fs_location` VARCHAR(255) NULL DEFAULT NULL ,
  `fs_type` VARCHAR(100) NULL DEFAULT NULL ,
  `fs_size` BIGINT(20) NULL DEFAULT NULL ,
  `fs_free` BIGINT(20) NULL DEFAULT NULL ,
  `fs_used` BIGINT(20) NULL DEFAULT NULL ,
  `fs_files` BIGINT(20) NULL DEFAULT NULL ,
  `fs_disk_reads` BIGINT(20) NULL DEFAULT NULL ,
  `fs_disk_read_bytes` BIGINT(20) NULL DEFAULT NULL ,
  `fs_disk_writes` BIGINT(20) NULL DEFAULT NULL ,
  `fs_disk_write_bytes` BIGINT(20) NULL DEFAULT NULL ,
  INDEX `machine_id` (`machine_id` ASC) ,
  INDEX `time` (`time` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`machine_pdu_map`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`machine_pdu_map` (
  `socket_num` INT(11) NULL DEFAULT NULL ,
  `machine_id` VARCHAR(255) NULL DEFAULT NULL )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`machine_util`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`machine_util` (
  `machine_id` VARCHAR(40) NULL DEFAULT NULL ,
  `time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `cpu_user` DOUBLE NULL DEFAULT NULL ,
  `cpu_sys` DOUBLE NULL DEFAULT NULL ,
  `cpu_idle` DOUBLE NULL DEFAULT NULL ,
  `cpu_nice` DOUBLE NULL DEFAULT NULL ,
  `cpu_wait` DOUBLE NULL DEFAULT NULL ,
  `memory_used` BIGINT(20) NULL DEFAULT NULL ,
  `memory_free` BIGINT(20) NULL DEFAULT NULL ,
  `swap_used` BIGINT(20) NULL DEFAULT NULL ,
  `swap_free` BIGINT(20) NULL DEFAULT NULL ,
  INDEX `temp` (`time` ASC) ,
  INDEX `machine_id` (`machine_id` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`network`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`network` (
  `machine_id` VARCHAR(40) NULL DEFAULT NULL ,
  `time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `device_name` VARCHAR(100) NULL DEFAULT NULL ,
  `ip_address` VARCHAR(15) NULL DEFAULT NULL ,
  `rx_bytes` BIGINT(20) NULL DEFAULT NULL ,
  `rx_packets` BIGINT(20) NULL DEFAULT NULL ,
  `rx_errors` BIGINT(20) NULL DEFAULT NULL ,
  `rx_dropped` BIGINT(20) NULL DEFAULT NULL ,
  `rx_overruns` BIGINT(20) NULL DEFAULT NULL ,
  `tx_bytes` BIGINT(20) NULL DEFAULT NULL ,
  `tx_packets` BIGINT(20) NULL DEFAULT NULL ,
  `tx_errors` BIGINT(20) NULL DEFAULT NULL ,
  `tx_dropped` BIGINT(20) NULL DEFAULT NULL ,
  `tx_overruns` BIGINT(20) NULL DEFAULT NULL ,
  INDEX `machine_id` (`machine_id` ASC) ,
  INDEX `time` (`time` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`power_predicts`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`power_predicts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `time` TIMESTAMP NULL DEFAULT NULL ,
  `predicted` DOUBLE NULL DEFAULT NULL ,
  `actual` DOUBLE NULL DEFAULT NULL ,
  `error` DOUBLE NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = MyISAM
AUTO_INCREMENT = 606
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`process`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`process` (
  `machine_id` VARCHAR(40) NULL DEFAULT NULL ,
  `time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `process_name` VARCHAR(255) NULL DEFAULT NULL ,
  `process_start_time` BIGINT(20) NULL DEFAULT NULL ,
  `process_cpu_percent` DOUBLE NULL DEFAULT NULL ,
  `process_mem` BIGINT(20) NULL DEFAULT NULL ,
  `process_resident` BIGINT(20) NULL DEFAULT NULL ,
  INDEX `machine_id` (`machine_id` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`sys_info`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`sys_info` (
  `machine_id` VARCHAR(40) NOT NULL DEFAULT '' ,
  `hostname` VARCHAR(255) NULL DEFAULT NULL ,
  `primary_ip` VARCHAR(15) NULL DEFAULT NULL ,
  `cpu_vendor` VARCHAR(100) NULL DEFAULT NULL ,
  `cpu_model` VARCHAR(100) NULL DEFAULT NULL ,
  `num_cores` TINYINT(2) NULL DEFAULT NULL ,
  `num_cpus` TINYINT(2) NULL DEFAULT NULL ,
  `cpu_mhz` INT(11) NULL DEFAULT NULL ,
  `cpu_cache_size` BIGINT(20) NULL DEFAULT NULL ,
  `os_name` VARCHAR(255) NULL DEFAULT NULL ,
  `os_version` VARCHAR(100) NULL DEFAULT NULL ,
  `default_gateway` VARCHAR(15) NULL DEFAULT NULL ,
  `memory_total` BIGINT(20) NULL DEFAULT NULL ,
  `swap_total` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`machine_id`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`task_metadata`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`task_metadata` (
  `uuid` VARBINARY(16) NOT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  `value` VARCHAR(45) NULL DEFAULT NULL ,
  UNIQUE INDEX `unique` (`uuid` ASC, `name` ASC, `value` ASC) ,
  INDEX `uuid` (`uuid` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `resource_monitoring`.`tasks`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `resource_monitoring`.`tasks` (
  `uuid` VARBINARY(16) NOT NULL ,
  `start_t` TIMESTAMP NULL DEFAULT NULL ,
  `end_t` TIMESTAMP NULL DEFAULT NULL ,
  `task_name` VARCHAR(45) NULL DEFAULT NULL ,
  `machine_id` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`uuid`) ,
  INDEX `start_t` (`start_t` ASC) ,
  INDEX `end_t` (`end_t` ASC) ,
  INDEX `machine_id` (`machine_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
