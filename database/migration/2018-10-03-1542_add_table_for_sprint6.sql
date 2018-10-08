/**
date: 2018-10-03 15:50
author: anhpth
purpose: add table for sprint 6

**/

-- show infomation of user join in project
CREATE TABLE `kpi_project_user` (
  `id`             INT(10) PRIMARY KEY AUTO_INCREMENT,
  `rated_username` varchar(50) NOT NULL,
  `project_id`     INT(11)             DEFAULT NULL,
  `joined_date`    INT(11)             DEFAULT NULL,
  UNIQUE KEY `unique_project_user` (`rated_username`,`project_id`) USING BTREE,
  KEY `fk_project_id` (`project_id`),
  CONSTRAINT `fk_project_id` FOREIGN KEY (`project_id`) REFERENCES `kpi_project` (`id`),
  KEY `fk_user` (`rated_username`),
  CONSTRAINT `fk_user` FOREIGN KEY (`rated_username`) REFERENCES `kpi_user` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- infomation of project in month
CREATE TABLE `kpi_project_log` (
  `id` int(11) NOT NULL,
  `rated_username` varchar(50) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `year_month` int(11) NOT NULL,
  `project_point` float DEFAULT NULL,
  `man_username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique1` (`rated_username`,`project_id`,`year_month`) USING BTREE,
  KEY `fk_project` (`project_id`),
  CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `kpi_project` (`id`),
  CONSTRAINT `fk_username` FOREIGN KEY (`rated_username`) REFERENCES `kpi_project_user` (`rated_username`),
  CONSTRAINT `fk_man` FOREIGN KEY (`man_username`) REFERENCES `kpi_user` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `higgsup_kpi`.`kpi_point` (
  `id`                    int(10) PRIMARY KEY AUTO_INCREMENT,
  `rated_username`        VARCHAR(50) CHARACTER SET utf8 NOT NULL,
  `rule_point`            FLOAT                          NOT NULL,
  `club_point`            FLOAT                          NULL,
  `normal_seminar_point`  FLOAT                          NULL,
  `weekend_seminar_point` FLOAT                          NULL,
  `support_point`         FLOAT                          NULL,
  `teambuilding_point`    FLOAT                          NULL,
  `personal_point`        FLOAT                          NOT NULL,
  `project_point`         FLOAT                          NOT NULL,
  `total_point`           FLOAT                          NOT NULL,
  `year_month`            int(11)                        NOT NULL,
  FOREIGN KEY (rated_username) REFERENCES kpi_user (user_name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;