/**
date: 2018-10-05 10:20
author: anhpth
purpose: update unique for project log

**/

CREATE TABLE `kpi_project_log` (
  `id` int(11) NOT NULL,
  `rated_username` varchar(50) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `year_month` int(11) NOT NULL,
  `project_point` float DEFAULT NULL,
  `man_username` varchar(50) DEFAULT NULL,
  `yearmonth` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique1` (`rated_username`,`project_id`,`year_month`,`man_username`) USING BTREE,
  KEY `fk_project` (`project_id`),
  KEY `fk_man` (`man_username`),
  CONSTRAINT `fk_man` FOREIGN KEY (`man_username`) REFERENCES `kpi_user` (`user_name`),
  CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `kpi_project` (`id`),
  CONSTRAINT `fk_username` FOREIGN KEY (`rated_username`) REFERENCES `kpi_project_user` (`rated_username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;