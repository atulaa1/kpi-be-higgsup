/**
date: 2018-10-09 14:40
author: tutt
purpose: change column year_month_id in kpi_point table

**/

ALTER TABLE `higgsup_kpi`.`kpi_point`
  CHANGE COLUMN `year_month` `year_month_id` INT(11) NOT NULL

