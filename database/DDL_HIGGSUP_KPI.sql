CREATE DATABASE higgsup_kpi;
USE higgsup_kpi;
CREATE TABLE kpi_user (
  user_name    VARCHAR(50) NOT NULL PRIMARY KEY,
  created_date DATETIME    NOT NULL,
  active       TINYINT(1)   DEFAULT NULL,
  first_name   VARCHAR(50)  DEFAULT NULL,
  last_name    VARCHAR(50)  DEFAULT NULL,
  email        VARCHAR(50)  DEFAULT NULL,
  avatar_url   VARCHAR(200) DEFAULT NULL
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
  name              VARCHAR(100) DEFAULT NULL,
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