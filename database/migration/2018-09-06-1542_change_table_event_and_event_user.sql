/**
date: 2018-09-06 15:00
author: ThanhLV, anhpth
purpose: change column event and event_user
purpose: create table check latetime and table month
**/


/*change column event*/
ALTER TABLE kpi_event ADD updated_date DATETIME NULL;
ALTER TABLE kpi_event ADD address TEXT NULL;
ALTER TABLE kpi_event ADD event_additional_config TEXT NULL COMMENT 'event_additional_config is config for event';
ALTER TABLE kpi_event CHANGE name event_name VARCHAR(350);
ALTER TABLE kpi_event MODIFY status TINYINT(4) COMMENT 'status maybe is 1== wait for confirmation 2== cancel..vv.. ,';

/*change column kpi_event_user*/
ALTER TABLE kpi_event_user CHANGE is_host type TINYINT(4) COMMENT 'type can 1 is host , 2 is member , 3 is listener';

-- Create table to checking latetime
CREATE TABLE `kpi_latetime_check` (
  `id` int(10) PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `late_times` varchar(20) NOT NULL,
  `month_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Add foreign key for table latetime_check
ALTER TABLE `kpi_latetime_check`
  ADD KEY `fk_foreign_key_name` (`user_name`),
  ADD KEY `fk_month` (`month_id`);

-- Add constraint for table latetime_check
ALTER TABLE `kpi_latetime_check`
  ADD CONSTRAINT `fk_foreign_key_name` FOREIGN KEY (`user_name`) REFERENCES `kpi_user` (`user_name`),
  ADD CONSTRAINT `fk_month` FOREIGN KEY (`month_id`) REFERENCES `kpi_month` (`id`);

-- create table month
CREATE TABLE `kpi_month` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `month` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


