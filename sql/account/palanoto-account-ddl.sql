-- PALANOTO-ACCOUNT-DDL
 DROP TABLE IF EXISTS `TB_USER_ROLE`;
 DROP TABLE IF EXISTS `TB_ROLE`;
 DROP TABLE IF EXISTS `TB_USER`;

-- ##################################################################################################################
-- 													   TABLE
-- ##################################################################################################################

-- USER
CREATE TABLE IF NOT EXISTS `TB_USER` (
	`USER_NO` INT(11) NOT NULL AUTO_INCREMENT,
	`USER_ID` VARCHAR(50) NOT NULL UNIQUE,
	`USER_NAME` VARCHAR(50) NOT NULL,
	`PASSWORD` VARCHAR(100) NOT NULL,
	`REG_DATE` DATETIME NOT NULL DEFAULT NOW(),
	`ENABLED` BIT DEFAULT 1,
	PRIMARY KEY(`USER_NO`)
) ENGINE = INNODB;

-- ROLE
CREATE TABLE IF NOT EXISTS `TB_ROLE` (
	`ROLE_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`ROLE_NAME` VARCHAR(50) NOT NULL UNIQUE,
	PRIMARY KEY(`ROLE_ID`)
) ENGINE = INNODB;

-- User Role Mapping
CREATE TABLE IF NOT EXISTS `TB_USER_ROLE` (
	`USER_NO` INT(11) NOT NULL,
	`ROLE_ID` INT(11) NOT NULL,
	PRIMARY KEY(`USER_NO`, `ROLE_ID`)
) ENGINE = INNODB;

-- ##################################################################################################################
-- 													   CONSTRAINT
-- ##################################################################################################################

ALTER TABLE `TB_USER_ROLE` ADD CONSTRAINT `FK_USER_ROLE_USER` FOREIGN KEY(`USER_NO`) REFERENCES TB_USER(`USER_NO`) ON DELETE CASCADE;
ALTER TABLE `TB_USER_ROLE` ADD CONSTRAINT `FK_USER_ROLE_ROLE` FOREIGN KEY(`ROLE_ID`) REFERENCES TB_ROLE(`ROLE_ID`) ON DELETE CASCADE;