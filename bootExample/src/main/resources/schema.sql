DROP TABLE IF EXISTS `CUSTOMER`;
CREATE TABLE `CUSTOMER`(
   ID   INT  PRIMARY KEY AUTO_INCREMENT,
   NAME VARCHAR (20)     NOT NULL,
   AGE  INT              NOT NULL,
   ADDRESS CHAR (25) ,
   SALARY DECIMAL (18, 2)
);
