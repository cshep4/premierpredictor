create table User
(
   id integer not null AUTO_INCREMENT,
   firstname varchar(50) not null,
   surname varchar(50) not null,
   email varchar(255) not null,
   password varchar(255) not null,
   primary key(id)
);
drop table IF EXISTS Token;
create table Token
(
  token varchar(255) not null,
  used boolean DEFAULT FALSE,
  PRIMARY KEY(token)
);

CREATE TABLE `Match` (
  id int(2) NOT NULL AUTO_INCREMENT,
  hTeam varchar(255) NOT NULL,
  aTeam varchar(255) NOT NULL,
  hGoals int(2) DEFAULT NULL,
  aGoals int(2) DEFAULT NULL,
  played tinyint(1) DEFAULT '0',
  dateTime datetime NOT NULL,
  matchday int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
);

CREATE TABLE Prediction (
  id int(11) NOT NULL,
  hGoals int(11) DEFAULT NULL,
  aGoals int(11) DEFAULT NULL,
  userID int(11) DEFAULT NULL,
  matchID int(2) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (matchID) REFERENCES `Match`(id),
  FOREIGN KEY (userID) REFERENCES `User`(id)
);

