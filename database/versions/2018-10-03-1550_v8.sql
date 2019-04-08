/**
date: 2018-10-03 15:50
author: anhpth
purpose: purpose: add table for sprint 6
**/

CREATE DATABASE higgsup_kpi;
USE higgsup_kpi;
CREATE TABLE kpi_user (
  user_name       VARCHAR(50)   NOT NULL PRIMARY KEY,
  created_date    DATETIME      NOT NULL,
  active          TINYINT(1)   DEFAULT NULL,
  first_name      VARCHAR(50)  DEFAULT NULL,
  last_name       VARCHAR(50)  DEFAULT NULL,
  full_name       VARCHAR(400) DEFAULT NULL,
  email           VARCHAR(50)  DEFAULT NULL,
  birthday        DATE          NULL,
  number_phone    VARCHAR(20)   NULL,
  address         NVARCHAR(250) NULL,
  gmail           VARCHAR(200)  NULL,
  skype           VARCHAR(200)  NULL,
  date_start_work DATE          NULL,
  avatar_url      MEDIUMTEXT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_group_type (
  id   INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50)                  DEFAULT NULL
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
  name              VARCHAR(350)                 DEFAULT NULL,
  description       VARCHAR(255)                 DEFAULT NULL,
  group_type_id     INT(11)                      DEFAULT NULL,
  created_date      DATETIME                     DEFAULT NULL,
  additional_config JSON                         DEFAULT NULL
  COMMENT 'save config  group is one json object',
  FOREIGN KEY (group_type_id) REFERENCES kpi_group_type (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_event (
  id                      INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  event_name              VARCHAR(350)                 DEFAULT NULL,
  description             VARCHAR(255)                 DEFAULT NULL,
  created_date            DATETIME                     DEFAULT NULL,
  updated_date            DATETIME                     DEFAULT NULL,
  group_id                INT(11)                      DEFAULT NULL,
  status                  TINYINT(4)                   DEFAULT NULL
  COMMENT 'status maybe is 1== wait for confirmation 2== cancel..vv.. ,',
  begin_date              DATETIME                     DEFAULT NULL
  COMMENT 'date start event',
  end_date                DATETIME                     DEFAULT NULL
  COMMENT 'date end event',
  event_additional_config TEXT                         DEFAULT NULL
  COMMENT 'event_additional_config is config for event',
  address                 TEXT                         DEFAULT NULL,
  creator                 VARCHAR(50)                  DEFAULT NULL,
  FOREIGN KEY (creator) REFERENCES kpi_user (user_name),
  FOREIGN KEY (group_id) REFERENCES kpi_group (id)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE kpi_event_user (
  user_name VARCHAR(255)         NOT NULL,
  event_id  INT(11)              NOT NULL,
  status    TINYINT(4) DEFAULT 0 NULL
  COMMENT 'status can 0 is unfinished , 1 is finish',
  type      TINYINT(4) DEFAULT NULL
  COMMENT 'type can 1 is host , 2 is member , 3 is listener',
  FOREIGN KEY (user_name) REFERENCES kpi_user (user_name),
  FOREIGN KEY (event_id) REFERENCES kpi_event (id),
  CONSTRAINT PK_kpi_event_user PRIMARY KEY (user_name, event_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `kpi_seminar_survey` (
  `id`                  INT(10) PRIMARY KEY AUTO_INCREMENT,
  `evaluating_username` VARCHAR(50)
                        CHARACTER SET utf8 NOT NULL,
  `event_id`            INT(11),
  `evaluated_username`  VARCHAR(50)
                        CHARACTER SET utf8 NOT NULL,
  `rating`              INT(11),

  FOREIGN KEY (evaluating_username) REFERENCES kpi_user (user_name),
  FOREIGN KEY (evaluated_username) REFERENCES kpi_user (user_name),
  FOREIGN KEY (event_id) REFERENCES kpi_event (id)


)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_survey_question_man (
  id       INT(11)                 NOT NULL AUTO_INCREMENT,
  question VARCHAR(2000)
           CHARACTER SET utf8
           COLLATE utf8_general_ci NULL     DEFAULT NULL,
  number   INT(11)                 NULL     DEFAULT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_project (
  id           INT(11)    NOT NULL AUTO_INCREMENT,
  active       TINYINT(4) NULL     DEFAULT 1,
  created_date DATETIME   NULL     DEFAULT NULL,
  updated_date DATETIME   NULL     DEFAULT NULL,
  name         VARCHAR(255),
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- create table kpi_year_month
CREATE TABLE `kpi_year_month` (
  `id`             INT PRIMARY KEY AUTO_INCREMENT,
  `year_and_month` INT(11) NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Create table to checking latetime
CREATE TABLE `kpi_latetime_check` (
  `id`            INT(10) PRIMARY KEY AUTO_INCREMENT,
  `user_name`     VARCHAR(50)
                  CHARACTER SET utf8 NOT NULL,
  `late_times`    INT(11),
  `year_month_id` INT(11)            NOT NULL,
  FOREIGN KEY (user_name) REFERENCES kpi_user (user_name),
  FOREIGN KEY (year_month_id) REFERENCES kpi_year_month (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_support
(
  id           INT PRIMARY KEY AUTO_INCREMENT,
  task_name    VARCHAR(500),
  task_point   FLOAT,
  created_date DATETIME        DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `kpi_project_user` (
  `id`             INT(10) PRIMARY KEY AUTO_INCREMENT,
  `rated_username` VARCHAR(50) NOT NULL,
  `project_id`     INT(11)             DEFAULT NULL,
  `joined_date`    INT(11)             DEFAULT NULL,
  UNIQUE KEY `unique_project_user` (`rated_username`, `project_id`) USING BTREE,
  KEY `fk_project_id` (`project_id`),
  CONSTRAINT `fk_project_id` FOREIGN KEY (`project_id`) REFERENCES `kpi_project` (`id`),
  KEY `fk_user` (`rated_username`),
  CONSTRAINT `fk_user` FOREIGN KEY (`rated_username`) REFERENCES `kpi_user` (`user_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- infomation of project in month
CREATE TABLE `kpi_project_log` (
  `id`             INT(11) NOT NULL,
  `rated_username` VARCHAR(50) DEFAULT NULL,
  `project_id`     INT(11)     DEFAULT NULL,
  `year_month`     INT(11) NOT NULL,
  `project_point`  FLOAT       DEFAULT NULL,
  `man_username`   VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique1` (`rated_username`, `project_id`, `year_month`) USING BTREE,
  KEY `fk_project` (`project_id`),
  CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `kpi_project` (`id`),
  CONSTRAINT `fk_username` FOREIGN KEY (`rated_username`) REFERENCES `kpi_project_user` (`rated_username`),
  CONSTRAINT `fk_man` FOREIGN KEY (`man_username`) REFERENCES `kpi_user` (`user_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE `higgsup_kpi`.`kpi_point` (
  `id`                    INT(10) PRIMARY KEY AUTO_INCREMENT,
  `rated_username`        VARCHAR(50)
                          CHARACTER SET utf8 NOT NULL,
  `rule_point`            FLOAT              NOT NULL,
  `club_point`            FLOAT              NULL,
  `normal_seminar_point`  FLOAT              NULL,
  `weekend_seminar_point` FLOAT              NULL,
  `support_point`         FLOAT              NULL,
  `teambuilding_point`    FLOAT              NULL,
  `personal_point`        FLOAT              NOT NULL,
  `project_point`         FLOAT              NOT NULL,
  `total_point`           FLOAT              NOT NULL,
  `year_month`            INT(11)            NOT NULL,
  FOREIGN KEY (rated_username) REFERENCES kpi_user (user_name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

