/**
date: 2018-10-02 15:14
author: TuTT
purpose: create table kpi_point and kpi_project_user

**/

CREATE TABLE `higgsup_kpi`.`kpi_point` (
  `id`                   int(10) PRIMARY KEY AUTO_INCREMENT,
  `user_name`            VARCHAR(50) CHARACTER SET utf8 NOT NULL,
  `rule_point`           FLOAT                          NOT NULL,
  `club_point`           FLOAT                          NULL,
  `normal_seminar_point` FLOAT                          NULL,
  `weekend_seminar`      FLOAT                          NULL,
  `support_point`        FLOAT                          NULL,
  `teambuilding_point`   FLOAT                          NULL,
  `evaluating_point`     FLOAT                          NOT NULL,
  `total_point`          FLOAT                          NOT NULL,
  `year_month_id`        int(11)                        NOT NULL,
  FOREIGN KEY (user_name) REFERENCES kpi_event_user (user_name),
  FOREIGN KEY (year_month_id) REFERENCES kpi_year_month (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `higgsup_kpi`.`kpi_project_user` (
  user_name  VARCHAR(255) NOT NULL,
  project_id INT(11)      NOT NULL,
  FOREIGN KEY (user_name) REFERENCES kpi_user (user_name),
  FOREIGN KEY (project_id) REFERENCES kpi_project (id),
  CONSTRAINT PK_kpi_project_user PRIMARY KEY (user_name, project_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;