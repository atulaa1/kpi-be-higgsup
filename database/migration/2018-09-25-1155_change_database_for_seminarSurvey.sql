/**
date: 2018-09-25 12:00
author: Tutt
purpose: edit table kpi_seminar_survey
purpose: add new column status for kpi_event_user table

**/
ALTER TABLE `higgsup_kpi`.`kpi_seminar_survey`
  DROP FOREIGN KEY `kpi_seminar_survey_ibfk_1`,
  DROP FOREIGN KEY `kpi_seminar_survey_ibfk_2`;
ALTER TABLE `higgsup_kpi`.`kpi_seminar_survey`
  ADD INDEX `kpi_seminar_survey_ibfk_1_idx` (`evaluating_username` ASC),
  ADD INDEX `kpi_seminar_survey_ibfk_2_idx` (`evaluated_username` ASC),
  DROP INDEX `evaluated_username`,
  DROP INDEX `evaluating_username`;
ALTER TABLE `higgsup_kpi`.`kpi_seminar_survey`
  ADD CONSTRAINT `kpi_seminar_survey_ibfk_1`
FOREIGN KEY (`evaluating_username`)
REFERENCES `higgsup_kpi`.`kpi_user` (`user_name`),
  ADD CONSTRAINT `kpi_seminar_survey_ibfk_2`
FOREIGN KEY (`evaluated_username`)
REFERENCES `higgsup_kpi`.`kpi_user` (`user_name`);


