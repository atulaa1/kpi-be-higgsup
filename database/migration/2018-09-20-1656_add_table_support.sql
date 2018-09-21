/**
date: 2018-09-20 16:54
author: ThanhLV
purpose: create table kip_support

**/

CREATE TABLE kpi_support
(
  id INT PRIMARY KEY AUTO_INCREMENT,
  task VARCHAR(500),
  task_point FLOAT,
  created_date      DATETIME     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;