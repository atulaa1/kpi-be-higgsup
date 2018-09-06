/**
date: 2018-09-06 15:00
author: ThanhLV
purpose: change column event and event_user
**/


/*change column event*/
ALTER TABLE kpi_event ADD updated_date DATETIME NULL;
ALTER TABLE kpi_event ADD address TEXT NULL;
ALTER TABLE kpi_event ADD event_additional_config TEXT NULL COMMENT 'event_additional_config is config for event';
ALTER TABLE kpi_event CHANGE name event_name VARCHAR(350);
ALTER TABLE kpi_event MODIFY status TINYINT(4) COMMENT 'status maybe is 1== wait for confirmation 2== cancel..vv.. ,';

/*change column kpi_event_user*/
ALTER TABLE kpi_event_user CHANGE is_host type TINYINT(4) COMMENT 'type can 1 is host , 2 is member , 3 is listener';