/**
date: 2018-08-30
author: Thieu Thanh Tu
purpose: Change size of column question
**/

ALTER TABLE kpi_survey_question_man CHANGE COLUMN question question VARCHAR(2000) NULL DEFAULT NULL ;
