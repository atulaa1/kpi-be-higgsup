/**
date: 2018-10-18 18:34
author: hiepnnt
purpose: create new table for new feature
**/
CREATE TABLE `kpi_point_detail` (
  `id`                    INT(11) PRIMARY KEY AUTO_INCREMENT,
  `event_id`              INT(11)            NULL,
  `user_name`             VARCHAR(50)
                          CHARACTER SET utf8 NOT NULL,
  `point_type_id`         INT(11)            NOT NULL,
  `point`                 INT(11)            NOT NULL,
  `year_month_id`         INT(11)            NULL,
  FOREIGN KEY (event_id) REFERENCES kpi_event (id),
  FOREIGN KEY (user_name) REFERENCES kpi_user (user_name),
  FOREIGN KEY (year_month_id) REFERENCES kpi_year_month(id),
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `kpi_point`
  ADD `title_id`  INT(11)