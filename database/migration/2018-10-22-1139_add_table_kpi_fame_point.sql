/**
date: 2018-10-22 11:39
author: hiepnnt
purpose: add table kpi_fame_point

**/

CREATE TABLE `kpi_fame_point` (
  `id`                    INT(10) PRIMARY KEY AUTO_INCREMENT,
  `username`              VARCHAR(50)
                          CHARACTER SET utf8 NOT NULL,
  `fame_point`            FLOAT              NOT NULL,
  `year`                  INT(11)              NULL,
  FOREIGN KEY (username) REFERENCES kpi_user (user_name)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;