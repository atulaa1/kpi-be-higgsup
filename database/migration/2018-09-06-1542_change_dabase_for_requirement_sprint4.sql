/**
date: 2018-09-06 15:00
author: ThanhLV, anhpth
purpose: add new column updated_date for kpi_event table
purpose: add new column address for kpi_event table
purpose: add new event_additional_config for kpi_event table
purpose: change column name to event_name for kpi_event table
purpose: MODIFY column status from varchar to TINYINT for kpi_event table
purpose: change year_work to name date_start_work

purpose: create table check latetime and table month
**/


ALTER TABLE kpi_event ADD updated_date DATETIME NULL;
ALTER TABLE kpi_event ADD address TEXT NULL;
ALTER TABLE kpi_event ADD event_additional_config TEXT NULL COMMENT 'event_additional_config is config for event';
ALTER TABLE kpi_event CHANGE name event_name VARCHAR(350);
ALTER TABLE kpi_event MODIFY status TINYINT(4) COMMENT 'status maybe is 1== wait for confirmation 2== cancel..vv.. ,';

/*change column kpi_event_user*/
ALTER TABLE kpi_event_user CHANGE is_host type TINYINT(4) COMMENT 'type can 1 is host , 2 is member , 3 is listener';

-- create table month
CREATE TABLE `kpi_month` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `month` DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Create table to checking latetime
CREATE TABLE `kpi_latetime_check` (
  `id` int(10) PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `late_times` varchar(20) NOT NULL,
  `month_id` int(11) NOT NULL,
  FOREIGN KEY (user_name) REFERENCES kpi_user(user_name),
  FOREIGN KEY (month_id) REFERENCES kpi_month(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- change year_work to name date_start_work
ALTER TABLE kpi_user CHANGE year_work date_start_work DATE;