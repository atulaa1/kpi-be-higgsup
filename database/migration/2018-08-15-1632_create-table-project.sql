CREATE TABLE kpi_project (
  id int(11) NOT NULL AUTO_INCREMENT,
  active tinyint(4) NULL DEFAULT 1,
  created_date datetime(0) NULL DEFAULT NULL,
  name varchar(255),
  PRIMARY KEY (id)
);