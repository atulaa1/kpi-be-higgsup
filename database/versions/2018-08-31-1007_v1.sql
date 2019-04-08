/**
date: 2018-08-31
author: Thieu Thanh Tu
purpose: Version 01
**/

CREATE DATABASE higgsup_kpi;
USE higgsup_kpi;
CREATE TABLE kpi_user (
  user_name    VARCHAR(50)  NOT NULL PRIMARY KEY,
  created_date DATETIME     NOT NULL,
  active       TINYINT(1)   DEFAULT NULL,
  first_name   VARCHAR(50)  DEFAULT NULL,
  last_name    VARCHAR(50)  DEFAULT NULL,
  email        VARCHAR(50)  DEFAULT NULL,
  birthday     DATE         NULL,
  number_phone VARCHAR(20)  NULL,
  address      NVARCHAR(250)NULL,
  gmail        VARCHAR(200) NULL,
  skype        VARCHAR(200) NULL,
  year_work    INT          NULL,
  avatar_url   MEDIUMTEXT;
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE kpi_group_type (
  id   INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


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
  name         VARCHAR(50)                  DEFAULT NULL,
  description  VARCHAR(255)                 DEFAULT NULL,
  created_date DATETIME                     DEFAULT NULL,
  group_id     INT(11)                      DEFAULT NULL,
  status       VARCHAR(50)                  DEFAULT NULL COMMENT 'status maybe is finish..vv..',
  begin_date   DATETIME                     DEFAULT NULL COMMENT 'date start event',
  end_date     DATETIME                     DEFAULT NULL COMMENT 'date end event',
  FOREIGN KEY (group_id) REFERENCES kpi_group (id)

)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE kpi_event_user (
  user_name VARCHAR(255) NOT NULL,
  event_id  INT(11)      NOT NULL,
  is_host   TINYINT(4) DEFAULT NULL COMMENT 'if not null then is host, null is member',
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
  created_date datetime(0) NULL DEFAULT NULL,
  name varchar(255),
  PRIMARY KEY (id)
);