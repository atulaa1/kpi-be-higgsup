/**
date: 2018-08-15
author: ThanhLV
purpose: add cloumns info user and change
**/

ALTER TABLE kpi_user ADD birthday DATE NULL;
ALTER TABLE kpi_user ADD number_phone VARCHAR(20) NULL;
ALTER TABLE kpi_user ADD address NVARCHAR(250) NULL;
ALTER TABLE kpi_user ADD gmail VARCHAR(200) NULL;
ALTER TABLE kpi_user ADD skype VARCHAR(200) NULL;
ALTER TABLE kpi_user ADD year_work INT NULL;
ALTER TABLE kpi_user MODIFY avatar_url MEDIUMTEXT;