/**
date: 2018-10-17 09:40
author: anhpth
purpose: add column famed point

**/

CREATE TABLE `higgsup_kpi`.`kpi_point` (
  `id`                    INT(10) PRIMARY KEY AUTO_INCREMENT,
  `rated_username`        VARCHAR(50)
                          CHARACTER SET utf8 NOT NULL,
  `rule_point`            FLOAT              NULL,
  `club_point`            FLOAT              NULL,
  `normal_seminar_point`  FLOAT              NULL,
  `weekend_seminar_point` FLOAT              NULL,
  `support_point`         FLOAT              NULL,
  `teambuilding_point`    FLOAT              NULL,
  `personal_point`        FLOAT              NULL,
  `project_point`         FLOAT              NULL,
  `total_point`           FLOAT              NULL,
  `year_month_id`         INT(11)            NULL,
  `famed_point`           FLOAT              DEFAULT NULL,
  FOREIGN KEY (rated_username) REFERENCES kpi_user (user_name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

