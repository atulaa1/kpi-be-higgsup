/**
date: 2018-10-02 14:08
author: anhpth
purpose: add event creator (reference to user_name of kpi_user)

**/
ALTER TABLE `higgsup_kpi`.`kpi_event`
ADD COLUMN `creator` VARCHAR(50) NULL
AFTER `address`,
ADD INDEX `kpi_event_fk_2_idx` (`creator` ASC);

ALTER TABLE `higgsup_kpi`.`kpi_event`
ADD CONSTRAINT `kpi_event_fk_2`
  FOREIGN KEY (`creator`)
  REFERENCES `higgsup_kpi`.`kpi_user` (`user_name`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;



