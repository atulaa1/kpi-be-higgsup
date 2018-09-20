/**
date: 2018-09-20 10:30
author: ThanhLV
purpose: create table kpi_seminar_survey
purpose: add new column status for kpi_event_user table

**/
CREATE TABLE `kpi_seminar_survey` (
  `id` int(10) PRIMARY KEY AUTO_INCREMENT,
  `evaluating_username` varchar(50) CHARACTER SET utf8 NOT NULL,
  `event_id` int(11),
  `evaluated_username` varchar(50) CHARACTER SET utf8 NOT NULL,
  `rating` varchar(50) CHARACTER SET utf8 NOT NULL,

  FOREIGN KEY (evaluating_username) REFERENCES kpi_event_user(user_name),
  FOREIGN KEY (evaluated_username) REFERENCES kpi_event_user(user_name),
  FOREIGN KEY (event_id) REFERENCES kpi_event(id)


) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE kpi_event_user ADD status TINYINT DEFAULT 0 NULL COMMENT 'status can 0 is unfinished , 1 is finish';