/**
date: 2018-09-06 15:00
author: ThanhLV
purpose: change column event and event_user
**/

CREATE DATABASE higgsup_kpi;
USE higgsup_kpi;
CREATE TABLE kpi_user (
  user_name    VARCHAR(50)  NOT NULL PRIMARY KEY,
  created_date DATETIME     NOT NULL,
  active       TINYINT(1)   DEFAULT NULL,
  first_name   VARCHAR(50)  DEFAULT NULL,
  last_name    VARCHAR(50)  DEFAULT NULL,
  full_name    VARCHAR(400) DEFAULT NULL,
  email        VARCHAR(50)  DEFAULT NULL,
  birthday     DATE         NULL,
  number_phone VARCHAR(20)  NULL,
  address      NVARCHAR(250)NULL,
  gmail        VARCHAR(200) NULL,
  skype        VARCHAR(200) NULL,
  date_start_work    DATE          NULL,
  avatar_url   MEDIUMTEXT
)
ENGINE = InnoDB
DEFAULT CHARSET = utf8;

CREATE TABLE kpi_group_type (
  id   INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) DEFAULT NULL
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of kpi_group_type
-- ----------------------------
INSERT INTO `kpi_group_type` VALUES (1, 'Seminar');
INSERT INTO `kpi_group_type` VALUES (2, 'Câu lạc bộ');
INSERT INTO `kpi_group_type` VALUES (3, 'Team building');
INSERT INTO `kpi_group_type` VALUES (4, 'Support chung');

CREATE TABLE kpi_group (
  id                INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name              VARCHAR(350) DEFAULT NULL,
  description       VARCHAR(255) DEFAULT NULL,
  group_type_id     INT(11)      DEFAULT NULL,
  created_date      DATETIME     DEFAULT NULL,
  additional_config JSON         DEFAULT NULL COMMENT 'save config  group is one json object',
  FOREIGN KEY (group_type_id) REFERENCES kpi_group_type (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_event (
  id           INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_name   VARCHAR(350)                 DEFAULT NULL,
  description  VARCHAR(255)                 DEFAULT NULL,
  created_date DATETIME                     DEFAULT NULL,
  updated_date DATETIME                     DEFAULT NULL,
  group_id     INT(11)                      DEFAULT NULL,
  status       TINYINT(4)                   DEFAULT NULL COMMENT 'status maybe is 1== wait for confirmation 2== cancel..vv.. ,',
  begin_date   DATETIME                     DEFAULT NULL COMMENT 'date start event',
  end_date     DATETIME                     DEFAULT NULL COMMENT 'date end event',
  event_additional_config    TEXT           DEFAULT NULL COMMENT 'event_additional_config is config for event',
  address      TEXT                         DEFAULT NULL,
  FOREIGN KEY (group_id) REFERENCES kpi_group (id)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE kpi_event_user (
  user_name VARCHAR(255) NOT NULL,
  event_id  INT(11)      NOT NULL,
  type   TINYINT(4) DEFAULT NULL COMMENT 'type can 1 is host , 2 is member , 3 is listener',
  FOREIGN KEY (user_name) REFERENCES kpi_user (user_name),
  FOREIGN KEY (event_id) REFERENCES kpi_event (id),
  CONSTRAINT PK_kpi_event_user PRIMARY KEY (user_name, event_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_survey_question_man(
  id int(11) NOT NULL AUTO_INCREMENT,
  question varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  number int(11) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE kpi_project (
  id int(11) NOT NULL AUTO_INCREMENT,
  active tinyint(4) NULL DEFAULT 1,
  created_date datetime NULL DEFAULT NULL,
  updated_date datetime NULL DEFAULT NULL,
  name varchar(255),
  PRIMARY KEY (id)
);

-- create table kpi_year_month
CREATE TABLE `kpi_year_month` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `year_and_month` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create table to checking latetime
CREATE TABLE `kpi_latetime_check` (
  `id` int(10) PRIMARY KEY AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `late_times` int(11),
  `year_month_id` int(11) NOT NULL,
  FOREIGN KEY (user_name) REFERENCES kpi_user(user_name),
  FOREIGN KEY (year_month_id) REFERENCES kpi_year_month(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;