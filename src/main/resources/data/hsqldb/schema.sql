/*
 * HSQLDB script.
 * Create the database schema for the application.
 */
DROP SEQUENCE IDS IF EXISTS;
CREATE SEQUENCE IDS;

DROP TABLE ACCOUNT IF EXISTS;

CREATE TABLE ACCOUNT (
  ID bigint GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
  NAME VARCHAR(500) NOT NULL,
  AMOUNT DECIMAL(10,2) NOT NULL
);

DROP TABLE ACCOUNT_TX IF EXISTS;

CREATE TABLE ACCOUNT_TX (
  ID bigint GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
  FROM_ACCOUNT_ID BIGINT NOT NULL,
  TO_ACCOUNT_ID BIGINT NOT NULL,
  DATE DATETIME NOT NULL,
  AMOUNT DECIMAL(10,2) NOT NULL
);

ALTER TABLE ACCOUNT_TX ADD FOREIGN KEY (FROM_ACCOUNT_ID) REFERENCES ACCOUNT (ID);
ALTER TABLE ACCOUNT_TX ADD FOREIGN KEY (TO_ACCOUNT_ID) REFERENCES ACCOUNT (ID);
