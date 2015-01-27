--DROP TABLE NOM_EQ_STATUSES
CREATE TABLE NOM_EQ_STATUSES(
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	DESCRIPTION VARCHAR(256) NOT NULL
);
/*
INSERT INTO NOM_EQ_STATUSES (DESCRIPTION) VALUES('�����');
SELECT * FROM NOM_EQ_STATUSES
*/


--DROP TABLE NOM_EQ_TYPES
CREATE TABLE NOM_EQ_TYPES(
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	DESCRIPTION VARCHAR(256) NOT NULL
);
/*
INSERT INTO NOM_EQ_TYPES (DESCRIPTION) VALUES('����');
SELECT * FROM NOM_EQ_TYPES
*/


--DROP TABLE NOM_ME_TYPES
CREATE TABLE NOM_ME_TYPES(
	ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	DESCRIPTION VARCHAR(256) NOT NULL
);
/*
INSERT INTO NOM_ME_TYPES (DESCRIPTION) VALUES('�����������');
SELECT * FROM NOM_ME_TYPES;
*/


--DROP TABLE MAIN_HALLS
CREATE TABLE MAIN_HALLS(
	HALL_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	HALL_NAME VARCHAR(50) NOT NULL,
	HALL_FLOOR INTEGER NOT NULL,
	HALL_CAPACITY INTEGER NOT NULL
);
/*
INSERT INTO MAIN_HALLS (HALL_NAME,HALL_FLOOR,HALL_CAPACITY) VALUES('���� C#',2,40);
SELECT * FROM MAIN_HALLS
*/


--DROP TABLE MAIN_EQUIPMENT_INVENTORY
CREATE TABLE MAIN_EQUIPMENT_INVENTORY(
	EQ_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	EQ_TYPE_ID INTEGER NOT NULL,	
	EQ_COUNT_AVAILABLE INTEGER NOT NULL
);
/*
INSERT INTO MAIN_EQUIPMENT_INVENTORY (EQ_TYPE_ID, EQ_COUNT_AVAILABLE) VALUES (1, 15);
INSERT INTO MAIN_EQUIPMENT_INVENTORY (EQ_TYPE_ID, EQ_COUNT_AVAILABLE) VALUES (2, 5);
INSERT INTO MAIN_EQUIPMENT_INVENTORY (EQ_TYPE_ID, EQ_COUNT_AVAILABLE) VALUES (3, 6);
SELECT * FROM MAIN_EQUIPMENT_INVENTORY;
SELECT NQT.ID, NQT.DESCRIPTION, MEI.EQ_COUNT_AVAILABLE
	FROM MAIN_EQUIPMENT_INVENTORY AS MEI
	INNER JOIN NOM_EQ_TYPES AS NQT ON NQT.ID = MEI.EQ_TYPE_ID
	WHERE NQT.ID = 1
	--INNER JOIN NOM_EQ_STATUSES AS NES ON NES.ID = MEI.EQ_STATUS_ID
*/


--DROP TABLE MAIN_REQUESTS_EQUIPMENT
CREATE TABLE MAIN_REQUESTS_EQUIPMENT(    	
	REQ_ID INTEGER NOT NULL,	
	REQ_EQ_TYPE_ID INTEGER NOT NULL,	
	REQ_EQ_COUNT INTEGER NOT NULL,
	REQ_EQ_POSITIONS VARCHAR(4096)    
);
/*
INSERT INTO MAIN_REQUESTS_EQUIPMENT (REQ_ID, REQ_EQ_TYPE_ID, REQ_EQ_COUNT, REQ_EQ_POSITIONS) VALUES ( 1, 1, 1, '10,30;20,30;30,30;40,30;50,30;');
SELECT * FROM MAIN_REQUESTS_EQUIPMENT
*/


--DROP TABLE MAIN_REQUESTS
CREATE TABLE MAIN_REQUESTS(    	
	REQ_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),	
	REQ_NAME VARCHAR(200),
    REQ_CU_ID VARCHAR(15) NOT NULL,
    REQ_START_DATE TIMESTAMP NOT NULL,
    REQ_END_DATE TIMESTAMP NOT NULL
);
--INSERT INTO MAIN_REQUESTS (REQ_NAME, REQ_CU_ID, REQ_START_DATE, REQ_END_DATE) VALUES ('Test name',1,'2014-12-16','2014-12-16');
--SELECT * FROM MAIN_REQUESTS


