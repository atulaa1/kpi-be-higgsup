/**
date: 2018-08-31
author: Thieu Thanh Tu
purpose: Change size of column name of table kpi_group
**/

ALTER TABLE kpi_group CHANGE COLUMN name name VARCHAR(350) NULL DEFAULT NULL ;