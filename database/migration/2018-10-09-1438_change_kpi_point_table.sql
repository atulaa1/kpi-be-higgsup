/**
date: 2018-10-09 14:40
author: tutt
purpose: change column year_month_id in kpi_point table

**/

ALTER TABLE `higgsup_kpi`.`kpi_point`
CHANGE COLUMN `year_month` `year_month_id` INT(11) NOT NULL ,
ALTER TABLE `higgsup_kpi`.`kpi_point`
ADD CONSTRAINT `kpi_point_ibfk_2`
  FOREIGN KEY (`year_month_id`)
  REFERENCES `higgsup_kpi`.`kpi_year_month` (`id`)