--DROP TABLE MAIN_MEETINGS
CREATE TABLE MAIN_MEETINGS(
	ME_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	ME_NAME VARCHAR(50) NOT NULL,
	ME_TYPE_ID INTEGER NOT NULL,	
	ME_DESC VARCHAR(256) NOT NULL,
	ME_REQ_ID INTEGER
);
/*
INSERT INTO MAIN_MEETINGS (ME_NAME, ME_TYPE_ID, ME_DESC, ME_REQ_ID) VALUES ('JAVA EE', 2, 'JAVA2DAYS CONFERENCE', 1 );
SELECT * FROM MAIN_MEETINGS
*/


--DROP TABLE MAIN_SCHEDULE
SELECT * FROM MAIN_SCHEDULE
CREATE TABLE MAIN_SCHEDULE(
	SCH_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	SCH_HALL_ID INTEGER NOT NULL,
	SCH_ME_ID INTEGER NOT NULL,
	ME_START_DATE TIMESTAMP NOT NULL,
	ME_END_DATE TIMESTAMP NOT NULL	
);
/*
 INSERT INTO MAIN_SCHEDULE( SCH_HALL_ID, SCH_ME_ID, ME_START_DATE, ME_END_DATE) VALUES( 1, 1, '2014-12-15 14:00:00', '2014-12-15 16:00:00');
 UPDATE MAIN_SCHEDULE SET ME_START_DATE='2014-12-16', ME_END_DATE = '2014-12-17', ME_START_TIME = '09:00', ME_END_TIME = '13:00' WHERE SCH_ID=3 
 SELECT * FROM MAIN_SCHEDULE
 
 SELECT MS.SCH_ID, MM.ME_DESC, MM.ME_NAME, MH.HALL_NAME, MS.ME_START_DATE, MS.ME_END_DATE, MM.ME_TYPE_ID, MM.ME_REQ_ID, MS.SCH_HALL_ID 
 	FROM MAIN_SCHEDULE AS MS	
 		INNER JOIN MAIN_MEETINGS AS MM ON MS.SCH_ME_ID = MM.ME_ID 
 			INNER JOIN MAIN_HALLS AS MH ON MS.SCH_HALL_ID = MH.HALL_ID"
 */


--DROP TABLE USERS
SET SCHEMA APP;
create table users (
  user_name         varchar(15) not null primary key,
  user_pass         varchar(15) not null,
  user_mail         varchar(40) not null 
);

SET SCHEMA APP;
create table user_roles (
  user_name         varchar(15) not null,
  role_name         varchar(15) not null,
  primary key (user_name, role_name)
);


SELECT * FROM USERS;
--INSERT INTO USERS VALUES('admin', '111', 'jotkata@abv.bg');
--INSERT INTO USERS VALUES('ivan', '222', 'ivanzhotev@gmail.com');
 
SELECT * FROM USER_ROLES;
--INSERT INTO user_roles VALUES( 'admin','administrator');
--INSERT INTO user_roles VALUES( 'ivan','user');



/*SELECTS*/	
SELECT MS.SCH_ID, MM.ME_DESC, MM.ME_NAME, MH.HALL_NAME, MS.ME_START_DATE, MS.ME_END_DATE FROM MAIN_SCHEDULE AS MS	
	INNER JOIN MAIN_MEETINGS AS MM ON MS.SCH_ME_ID = MM.ME_ID 
		INNER JOIN MAIN_HALLS AS MH ON MS.SCH_HALL_ID = MH.HALL_ID
	
/*GET OCCUPIED ITEMS COUNT FOR CURRENT DATE*/
SELECT MRQ.REQ_EQ_TYPE_ID, SUM(MRQ.REQ_EQ_COUNT) FROM MAIN_REQUESTS_EQUIPMENT AS MRQ
	INNER JOIN NOM_EQ_TYPES AS NET ON MRQ.REQ_EQ_TYPE_ID = NET.ID
		INNER JOIN MAIN_MEETINGS AS MM ON MRQ.REQ_ID = MM.ME_REQ_ID
			INNER JOIN MAIN_SCHEDULE AS MS ON MS.SCH_ME_ID = MM.ME_ID
				WHERE MS.ME_START_DATE>='2015-01-29 00:00:00'				
				AND MS.ME_END_DATE<='2015-01-29 23:59:00'					
			 GROUP BY MRQ.REQ_EQ_TYPE_ID;