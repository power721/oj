-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: oj
-- ------------------------------------------------------
-- Server version	5.5.49-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `oj`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `oj` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `oj`;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `board` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `cid` int(9) NOT NULL DEFAULT '0',
  `uid` int(9) NOT NULL,
  `solved` int(5) NOT NULL DEFAULT '0',
  `penalty` int(11) NOT NULL DEFAULT '0',
  `A_SolvedTime` int(11) DEFAULT '0',
  `A_WrongNum` tinyint(5) DEFAULT '0',
  `B_SolvedTime` int(11) DEFAULT '0',
  `B_WrongNum` tinyint(5) DEFAULT '0',
  `C_SolvedTime` int(11) DEFAULT '0',
  `C_WrongNum` tinyint(5) DEFAULT '0',
  `D_SolvedTime` int(11) DEFAULT '0',
  `D_WrongNum` tinyint(5) DEFAULT '0',
  `E_SolvedTime` int(11) DEFAULT '0',
  `E_WrongNum` tinyint(5) DEFAULT '0',
  `F_SolvedTime` int(11) DEFAULT '0',
  `F_WrongNum` tinyint(5) DEFAULT '0',
  `G_SolvedTime` int(11) DEFAULT '0',
  `G_WrongNum` tinyint(5) DEFAULT '0',
  `H_SolvedTime` int(11) DEFAULT '0',
  `H_WrongNum` tinyint(5) DEFAULT '0',
  `I_SolvedTime` int(11) DEFAULT '0',
  `I_WrongNum` tinyint(5) DEFAULT '0',
  `J_SolvedTime` int(11) DEFAULT '0',
  `J_WrongNum` tinyint(5) DEFAULT '0',
  `K_SolvedTime` int(11) DEFAULT '0',
  `K_WrongNum` tinyint(5) DEFAULT '0',
  `L_SolvedTime` int(11) DEFAULT '0',
  `L_WrongNum` tinyint(5) DEFAULT '0',
  `M_SolvedTime` int(11) DEFAULT '0',
  `M_WrongNum` tinyint(5) DEFAULT '0',
  `N_SolvedTime` int(11) DEFAULT '0',
  `N_WrongNum` tinyint(5) DEFAULT '0',
  `O_SolvedTime` int(11) DEFAULT '0',
  `O_WrongNum` tinyint(5) DEFAULT '0',
  `P_SolvedTime` int(11) DEFAULT '0',
  `P_WrongNum` tinyint(5) DEFAULT '0',
  `Q_SolvedTime` int(11) DEFAULT '0',
  `Q_WrongNum` tinyint(5) DEFAULT '0',
  `R_SolvedTime` int(11) DEFAULT '0',
  `R_WrongNum` tinyint(5) DEFAULT '0',
  `S_SolvedTime` int(11) DEFAULT '0',
  `S_WrongNum` tinyint(5) DEFAULT '0',
  `T_SolvedTime` int(11) DEFAULT '0',
  `T_WrongNum` tinyint(5) DEFAULT '0',
  `U_SolvedTime` int(11) DEFAULT '0',
  `U_WrongNum` tinyint(5) DEFAULT '0',
  `V_SolvedTime` int(11) DEFAULT '0',
  `V_WrongNum` tinyint(5) DEFAULT '0',
  `W_SolvedTime` int(11) DEFAULT '0',
  `W_WrongNum` tinyint(5) DEFAULT '0',
  `X_SolvedTime` int(11) DEFAULT '0',
  `X_WrongNum` tinyint(5) DEFAULT '0',
  `Y_SolvedTime` int(11) DEFAULT '0',
  `Y_WrongNum` tinyint(5) DEFAULT '0',
  `Z_SolvedTime` int(11) DEFAULT '0',
  `Z_WrongNum` tinyint(5) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `board_cid_uid_pk` (`cid`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `parent` int(9) NOT NULL DEFAULT '0',
  `name` varchar(2048) NOT NULL,
  `zh` varchar(2048) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `threadId` int(9) NOT NULL,
  `quoteId` int(9) DEFAULT NULL,
  `content` text NOT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contest`
--

DROP TABLE IF EXISTS `contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contest` (
  `cid` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `startTime` int(11) DEFAULT '0',
  `endTime` int(11) DEFAULT '0',
  `description` text,
  `report` text,
  `type` tinyint(4) NOT NULL DEFAULT '0',
  `password` varchar(255) DEFAULT NULL,
  `languages` varchar(255) DEFAULT NULL,
  `lockBoard` tinyint(1) NOT NULL DEFAULT '0',
  `atime` int(11) DEFAULT NULL,
  `ctime` int(11) DEFAULT NULL,
  `mtime` int(11) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `lockReport` tinyint(1) NOT NULL DEFAULT '0',
  `lockBoardTime` int(9) NOT NULL DEFAULT '60',
  `unlockBoardTime` int(9) NOT NULL DEFAULT '30',
  PRIMARY KEY (`cid`),
  KEY `search` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contest_clarify`
--

DROP TABLE IF EXISTS `contest_clarify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contest_clarify` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `cid` int(9) NOT NULL,
  `num` tinyint(5) DEFAULT NULL,
  `uid` int(9) NOT NULL,
  `admin` int(9) DEFAULT NULL,
  `question` text NOT NULL,
  `reply` text,
  `public` tinyint(1) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  `atime` int(11) DEFAULT NULL COMMENT 'answer timestamp',
  `mtime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contest_problem`
--

DROP TABLE IF EXISTS `contest_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contest_problem` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `cid` int(9) NOT NULL DEFAULT '0',
  `pid` int(9) NOT NULL DEFAULT '0',
  `title` char(255) NOT NULL,
  `timeLimit` int(5) NOT NULL DEFAULT '1000',
  `memoryLimit` int(5) NOT NULL DEFAULT '65536',
  `num` int(5) NOT NULL DEFAULT '0',
  `view` int(5) NOT NULL DEFAULT '0',
  `accepted` int(5) NOT NULL DEFAULT '0',
  `submission` int(5) NOT NULL DEFAULT '0',
  `firstBloodUid` int(9) NOT NULL DEFAULT '0' COMMENT 'first user(uid) solved this problem',
  `firstBloodTime` int(9) NOT NULL DEFAULT '-1' COMMENT 'first time(minutes) solved this problem',
  `maxSim` int(3) NOT NULL DEFAULT 100,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contest_problem_cid_pid_pk` (`cid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contest_solution`
--

DROP TABLE IF EXISTS `contest_solution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contest_solution` (
  `sid` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `pid` int(9) NOT NULL,
  `cid` int(9) NOT NULL DEFAULT '0',
  `num` int(3) DEFAULT NULL,
  `time` int(9) DEFAULT NULL,
  `memory` int(9) DEFAULT NULL,
  `result` int(5) NOT NULL,
  `language` int(3) NOT NULL,
  `ctime` int(11) NOT NULL,
  `mtime` int(11) NOT NULL,
  `test` int(9) NOT NULL DEFAULT '0',
  `wrong` text,
  `error` text,
  `source` text NOT NULL,
  `codeLen` int(9) NOT NULL DEFAULT '0',
  `systemError` text,
  `sim` int(3) NOT NULL DEFAULT 0,
  `sim_id` int(9) NOT NULL DEFAULT 0,
  `balloon` tinyint(1) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contest_user`
--

DROP TABLE IF EXISTS `contest_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contest_user` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `cid` int(9) NOT NULL,
  `special` tinyint(1) NOT NULL DEFAULT '0',
  `freshman` tinyint(1) NOT NULL DEFAULT '0',
  `girls` tinyint(1) NOT NULL DEFAULT '0',
  `nick` varchar(55) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  `teamName` varchar(20) NOT NULL DEFAULT 'NoTeamName',
  PRIMARY KEY (`id`),
  UNIQUE KEY `contest_user_cid_uid_pk` (`cid`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `freeze_board`
--

DROP TABLE IF EXISTS `freeze_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `freeze_board` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `cid` int(9) NOT NULL DEFAULT '0',
  `uid` int(9) NOT NULL,
  `solved` int(5) NOT NULL DEFAULT '0',
  `penalty` int(11) NOT NULL DEFAULT '0',
  `A_SolvedTime` int(11) DEFAULT '0',
  `A_WrongNum` tinyint(5) DEFAULT '0',
  `B_SolvedTime` int(11) DEFAULT '0',
  `B_WrongNum` tinyint(5) DEFAULT '0',
  `C_SolvedTime` int(11) DEFAULT '0',
  `C_WrongNum` tinyint(5) DEFAULT '0',
  `D_SolvedTime` int(11) DEFAULT '0',
  `D_WrongNum` tinyint(5) DEFAULT '0',
  `E_SolvedTime` int(11) DEFAULT '0',
  `E_WrongNum` tinyint(5) DEFAULT '0',
  `F_SolvedTime` int(11) DEFAULT '0',
  `F_WrongNum` tinyint(5) DEFAULT '0',
  `G_SolvedTime` int(11) DEFAULT '0',
  `G_WrongNum` tinyint(5) DEFAULT '0',
  `H_SolvedTime` int(11) DEFAULT '0',
  `H_WrongNum` tinyint(5) DEFAULT '0',
  `I_SolvedTime` int(11) DEFAULT '0',
  `I_WrongNum` tinyint(5) DEFAULT '0',
  `J_SolvedTime` int(11) DEFAULT '0',
  `J_WrongNum` tinyint(5) DEFAULT '0',
  `K_SolvedTime` int(11) DEFAULT '0',
  `K_WrongNum` tinyint(5) DEFAULT '0',
  `L_SolvedTime` int(11) DEFAULT '0',
  `L_WrongNum` tinyint(5) DEFAULT '0',
  `M_SolvedTime` int(11) DEFAULT '0',
  `M_WrongNum` tinyint(5) DEFAULT '0',
  `N_SolvedTime` int(11) DEFAULT '0',
  `N_WrongNum` tinyint(5) DEFAULT '0',
  `O_SolvedTime` int(11) DEFAULT '0',
  `O_WrongNum` tinyint(5) DEFAULT '0',
  `P_SolvedTime` int(11) DEFAULT '0',
  `P_WrongNum` tinyint(5) DEFAULT '0',
  `Q_SolvedTime` int(11) DEFAULT '0',
  `Q_WrongNum` tinyint(5) DEFAULT '0',
  `R_SolvedTime` int(11) DEFAULT '0',
  `R_WrongNum` tinyint(5) DEFAULT '0',
  `S_SolvedTime` int(11) DEFAULT '0',
  `S_WrongNum` tinyint(5) DEFAULT '0',
  `T_SolvedTime` int(11) DEFAULT '0',
  `T_WrongNum` tinyint(5) DEFAULT '0',
  `U_SolvedTime` int(11) DEFAULT '0',
  `U_WrongNum` tinyint(5) DEFAULT '0',
  `V_SolvedTime` int(11) DEFAULT '0',
  `V_WrongNum` tinyint(5) DEFAULT '0',
  `W_SolvedTime` int(11) DEFAULT '0',
  `W_WrongNum` tinyint(5) DEFAULT '0',
  `X_SolvedTime` int(11) DEFAULT '0',
  `X_WrongNum` tinyint(5) DEFAULT '0',
  `Y_SolvedTime` int(11) DEFAULT '0',
  `Y_WrongNum` tinyint(5) DEFAULT '0',
  `Z_SolvedTime` int(11) DEFAULT '0',
  `Z_WrongNum` tinyint(5) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `freeze_board_cid_uid_pk` (`cid`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `gid` int(9) NOT NULL DEFAULT '1',
  `userId` int(9) NOT NULL,
  `friendUid` int(9) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `friend_group`
--

DROP TABLE IF EXISTS `friend_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `name` varchar(35) NOT NULL,
  `count` int(9) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `honors`
--

DROP TABLE IF EXISTS `honors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `honors` (
  `level` varchar(255) NOT NULL,
  `contest` varchar(255) NOT NULL,
  `team` varchar(255) DEFAULT NULL,
  `player` varchar(255) NOT NULL DEFAULT '',
  `prize` varchar(255) NOT NULL DEFAULT '',
  `id` int(5) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level` (
  `level` int(5) NOT NULL AUTO_INCREMENT,
  `exp` int(9) NOT NULL,
  PRIMARY KEY (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `loginlog`
--

DROP TABLE IF EXISTS `loginlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loginlog` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL DEFAULT '0',
  `name` varchar(35) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mail`
--

DROP TABLE IF EXISTS `mail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `mid` int(9) NOT NULL COMMENT 'mail id',
  `user` int(9) NOT NULL COMMENT 'user id',
  `peer` int(9) NOT NULL COMMENT 'peer uid',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=new, 1=read, 2=deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mail_banlist`
--

DROP TABLE IF EXISTS `mail_banlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_banlist` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `bannedUid` int(9) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mail_content`
--

DROP TABLE IF EXISTS `mail_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mail_content` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fromUid` int(9) NOT NULL,
  `toUid` int(9) NOT NULL,
  `content` text,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `title` varchar(1000) NOT NULL DEFAULT '',
  `content` varchar(10000) DEFAULT '',
  `time` int(11) NOT NULL,
  `author` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `view` int(9) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `editorUid` int(9) DEFAULT NULL,
  `cid` int(9) DEFAULT NULL,
  `title` varchar(512) NOT NULL,
  `startTime` int(11) NOT NULL,
  `endTime` int(11) NOT NULL,
  `content` text,
  `atime` int(11) DEFAULT NULL,
  `ctime` int(11) NOT NULL,
  `mtime` int(11) DEFAULT NULL,
  `view` int(9) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `module` varchar(20) NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '1',
  `name` varchar(64) NOT NULL,
  `title` varchar(64) NOT NULL,
  `parentID` int(9) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem`
--

DROP TABLE IF EXISTS `problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem` (
  `pid` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `title` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `input` text,
  `output` text,
  `sampleInput` text,
  `sampleOutput` text,
  `hint` text,
  `source` varchar(255) DEFAULT NULL,
  `sampleProgram` text,
  `timeLimit` int(5) NOT NULL DEFAULT '1000',
  `memoryLimit` int(5) NOT NULL DEFAULT '65536',
  `atime` int(11) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  `mtime` int(11) NOT NULL,
  `stime` int(11) NOT NULL DEFAULT '0',
  `accepted` int(5) NOT NULL DEFAULT '0',
  `solved` int(5) NOT NULL DEFAULT '0',
  `submission` int(5) NOT NULL DEFAULT '0',
  `submitUser` int(5) NOT NULL DEFAULT '0',
  `error` int(5) NOT NULL DEFAULT '0',
  `ratio` tinyint(4) NOT NULL DEFAULT '0',
  `difficulty` tinyint(4) NOT NULL DEFAULT '0',
  `view` int(9) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `problem_category`
--

DROP TABLE IF EXISTS `problem_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `problem_category` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `pid` int(9) NOT NULL,
  `cid` int(9) NOT NULL,
  `weight` int(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_language`
--

DROP TABLE IF EXISTS `program_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_language` (
  `id` tinyint(4) NOT NULL,
  `name` varchar(10) NOT NULL,
  `description` varchar(255) NOT NULL,
  `extTime` int(9) NOT NULL DEFAULT '0',
  `extMemory` int(9) NOT NULL DEFAULT '0',
  `timeFactor` tinyint(3) NOT NULL DEFAULT '1',
  `memoryFactor` tinyint(3) NOT NULL DEFAULT '1',
  `ext` varchar(9) NOT NULL,
  `exe` varchar(9) NOT NULL,
  `complieOrder` tinyint(3) NOT NULL DEFAULT '0',
  `compileCmd` varchar(255) NOT NULL,
  `brush` varchar(15) DEFAULT '' COMMENT 'SyntaxHighlighter brush',
  `script` tinyint(1) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `os`  varchar(255) NULL DEFAULT 'Windows',
  `arch`  varchar(255) NULL DEFAULT 'x64',
  `path` text NOT NULL,
  `ctime` int(11) NOT NULL,
  `download` int(9) DEFAULT '0',
  `access` enum('public','private','security') NOT NULL DEFAULT 'public',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(80) NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `rid` int(9) NOT NULL,
  `pid` int(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `session` (
  `sessionId` varchar(40) NOT NULL DEFAULT '0',
  `uid` int(9) NOT NULL DEFAULT '0',
  `name` varchar(35) DEFAULT NULL,
  `ipAddress` varchar(45) DEFAULT '',
  `userAgent` varchar(255) DEFAULT '',
  `ctime` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'Session create time.',
  `lastActivity` int(11) NOT NULL DEFAULT '0',
  `sessionExpires` int(11) NOT NULL,
  `session_data` blob,
  `user_data` text,
  `uri` varchar(255) DEFAULT '',
  PRIMARY KEY (`sessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solution`
--

DROP TABLE IF EXISTS `solution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `solution` (
  `sid` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `pid` int(9) NOT NULL,
  `cid` int(9) NOT NULL DEFAULT '0',
  `num` int(3) DEFAULT NULL,
  `time` int(9) DEFAULT NULL,
  `memory` int(9) DEFAULT NULL,
  `result` int(5) NOT NULL,
  `language` int(3) NOT NULL,
  `ctime` int(11) NOT NULL,
  `mtime` int(11) NOT NULL,
  `test` int(9) NOT NULL DEFAULT '0',
  `wrong` text,
  `error` text,
  `source` text NOT NULL,
  `codeLen` int(9) NOT NULL DEFAULT '0',
  `systemError` text,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `pid` int(5) unsigned NOT NULL DEFAULT '0',
  `uid` int(9) DEFAULT NULL,
  `tag` varchar(45) NOT NULL DEFAULT '',
  `ctime` int(11) NOT NULL DEFAULT '0',
  `mtime` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team` (
  `tid` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `name1` varchar(30) NOT NULL,
  `stuId1` varchar(16) NOT NULL,
  `college1` varchar(50) NOT NULL,
  `class1` varchar(20) NOT NULL,
  `gender1` enum('female','male') NOT NULL,
  `contact1` varchar(100) NOT NULL,
  `name2` varchar(30) DEFAULT NULL,
  `stuId2` varchar(16) DEFAULT '',
  `college2` varchar(50) DEFAULT NULL,
  `class2` varchar(20) DEFAULT NULL,
  `gender2` enum('female','male') DEFAULT NULL,
  `contact2` varchar(100) DEFAULT NULL,
  `name3` varchar(30) DEFAULT NULL,
  `stuId3` varchar(16) DEFAULT '',
  `college3` varchar(50) DEFAULT NULL,
  `class3` varchar(20) DEFAULT NULL,
  `gender3` enum('female','male') DEFAULT NULL,
  `contact3` varchar(100) DEFAULT NULL,
  `notice` tinyint(1) NOT NULL DEFAULT '1',
  `comment` varchar(200) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  `mtime` int(11) NOT NULL,
  `history` text,
  `cid` int(4) DEFAULT NULL,
  `isGirlTeam` tinyint(1) NOT NULL DEFAULT '0',
  `isRookieTeam` tinyint(1) NOT NULL DEFAULT '0',
  `isSpecialTeam` tinyint(1) NOT NULL DEFAULT '0',
  `teamNameChinese` varchar(20) NOT NULL DEFAULT 'NoTeamName',
  `teamNameEnglish` varchar(20) NOT NULL DEFAULT 'NoTeamName',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topic`
--

DROP TABLE IF EXISTS `topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topic` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `pid` int(9) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `atime` int(11) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL DEFAULT '0',
  `mtime` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `view` int(9) NOT NULL DEFAULT '0',
  `threadId` int(9) DEFAULT '0',
  `parentId` int(9) DEFAULT '0',
  `orderNum` int(9) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `uid` int(9) NOT NULL AUTO_INCREMENT COMMENT 'Unique user ID, internal use.',
  `tid` int(9) NOT NULL DEFAULT '0' COMMENT 'refere team id.',
  `name` varchar(35) NOT NULL COMMENT 'unique user login name.',
  `password` varchar(128) NOT NULL COMMENT 'User?s password (hashed).',
  `nick` varchar(255) DEFAULT NULL COMMENT 'nick',
  `realName` varchar(35) DEFAULT NULL,
  `regEmail` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL COMMENT 'User’s e-mail address.',
  `emailVerified` tinyint(1) NOT NULL DEFAULT '0',
  `language` int(5) NOT NULL DEFAULT '0',
  `school` varchar(65) DEFAULT NULL,
  `solved` int(6) NOT NULL DEFAULT '0' COMMENT 'the number of problems user solved',
  `accepted` int(6) NOT NULL DEFAULT '0',
  `submission` int(6) NOT NULL DEFAULT '0' COMMENT 'the number of user submit code',
  `atime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for previous time user accessed the site.',
  `ctime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for when user was created.',
  `mtime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for when user edit its profile.',
  `loginTime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for user last login.',
  `loginIP` varchar(64) DEFAULT NULL,
  `phone` varchar(35) DEFAULT NULL,
  `qq` varchar(15) DEFAULT NULL,
  `blog` varchar(255) DEFAULT NULL,
  `gender` enum('female','male','secret') NOT NULL DEFAULT 'secret',
  `comeFrom` varchar(35) DEFAULT NULL,
  `online` int(9) NOT NULL DEFAULT '0',
  `level` int(5) NOT NULL DEFAULT '1',
  `credit` int(9) NOT NULL DEFAULT '0',
  `shareCode` tinyint(1) NOT NULL DEFAULT '1',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'user avatar path',
  `signature` varchar(255) DEFAULT NULL,
  `checkin` int(11) NOT NULL DEFAULT '0',
  `checkin_times` int(5) NOT NULL DEFAULT '0',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT 'Whether the user is active(1) or blocked(0).',
  `data` blob,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_ext`
--

DROP TABLE IF EXISTS `user_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ext` (
  `uid` int(9) NOT NULL,
  `tid` int(9) NOT NULL DEFAULT '0',
  `realName` varchar(35) DEFAULT NULL,
  `phone` varchar(35) DEFAULT NULL,
  `qq` varchar(15) DEFAULT NULL,
  `blog` varchar(255) DEFAULT NULL,
  `shareCode` tinyint(1) NOT NULL DEFAULT '0',
  `online` int(9) NOT NULL DEFAULT '0',
  `level` int(9) NOT NULL DEFAULT '1',
  `credit` int(9) NOT NULL DEFAULT '0',
  `experience` int(9) NOT NULL DEFAULT '0',
  `checkin` int(11) NOT NULL DEFAULT '0',
  `checkinTimes` tinyint(5) NOT NULL DEFAULT '0',
  `totalCheckin` int(9) NOT NULL DEFAULT '0',
  `lastSendDrift` int(11) NOT NULL DEFAULT '0',
  `sendDriftNum` tinyint(3) NOT NULL DEFAULT '0',
  `lastGetDrift` int(11) NOT NULL DEFAULT '0',
  `getDriftNum` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `rid` int(9) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role_uid_rid_pk` (`uid`,`rid`),
  KEY `rid` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `variable`
--

DROP TABLE IF EXISTS `variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `variable` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `stringValue` varchar(255) DEFAULT NULL,
  `booleanValue` tinyint(1) DEFAULT NULL,
  `intValue` int(11) DEFAULT NULL,
  `textValue` text,
  `type` enum('boolean','int','string','text') NOT NULL DEFAULT 'string',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `web_login`
--

DROP TABLE IF EXISTS `web_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `web_login` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `openId` varchar(64) NOT NULL,
  `uid` int(9) DEFAULT NULL,
  `nick` varchar(64) DEFAULT NULL,
  `avatar` varchar(512) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-24 10:16:44

ALTER TABLE contest AUTO_INCREMENT=1000;

ALTER TABLE contest_solution AUTO_INCREMENT=1000;

ALTER TABLE notice AUTO_INCREMENT=1000;

ALTER TABLE problem AUTO_INCREMENT=1000;

ALTER TABLE solution AUTO_INCREMENT=1000;

ALTER TABLE user AUTO_INCREMENT=1000;
-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: oj
-- ------------------------------------------------------
-- Server version	5.5.49-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `user`
--
-- WHERE:  uid=1000

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1000,0,'root','$2a$10$FYhE.LjD7nmGPJRUaIoeVOupJITPSRCjqJkfLZq/ooOEVEoL0Wjea','root','root','','admin@power.oj',0,1,'',0,0,0,0,0,0,0,'127.0.0.1','','','','secret','',0,1,0,1,NULL,NULL,0,0,1,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_ext`
--
-- WHERE:  uid=1000

LOCK TABLES `user_ext` WRITE;
/*!40000 ALTER TABLE `user_ext` DISABLE KEYS */;
INSERT INTO `user_ext` VALUES (1000,0,NULL,NULL,NULL,NULL,0,0,1,2,2,1410324944,2,1,0,0,0,0);
/*!40000 ALTER TABLE `user_ext` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_role`
--
-- WHERE:  uid=1000

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1000,1,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-24 10:16:44
-- MySQL dump 10.13  Distrib 5.5.49, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: oj
-- ------------------------------------------------------
-- Server version	5.5.49-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,0,'Graph Theory','图论');
INSERT INTO `category` VALUES (2,1,'2-SAT','2-SAT');
INSERT INTO `category` VALUES (3,1,'Articulation/Bridge/Biconnected Component','连通性');
INSERT INTO `category` VALUES (4,1,'Cycles/Topological Sorting/Strongly Connected Component','环/拓扑/强连通');
INSERT INTO `category` VALUES (5,1,'Shortest Path','最短路径');
INSERT INTO `category` VALUES (6,5,'Bellman Ford/SPFA','Bellman Ford/SPFA');
INSERT INTO `category` VALUES (7,5,'Dijkstra/Floyd Warshall','Dijkstra/Floyd Warshall');
INSERT INTO `category` VALUES (8,1,'Euler Trail/Circuit','欧拉路径/欧拉回路');
INSERT INTO `category` VALUES (9,1,'Heavy-Light Decomposition','动态树');
INSERT INTO `category` VALUES (10,1,'Minimum Spanning Tree','最小生成树');
INSERT INTO `category` VALUES (11,1,'Directed Minimum Spanning Tree','有向最小生成树');
INSERT INTO `category` VALUES (12,1,'Stable Marriage Problem','稳定婚姻匹配问题');
INSERT INTO `category` VALUES (13,1,'Trees','树');
INSERT INTO `category` VALUES (14,1,'Flow/Matching','流/匹配');
INSERT INTO `category` VALUES (15,14,'Graph Matching','一般图匹配');
INSERT INTO `category` VALUES (16,15,'Bipartite Matching','2分图匹配');
INSERT INTO `category` VALUES (17,15,'Hopcroft–Karp Bipartite Matching','HK匹配');
INSERT INTO `category` VALUES (18,15,'Weighted Bipartite Matching/Hungarian Algorithm','加权图匹配/匈牙利算法');
INSERT INTO `category` VALUES (19,14,'Flow','流');
INSERT INTO `category` VALUES (20,19,'Max Flow/Min Cut','最大流/最小割');
INSERT INTO `category` VALUES (21,19,'Min Cost Max Flow','最小费用最大流');
INSERT INTO `category` VALUES (22,0,'DFS-like','搜索');
INSERT INTO `category` VALUES (23,22,'Backtracking with Pruning/Branch and Bound','回溯剪枝/分枝界限法\r\n');
INSERT INTO `category` VALUES (24,22,'Basic Recursion','递归');
INSERT INTO `category` VALUES (25,22,'IDA* Search','迭代加深搜索');
INSERT INTO `category` VALUES (26,22,'Parsing/Grammar','语法分析');
INSERT INTO `category` VALUES (27,22,'Breadth First Search/Depth First Search','广度/深度优先搜索');
INSERT INTO `category` VALUES (28,22,'Advanced Search Techniques','高级搜索技术');
INSERT INTO `category` VALUES (29,28,'Binary Search/Bisection','二分搜索');
INSERT INTO `category` VALUES (30,28,'Ternary Search','三叉树搜索');
INSERT INTO `category` VALUES (31,0,'Geometry','几何');
INSERT INTO `category` VALUES (32,32,'Basic Geometry','简单几何');
INSERT INTO `category` VALUES (33,32,'Computational Geometry','计算几何');
INSERT INTO `category` VALUES (34,32,'Convex Hull','凸包');
INSERT INTO `category` VALUES (35,32,'Pick Theorem','皮克定理');
INSERT INTO `category` VALUES (36,0,'Game Theory','博弈论');
INSERT INTO `category` VALUES (37,36,'Green Hackenbush/Colon Principle/Fusion Principle','Green Hackenbush/Colon Principle/Fusion Principle');
INSERT INTO `category` VALUES (38,36,'Nim','Nim');
INSERT INTO `category` VALUES (39,36,'Sprague-Grundy Number','SG值');
INSERT INTO `category` VALUES (40,0,'Matrix','矩阵');
INSERT INTO `category` VALUES (41,41,'Gaussian Elimination','高斯消元');
INSERT INTO `category` VALUES (42,41,'Matrix Exponentiation','矩阵求幂');
INSERT INTO `category` VALUES (43,0,'Data Structures','数据结构');
INSERT INTO `category` VALUES (44,43,'Basic Data Structures','简单数据结构');
INSERT INTO `category` VALUES (45,43,'Binary Indexed Tree','树状数组');
INSERT INTO `category` VALUES (46,43,'Binary Search Tree','二叉搜索树');
INSERT INTO `category` VALUES (47,43,'Hashing','哈希');
INSERT INTO `category` VALUES (48,43,'Orthogonal Range Search','正交范围搜索');
INSERT INTO `category` VALUES (49,43,'Range Minimum Query/Lowest Common Ancestor','RMQ/LCA');
INSERT INTO `category` VALUES (50,43,'Segment Tree/Interval Tree','线段树/区间树');
INSERT INTO `category` VALUES (51,43,'Trie Tree','字典树');
INSERT INTO `category` VALUES (52,43,'Sorting','排序');
INSERT INTO `category` VALUES (53,43,'Disjoint Set','并查集');
INSERT INTO `category` VALUES (54,0,'String','字符串');
INSERT INTO `category` VALUES (55,54,'Aho Corasick','AC自动机');
INSERT INTO `category` VALUES (56,54,'Knuth-Morris-Pratt','KMP匹配');
INSERT INTO `category` VALUES (57,54,'Suffix Array/Suffix Tree','后缀数组/后缀树');
INSERT INTO `category` VALUES (58,0,'Math','数学');
INSERT INTO `category` VALUES (59,58,'Basic Math','基础数学');
INSERT INTO `category` VALUES (60,58,'Big Integer Arithmetic','高精度');
INSERT INTO `category` VALUES (61,58,'Number Theory','数论');
INSERT INTO `category` VALUES (62,61,'Chinese Remainder Theorem','中国同余定理');
INSERT INTO `category` VALUES (64,61,'Inclusion/Exclusion','容斥');
INSERT INTO `category` VALUES (65,61,'Modular Arithmetic','模运算');
INSERT INTO `category` VALUES (66,58,'Combinatorics','组合数学');
INSERT INTO `category` VALUES (67,66,'Group Theory/Burnside lemma','集团理论/伯恩赛德引理');
INSERT INTO `category` VALUES (68,66,'Counting','计数');
INSERT INTO `category` VALUES (69,58,'Probability/Expected Value','概率/期望');
INSERT INTO `category` VALUES (70,0,'Others','其它');
INSERT INTO `category` VALUES (71,71,'Tricky','技巧');
INSERT INTO `category` VALUES (72,71,'Hardest','困难');
INSERT INTO `category` VALUES (73,71,'Unusual','罕见');
INSERT INTO `category` VALUES (74,71,'Brute Force','暴力');
INSERT INTO `category` VALUES (75,71,'Implementation','实现');
INSERT INTO `category` VALUES (76,71,'Constructive Algorithms','构造算法');
INSERT INTO `category` VALUES (77,71,'Two Pointer','');
INSERT INTO `category` VALUES (78,71,'Bitmask','位掩码');
INSERT INTO `category` VALUES (79,71,'Beginner','入门');
INSERT INTO `category` VALUES (80,71,'Discrete Logarithm/Shank Baby-step Giant-step Algorithm','离散对数/');
INSERT INTO `category` VALUES (81,71,'Greedy','贪心');
INSERT INTO `category` VALUES (82,71,'Divide and Conquer','分治');
INSERT INTO `category` VALUES (83,0,'Dynamic Programming','动态规划');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,10);
INSERT INTO `level` VALUES (2,20);
INSERT INTO `level` VALUES (3,40);
INSERT INTO `level` VALUES (4,80);
INSERT INTO `level` VALUES (5,160);
INSERT INTO `level` VALUES (6,320);
INSERT INTO `level` VALUES (7,640);
INSERT INTO `level` VALUES (8,1280);
INSERT INTO `level` VALUES (9,2560);
INSERT INTO `level` VALUES (10,5120);
INSERT INTO `level` VALUES (11,10240);
INSERT INTO `level` VALUES (12,20480);
INSERT INTO `level` VALUES (13,40960);
INSERT INTO `level` VALUES (14,81920);
INSERT INTO `level` VALUES (15,163840);
INSERT INTO `level` VALUES (16,327680);
INSERT INTO `level` VALUES (17,655360);
INSERT INTO `level` VALUES (18,1310720);
INSERT INTO `level` VALUES (19,2621440);
INSERT INTO `level` VALUES (20,5242880);
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'root',1,'*','所有权限',0,1);
INSERT INTO `permission` VALUES (2,'admin',1,'admin','管理',1,1);
INSERT INTO `permission` VALUES (3,'admin',1,'admin:view:actionLog','查看行为日志',2,1);
INSERT INTO `permission` VALUES (4,'admin',1,'admin:delete:actionLog','删除行为日志',2,1);
INSERT INTO `permission` VALUES (5,'admin',1,'admin:view:setting','查看设置',2,1);
INSERT INTO `permission` VALUES (6,'admin',1,'admin:view:content','查看内容管理',2,1);
INSERT INTO `permission` VALUES (7,'admin',1,'admin:view:user','查看用户管理',2,1);
INSERT INTO `permission` VALUES (8,'admin',1,'admin:edit:user','编辑用户管理',2,1);
INSERT INTO `permission` VALUES (9,'admin',1,'admin:view:system','查看系统管理',2,1);
INSERT INTO `permission` VALUES (10,'admin',1,'admin:edit:system','编辑系统管理',2,1);
INSERT INTO `permission` VALUES (11,'admin',1,'admin:configure','配置',2,1);
INSERT INTO `permission` VALUES (12,'admin',1,'admin:add:menu','添加菜单',2,1);
INSERT INTO `permission` VALUES (13,'admin',1,'admin:edit:menu','编辑菜单',2,1);
INSERT INTO `permission` VALUES (14,'admin',1,'admin:delete:menu','删除菜单',2,1);
INSERT INTO `permission` VALUES (15,'admin',1,'admin:add:navigation','添加导航',2,1);
INSERT INTO `permission` VALUES (16,'admin',1,'admin:edit:navigation','编辑导航',2,1);
INSERT INTO `permission` VALUES (17,'admin',1,'admin:delete:navigation','删除导航',2,1);
INSERT INTO `permission` VALUES (18,'admin',1,'admin:upload','上传',2,1);
INSERT INTO `permission` VALUES (19,'admin',1,'admin:download','下载',2,1);
INSERT INTO `permission` VALUES (20,'admin',1,'admin:add:plugin','添加插件',2,1);
INSERT INTO `permission` VALUES (21,'admin',1,'admin:delete:plugin','删除插件',2,1);
INSERT INTO `permission` VALUES (22,'system',1,'system','系统',1,1);
INSERT INTO `permission` VALUES (23,'system',1,'system:viewInfo','查看信息',22,1);
INSERT INTO `permission` VALUES (24,'system',1,'system:viewTask','查看任务',22,1);
INSERT INTO `permission` VALUES (25,'system',1,'system:killTask','杀死任务',22,1);
INSERT INTO `permission` VALUES (26,'system',1,'system:backup','备份',22,1);
INSERT INTO `permission` VALUES (27,'system',1,'system:backup:db','数据库',26,1);
INSERT INTO `permission` VALUES (28,'system',1,'system:restore:db','恢复',22,1);
INSERT INTO `permission` VALUES (29,'system',1,'system:optimize:db','优化',22,1);
INSERT INTO `permission` VALUES (30,'system',1,'system:repair:db','修复',22,1);
INSERT INTO `permission` VALUES (31,'user',1,'user','用户',1,1);
INSERT INTO `permission` VALUES (32,'user',1,'user:view','查看',31,1);
INSERT INTO `permission` VALUES (33,'user',1,'user:view:normal','普通',32,1);
INSERT INTO `permission` VALUES (34,'user',1,'user:view:self','自己',32,1);
INSERT INTO `permission` VALUES (35,'user',1,'user:view:privacy','隐私',32,1);
INSERT INTO `permission` VALUES (36,'user',1,'user:view:share','分享',32,1);
INSERT INTO `permission` VALUES (37,'admin',1,'user:viewLog','查看日志',31,1);
INSERT INTO `permission` VALUES (38,'user',1,'user:viewLog:self','自己的',37,1);
INSERT INTO `permission` VALUES (39,'admin',1,'user:deleteLog','删除日志',31,1);
INSERT INTO `permission` VALUES (40,'user',1,'user:deleteLog:self','自己的',39,1);
INSERT INTO `permission` VALUES (41,'user',1,'user:search','搜索',31,1);
INSERT INTO `permission` VALUES (42,'admin',1,'user:archive','存档',31,1);
INSERT INTO `permission` VALUES (43,'user',1,'user:archive:self','自己',42,1);
INSERT INTO `permission` VALUES (44,'admin',1,'user:add','添加',31,1);
INSERT INTO `permission` VALUES (45,'admin',1,'user:edit','编辑',31,1);
INSERT INTO `permission` VALUES (46,'user',1,'user:edit:self','自己',45,1);
INSERT INTO `permission` VALUES (47,'user',1,'user:edit:nick','昵称',45,1);
INSERT INTO `permission` VALUES (48,'user',1,'user:edit:password','密码',45,1);
INSERT INTO `permission` VALUES (49,'user',1,'user:upload:avatar','上传头像',31,1);
INSERT INTO `permission` VALUES (50,'admin',1,'user:delete','删除',31,1);
INSERT INTO `permission` VALUES (51,'admin',1,'user:switch','切换',31,1);
INSERT INTO `permission` VALUES (52,'admin',1,'user:forbid','禁用',31,1);
INSERT INTO `permission` VALUES (53,'admin',1,'user:resume','启用',31,1);
INSERT INTO `permission` VALUES (54,'admin',1,'user:build','重建统计信息',31,1);
INSERT INTO `permission` VALUES (55,'admin',1,'user:buildRank','重建排名',31,1);
INSERT INTO `permission` VALUES (56,'group',1,'group','用户组',1,1);
INSERT INTO `permission` VALUES (57,'group',1,'group:view','查看',56,1);
INSERT INTO `permission` VALUES (58,'admin',1,'group:add','添加',56,1);
INSERT INTO `permission` VALUES (59,'admin',1,'group:addUser','添加用户',56,1);
INSERT INTO `permission` VALUES (60,'admin',1,'group:removeUser','移除用户',56,1);
INSERT INTO `permission` VALUES (61,'admin',1,'group:edit','编辑',56,1);
INSERT INTO `permission` VALUES (62,'admin',1,'group:delete','删除',56,1);
INSERT INTO `permission` VALUES (63,'admin',1,'group:forbid','禁用',56,1);
INSERT INTO `permission` VALUES (64,'admin',1,'group:resume','启用',56,1);
INSERT INTO `permission` VALUES (65,'admin',1,'permission','权限',1,1);
INSERT INTO `permission` VALUES (66,'admin',1,'permission:add','添加',65,1);
INSERT INTO `permission` VALUES (67,'admin',1,'permission:view','查看',65,1);
INSERT INTO `permission` VALUES (68,'admin',1,'permission:edit','编辑',65,1);
INSERT INTO `permission` VALUES (69,'admin',1,'permission:delete','删除',65,1);
INSERT INTO `permission` VALUES (70,'admin',1,'permission:forbid','禁用',65,1);
INSERT INTO `permission` VALUES (71,'admin',1,'permission:resume','启用',65,1);
INSERT INTO `permission` VALUES (72,'problem',1,'problem','题目',1,1);
INSERT INTO `permission` VALUES (73,'',1,'problem:view','查看',72,1);
INSERT INTO `permission` VALUES (74,'problem',1,'problem:view:normal','普通',73,1);
INSERT INTO `permission` VALUES (75,'problem',1,'problem:status','状态',72,1);
INSERT INTO `permission` VALUES (76,'problem',1,'problem:discuss','讨论',72,1);
INSERT INTO `permission` VALUES (77,'problem',1,'problem:submit','提交',72,1);
INSERT INTO `permission` VALUES (78,'problem',1,'problem:search','搜索',72,1);
INSERT INTO `permission` VALUES (79,'admin',1,'problem:add','添加',72,1);
INSERT INTO `permission` VALUES (80,'admin',1,'problem:edit','编辑',72,1);
INSERT INTO `permission` VALUES (81,'admin',1,'problem:edit:title','标题',80,1);
INSERT INTO `permission` VALUES (82,'admin',1,'problem:edit:hint','提示',80,1);
INSERT INTO `permission` VALUES (83,'admin',1,'problem:edit:source','来源',80,1);
INSERT INTO `permission` VALUES (84,'',1,'problem:addTag','添加标签',72,1);
INSERT INTO `permission` VALUES (85,'problem',1,'problem:viewTag','查看标签',72,1);
INSERT INTO `permission` VALUES (86,'admin',1,'problem:editTag','编辑标签',72,1);
INSERT INTO `permission` VALUES (87,'admin',1,'problem:deleteTag','删除标签',72,1);
INSERT INTO `permission` VALUES (88,'admin',1,'problem:delete','删除',72,1);
INSERT INTO `permission` VALUES (89,'admin',1,'problem:forbid','禁用',72,1);
INSERT INTO `permission` VALUES (90,'admin',1,'problem:resume','启用',72,1);
INSERT INTO `permission` VALUES (91,'admin',1,'problem:rejudge','重判',72,1);
INSERT INTO `permission` VALUES (92,'admin',1,'problem:rebuild','重建统计信息',72,1);
INSERT INTO `permission` VALUES (93,'admin',1,'problem:import','导入',72,1);
INSERT INTO `permission` VALUES (94,'admin',1,'problem:export','导出',72,1);
INSERT INTO `permission` VALUES (95,'admin',1,'problem:upload','上传',72,1);
INSERT INTO `permission` VALUES (96,'admin',1,'problem:upload:image','图片',95,1);
INSERT INTO `permission` VALUES (97,'admin',1,'problem:upload:data','数据',95,1);
INSERT INTO `permission` VALUES (98,'admin',1,'problem:viewData','查看数据',72,1);
INSERT INTO `permission` VALUES (99,'admin',1,'problem:editData','编辑数据',72,1);
INSERT INTO `permission` VALUES (100,'admin',1,'problem:deleteData','删除数据',72,1);
INSERT INTO `permission` VALUES (101,'contest',1,'contest','比赛',1,1);
INSERT INTO `permission` VALUES (102,'contest',1,'contest:view','查看',101,1);
INSERT INTO `permission` VALUES (103,'contest',1,'contest:view:normal','普通',102,1);
INSERT INTO `permission` VALUES (104,'contest',1,'contest:view:public','公开',102,1);
INSERT INTO `permission` VALUES (105,'contest',1,'contest:view:private','私有',102,1);
INSERT INTO `permission` VALUES (106,'contest',1,'contest:view:password','密码访问',102,1);
INSERT INTO `permission` VALUES (107,'contest',1,'contest:viewCode','查看代码',101,1);
INSERT INTO `permission` VALUES (108,'contest',1,'contest:viewCode:self','自己',101,1);
INSERT INTO `permission` VALUES (109,'contest',1,'contest:submit','提交',101,1);
INSERT INTO `permission` VALUES (110,'contest',1,'contest:discuss','讨论',101,1);
INSERT INTO `permission` VALUES (111,'admin',1,'contest:add','添加',101,1);
INSERT INTO `permission` VALUES (112,'admin',1,'contest:edit','编辑',101,1);
INSERT INTO `permission` VALUES (113,'admin',1,'contest:edit:title','标题',112,1);
INSERT INTO `permission` VALUES (114,'admin',1,'contest:edit:time','时间',112,1);
INSERT INTO `permission` VALUES (115,'admin',1,'contest:edit:type','类型',112,1);
INSERT INTO `permission` VALUES (116,'admin',1,'contest:edit:description','描述',112,1);
INSERT INTO `permission` VALUES (117,'admin',1,'contest:freezeBoard','封榜',101,1);
INSERT INTO `permission` VALUES (118,'admin',1,'contest:notify','通知',101,1);
INSERT INTO `permission` VALUES (119,'admin',1,'contest:addProblem','添加题目',101,1);
INSERT INTO `permission` VALUES (120,'contest',1,'contest:viewProblem','查看题目',101,1);
INSERT INTO `permission` VALUES (121,'admin',1,'contest:editProblem','编辑题目',101,1);
INSERT INTO `permission` VALUES (122,'admin',1,'contest:removeProblem','移除题目',101,1);
INSERT INTO `permission` VALUES (123,'admin',1,'contest:addUser','添加用户',101,1);
INSERT INTO `permission` VALUES (124,'admin',1,'contest:removeUser','移除用户',101,1);
INSERT INTO `permission` VALUES (125,'admin',1,'contest:addGroup','添加用户组',101,1);
INSERT INTO `permission` VALUES (126,'admin',1,'contest:removeGroup','移除用户组',101,1);
INSERT INTO `permission` VALUES (127,'admin',1,'contest:copy','复制',101,1);
INSERT INTO `permission` VALUES (128,'admin',1,'contest:import','导入',101,1);
INSERT INTO `permission` VALUES (129,'admin',1,'contest:export','导出',101,1);
INSERT INTO `permission` VALUES (130,'admin',1,'contest:rejudge','重判',101,1);
INSERT INTO `permission` VALUES (131,'admin',1,'contest:buildRank','重建排名',101,1);
INSERT INTO `permission` VALUES (132,'code',1,'code','代码',1,1);
INSERT INTO `permission` VALUES (133,'admin',1,'code:view','查看',132,1);
INSERT INTO `permission` VALUES (134,'code',1,'code:view:self','自己',133,1);
INSERT INTO `permission` VALUES (135,'code',1,'code:view:share','分享',133,1);
INSERT INTO `permission` VALUES (136,'code',1,'code:search','搜索',132,1);
INSERT INTO `permission` VALUES (137,'code',1,'code:add','添加',132,1);
INSERT INTO `permission` VALUES (138,'code',1,'code:edit','编辑',132,1);
INSERT INTO `permission` VALUES (139,'code',1,'code:edit:self','自己',138,1);
INSERT INTO `permission` VALUES (140,'code',1,'code:judge','评测',132,1);
INSERT INTO `permission` VALUES (141,'admin',1,'code:rejudge','重判',132,1);
INSERT INTO `permission` VALUES (142,'mail',1,'mail','邮件',0,1);
INSERT INTO `permission` VALUES (143,'',1,'mail:view','查看',142,1);
INSERT INTO `permission` VALUES (144,'mail',1,'mail:view:self','自己',143,1);
INSERT INTO `permission` VALUES (145,'mail',1,'mail:send','发送',142,1);
INSERT INTO `permission` VALUES (146,'admin',1,'mail:massSend','群发',142,1);
INSERT INTO `permission` VALUES (147,'',1,'mail:delete','删除',142,1);
INSERT INTO `permission` VALUES (148,'mail',1,'mail:delete:self','自己',147,1);
INSERT INTO `permission` VALUES (149,'mail',1,'mail:clean:self','清空',142,1);
INSERT INTO `permission` VALUES (150,'comment',1,'comment','评论',1,1);
INSERT INTO `permission` VALUES (151,'',1,'comment:view','查看',150,1);
INSERT INTO `permission` VALUES (152,'comment',1,'comment:view:normal','普通',151,1);
INSERT INTO `permission` VALUES (153,'comment',1,'comment:add','添加',150,1);
INSERT INTO `permission` VALUES (154,'admin',1,'comment:edit','编辑',150,1);
INSERT INTO `permission` VALUES (155,'comment',1,'comment:edit:self','自己',154,1);
INSERT INTO `permission` VALUES (156,'admin',1,'comment:delete','删除',150,1);
INSERT INTO `permission` VALUES (157,'comment',1,'comment:delete:self','自己',156,1);
INSERT INTO `permission` VALUES (158,'admin',1,'comment:audit','审核',150,1);
INSERT INTO `permission` VALUES (159,'admin',1,'comment:top','置顶',150,1);
INSERT INTO `permission` VALUES (160,'admin',1,'comment:forbid','禁用',150,1);
INSERT INTO `permission` VALUES (161,'admin',1,'comment:resume','启用',150,1);
INSERT INTO `permission` VALUES (162,'announcement',1,'announcement','公告',1,1);
INSERT INTO `permission` VALUES (163,'',1,'announcement:view','查看',162,1);
INSERT INTO `permission` VALUES (164,'announcement',1,'announcement:view:normal','普通',163,1);
INSERT INTO `permission` VALUES (165,'',1,'announcement:list','列表',162,1);
INSERT INTO `permission` VALUES (166,'announcement',1,'announcement:list:normal','普通',165,1);
INSERT INTO `permission` VALUES (167,'admin',1,'announcement:add','添加',162,1);
INSERT INTO `permission` VALUES (168,'admin',1,'announcement:edit','编辑',162,1);
INSERT INTO `permission` VALUES (169,'admin',1,'announcement:edit:title','标题',168,1);
INSERT INTO `permission` VALUES (170,'admin',1,'announcement:edit:time','时间',168,1);
INSERT INTO `permission` VALUES (171,'admin',1,'announcement:delete','删除',168,1);
INSERT INTO `permission` VALUES (172,'admin',1,'announcement:forbid','禁用',162,1);
INSERT INTO `permission` VALUES (173,'admin',1,'announcement:resume','启用',162,1);
INSERT INTO `permission` VALUES (174,'user',1,'user:sp:nick','特殊昵称',45,1);
INSERT INTO `permission` VALUES (175,'user',1,'teacher','教师权限',0,1);
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_language`
--

LOCK TABLES `program_language` WRITE;
/*!40000 ALTER TABLE `program_language` DISABLE KEYS */;
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('1', 'GCC11', 'C语言推荐', '0', '996', '1', '1', 'c', 'exe', '1', 'C:\\JudgeOnline\\bin\\gcc\\bin\\gcc.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('2', 'G++11', 'C++推荐', '0', '996', '1', '1', 'cc', 'exe', '1', 'C:\\JudgeOnline\\bin\\gcc\\bin\\g++.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('3', 'Pascal', '', '0', '1000', '1', '1', 'pas', 'exe', '0', 'C:\\JudgeOnline\\bin\\fpc\\fpc.exe -Sg -dONLINE_JUDGE %PATH%%NAME%.%EXT%', 'pascal', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('4', 'Java', '', '0', '8000', '3', '3', 'java', 'class', '2', 'C:\\JudgeOnline\\bin\\Java\\Java.bat %PATH%', 'java', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('5', 'Python2.7', '', '0', '7000', '4', '3', 'py', 'exe', '1', 'C:\\JudgeOnline\\bin\\Python\\Python.bat %PATH% %NAME% %EXT%', 'python', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('6', 'GCC99', '', '0', '996', '1', '1', 'c', 'exe', '1', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('7', 'G++98', '', '0', '996', '1', '1', 'cc', 'exe', '0', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('8', 'G++14', ' ', '0', '996', '1', '1', 'cc', 'exe', '1', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('9', 'G++17', ' ', '0', '996', '1', '1', 'cc', 'exe', '0', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('10', 'Python3', ' ', '0', '7000', '4', '3', 'py', 'exe', '1', 'C:\\', 'python', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('11', 'Kotlin', '', '0', '8000', '3', '3', 'kt', 'class', '0', '', 'java', '0', '1');
/*!40000 ALTER TABLE `program_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'root','root',1);
INSERT INTO `role` VALUES (2,'admin','administrator',1);
INSERT INTO `role` VALUES (3,'member','team member',1);
INSERT INTO `role` VALUES (10,'user','user',1);
INSERT INTO `oj`.`role` (`id`, `name`, `description`, `status`) VALUES ('4', 'teacher', 'teacher', '1');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` VALUES (1,1,1);
INSERT INTO `role_permission` VALUES (2,2,2);
INSERT INTO `role_permission` VALUES (3,2,31);
INSERT INTO `role_permission` VALUES (4,2,56);
INSERT INTO `role_permission` VALUES (5,2,72);
INSERT INTO `role_permission` VALUES (6,2,101);
INSERT INTO `role_permission` VALUES (7,2,132);
INSERT INTO `role_permission` VALUES (8,2,142);
INSERT INTO `role_permission` VALUES (9,2,150);
INSERT INTO `role_permission` VALUES (10,2,162);
INSERT INTO `role_permission` VALUES (11,3,77);
INSERT INTO `role_permission` VALUES (12,3,174);
INSERT INTO `role_permission` VALUES (13,3,84);
INSERT INTO `role_permission` VALUES (14,10,77);
INSERT INTO `oj`.`role_permission` (`id`, `rid`, `pid`) VALUES ('15', '4', '77');
INSERT INTO `oj`.`role_permission` (`id`, `rid`, `pid`) VALUES ('16', '4', '175');
insert into role_permission values(17,4,119);
insert into role_permission values(18,4,122);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `variable`
--

LOCK TABLES `variable` WRITE;
/*!40000 ALTER TABLE `variable` DISABLE KEYS */;
INSERT INTO `variable` VALUES (1,'workPath','/home/judge/temp',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (2,'dataPath','/home/judge/data',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (3,'runShell','C:\\power\\oj\\JudgeOnline\\bin\\run.exe',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (4,'compileShell','C:\\power\\oj\\JudgeOnline\\bin\\com.exe',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (5,'adminName','root',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (6,'adminMail','root@localhost.com',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (7,'debugFile','debug.log',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (8,'errorFile','error.log',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (9,'deleteTmpFile','1',1,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (10,'debug','0',0,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (11,'enableLogin','1',1,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (12,'enableMail','1',1,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (13,'enableSource','1',1,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (14,'enableArchive','1',1,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (15,'openid_qq','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (16,'openkey_qq','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (17,'redirect_qq','http://power-oj.com/api/oauth/qq/callback',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (18,'enable_qq_login','0',0,NULL,NULL,'boolean',NULL);
INSERT INTO `variable` VALUES (19,'openid_sina','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (20,'openkey_sina','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (21,'redirect_sina','http://power-oj.com/api/oauth/sina/callback',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (22,'adminEmail','swust_acm@163.com',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (23,'emailServer','smtp.163.com',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (24,'emailUser','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (25,'emailPass','',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (26,'version','20140221',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (27,'siteTitle','Power OJ',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (28,'uploadPath','/var/www/upload',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (29,'downloadPath','/var/www/download/',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (30,'userAvatarPath','/var/www/upload/image/user/',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (31,'problemImagePath','/var/www/upload/image/problem/',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (32,'judgeHost','127.0.0.1',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (33,'judgePort','55555',NULL,55555,NULL,'int',NULL);
INSERT INTO `variable` VALUES (34,'judgeSecurity','PowerJudgeV1.1',NULL,NULL,NULL,'string',NULL);
insert into variable (name,stringValue) values('astylePath','/usr/local/bin/astyle');
/*!40000 ALTER TABLE `variable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-24 10:16:44

-- ----------------------------
-- Table structure for wxnews
-- ----------------------------
DROP TABLE IF EXISTS `wxnews`;
CREATE TABLE `wxnews` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `digest` varchar(255) DEFAULT NULL,
  `content` text,
  `cover_pic` varchar(255) DEFAULT NULL,
  `source_url` varchar(255) DEFAULT NULL,
  `news_type` int(11) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wxnews_picture
-- ----------------------------
DROP TABLE IF EXISTS `wxnews_picture`;
CREATE TABLE `wxnews_picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `pic_name` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wxquestion
-- ----------------------------
DROP TABLE IF EXISTS `wxquestion`;
CREATE TABLE `wxquestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) DEFAULT '',
  `answer` varchar(1000) DEFAULT '',
  `question_time` varchar(50) DEFAULT '',
  `answer_time` varchar(50) DEFAULT '',
  `answer_author` varchar(255) DEFAULT NULL,
  `respond` int(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wxrookie
-- ---------------------------
DROP TABLE IF EXISTS `wxrookie`;
CREATE TABLE `wxrookie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `StuId` varchar(20) NOT NULL,
  `academy` varchar(50) DEFAULT NULL,
  `Class` varchar(50) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `AdmissionYear` int(11) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `qq` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `join` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*
Add For WeiXin
*/
-- ----------------------------
-- Table structure for cprogram_password
-- ----------------------------
DROP TABLE IF EXISTS `cprogram_password`;
CREATE TABLE `cprogram_password` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `uid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for cprogram_user_info
-- ----------------------------
DROP TABLE IF EXISTS `cprogram_user_info`;
CREATE TABLE `cprogram_user_info` (
  `uid` int(11) NOT NULL,
  `classes` varchar(255) NOT NULL,
  `stuid` varchar(255) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  `class_week` int(11) DEFAULT NULL,
  `class_lecture` int(11) DEFAULT NULL,
  `ctime` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  `score1` int(11) DEFAULT '0',
  `score2` int(11) DEFAULT '0',
  `accepted` int(11) DEFAULT '0',
  `submited` int(11) DEFAULT '0',
  `ctime` int(11) NOT NULL DEFAULT '0',
  `week` int(11) NOT NULL DEFAULT '0',
  `lecture` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cprogram_commit`;
CREATE TABLE `cprogram_commit` (
  `id` int(11) NOT NULL,
  `cid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `commit` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`cid`,`uid`,`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cprogram_commit`;
CREATE TABLE `cprogram_commit` (
  `id` int(11) NOT NULL,
  `cid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `commit` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`cid`,`uid`,`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cprogram_experiment_report`;
CREATE TABLE `cprogram_experiment_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `week` int(11) DEFAULT NULL,
  `lecture` int(11) DEFAULT NULL,
  `commit` text,
  `machine` int(11) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`cid`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cprogram_info`;
CREATE TABLE `cprogram_info` (
  `cid` int(11) NOT NULL,
  `type` enum('HOMEWORK','EXPERIMENT','EXPERIMENT_EXAM','COURSE_EXAM') DEFAULT NULL,
  `commit` text,
  `week` int(11) DEFAULT NULL,
  `lecture` int(11) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
