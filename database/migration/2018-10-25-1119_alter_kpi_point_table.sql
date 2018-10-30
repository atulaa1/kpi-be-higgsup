/**
date: 2018-10-25 11:19
author: hiepnnt
purpose: alter table kpi_point and modify data type kpi_point_detail
**/

ALTER TABLE `kpi_point`
  ALTER `rule_point`           SET  DEFAULT 0,
  ALTER`club_point`            SET  DEFAULT 0,
  ALTER`normal_seminar_point`  SET  DEFAULT 0,
  ALTER`weekend_seminar_point` SET  DEFAULT 0,
  ALTER`support_point`         SET  DEFAULT 0,
  ALTER `teambuilding_point`   SET  DEFAULT 0,
  ALTER`personal_point`        SET  DEFAULT 0,
  ALTER`project_point`         SET  DEFAULT 0,
  ALTER`total_point`           SET  DEFAULT 0,
  ALTER`famed_point`           SET  DEFAULT 0,
  ALTER`title`                 SET  DEFAULT 0;

ALTER TABLE `kpi_point_detail`
  MODIFY COLUMN `point` FLOAT;