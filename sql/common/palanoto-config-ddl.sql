-- PALANOTO-CONFIG-DDL

DROP TABLE IF EXISTS `TB_CONFIG`;


-- ##################################################################################################################
-- 													   TABLE
-- ##################################################################################################################

CREATE TABLE IF NOT EXISTS `TB_CONFIG` (
	`ID` VARCHAR(50) NOT NULL,
	`VALUE` VARCHAR(100) ,
	PRIMARY KEY(`ID`)
) ENGINE=INNODB;