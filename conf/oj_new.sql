-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: oj_new
-- ------------------------------------------------------
-- Server version 5.5.35-0+wheezy1

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
  `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `B_SolvedTime` int(11) DEFAULT '0',
  `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `C_SolvedTime` int(11) DEFAULT '0',
  `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `D_SolvedTime` int(11) DEFAULT '0',
  `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `E_SolvedTime` int(11) DEFAULT '0',
  `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `F_SolvedTime` int(11) DEFAULT '0',
  `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `G_SolvedTime` int(11) DEFAULT '0',
  `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `H_SolvedTime` int(11) DEFAULT '0',
  `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `I_SolvedTime` int(11) DEFAULT '0',
  `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `J_SolvedTime` int(11) DEFAULT '0',
  `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `K_SolvedTime` int(11) DEFAULT '0',
  `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `L_SolvedTime` int(11) DEFAULT '0',
  `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `M_SolvedTime` int(11) DEFAULT '0',
  `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `N_SolvedTime` int(11) DEFAULT '0',
  `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `O_SolvedTime` int(11) DEFAULT '0',
  `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `P_SolvedTime` int(11) DEFAULT '0',
  `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Q_SolvedTime` int(11) DEFAULT '0',
  `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `R_SolvedTime` int(11) DEFAULT '0',
  `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `S_SolvedTime` int(11) DEFAULT '0',
  `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `T_SolvedTime` int(11) DEFAULT '0',
  `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `U_SolvedTime` int(11) DEFAULT '0',
  `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `V_SolvedTime` int(11) DEFAULT '0',
  `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `W_SolvedTime` int(11) DEFAULT '0',
  `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `X_SolvedTime` int(11) DEFAULT '0',
  `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Y_SolvedTime` int(11) DEFAULT '0',
  `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Z_SolvedTime` int(11) DEFAULT '0',
  `Z_WrongNum` tinyint(5) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;
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
  `pass` varchar(255) DEFAULT NULL,
  `freeze` tinyint(1) NOT NULL DEFAULT '0',
  `atime` int(11) DEFAULT NULL,
  `ctime` int(11) DEFAULT NULL,
  `mtime` int(11) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cid`),
  KEY `search` (`title`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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
  `cid` int(9) NOT NULL,
  `pid` int(9) NOT NULL,
  `title` char(255) NOT NULL,
  `num` tinyint(5) NOT NULL DEFAULT '0',
  `accepted` int(5) NOT NULL DEFAULT '0',
  `submission` int(5) NOT NULL DEFAULT '0',
  `firstBloodUid` int(9) NOT NULL DEFAULT '0' COMMENT 'first user(uid) solved this problem',
  `firstBloodTime` int(9) NOT NULL DEFAULT '-1' COMMENT 'first time(minutes) solved this problem',
  PRIMARY KEY (`id`)
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
  `error` text,
  `source` text NOT NULL,
  `codeLen` int(9) NOT NULL DEFAULT '0',
  `systemError` text,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
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
  `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `B_SolvedTime` int(11) DEFAULT '0',
  `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `C_SolvedTime` int(11) DEFAULT '0',
  `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `D_SolvedTime` int(11) DEFAULT '0',
  `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `E_SolvedTime` int(11) DEFAULT '0',
  `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `F_SolvedTime` int(11) DEFAULT '0',
  `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `G_SolvedTime` int(11) DEFAULT '0',
  `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `H_SolvedTime` int(11) DEFAULT '0',
  `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `I_SolvedTime` int(11) DEFAULT '0',
  `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `J_SolvedTime` int(11) DEFAULT '0',
  `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `K_SolvedTime` int(11) DEFAULT '0',
  `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `L_SolvedTime` int(11) DEFAULT '0',
  `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `M_SolvedTime` int(11) DEFAULT '0',
  `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `N_SolvedTime` int(11) DEFAULT '0',
  `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `O_SolvedTime` int(11) DEFAULT '0',
  `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `P_SolvedTime` int(11) DEFAULT '0',
  `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Q_SolvedTime` int(11) DEFAULT '0',
  `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `R_SolvedTime` int(11) DEFAULT '0',
  `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `S_SolvedTime` int(11) DEFAULT '0',
  `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `T_SolvedTime` int(11) DEFAULT '0',
  `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `U_SolvedTime` int(11) DEFAULT '0',
  `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `V_SolvedTime` int(11) DEFAULT '0',
  `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `W_SolvedTime` int(11) DEFAULT '0',
  `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `X_SolvedTime` int(11) DEFAULT '0',
  `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Y_SolvedTime` int(11) DEFAULT '0',
  `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
  `Z_SolvedTime` int(11) DEFAULT '0',
  `Z_WrongNum` tinyint(5) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
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
  `ipAddress` varchar(45) NOT NULL DEFAULT '0',
  `userAgent` varchar(255) DEFAULT '',
  `ctime` int(11) NOT NULL DEFAULT '0' COMMENT 'Session create time.',
  `lastActivity` int(11) NOT NULL DEFAULT '0',
  `sessionExpires` int(11) NOT NULL,
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
  `error` text,
  `source` text NOT NULL,
  `codeLen` int(9) NOT NULL DEFAULT '0',
  `systemError` text,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;
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
  `stuId1` varchar(8) NOT NULL,
  `college1` varchar(50) NOT NULL,
  `class1` varchar(20) NOT NULL,
  `gender1` enum('female','male') NOT NULL,
  `contact1` varchar(100) NOT NULL,
  `name2` varchar(30) DEFAULT NULL,
  `stuId2` varchar(8) DEFAULT NULL,
  `college2` varchar(50) DEFAULT NULL,
  `class2` varchar(20) DEFAULT NULL,
  `gender2` enum('female','male') DEFAULT NULL,
  `contact2` varchar(100) DEFAULT NULL,
  `name3` varchar(30) DEFAULT NULL,
  `stuId3` varchar(8) DEFAULT NULL,
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
  `year` int(4) DEFAULT NULL,
  `isGirlTeam` tinyint(1) NOT NULL DEFAULT '0',
  `isRookieTeam` tinyint(1) NOT NULL DEFAULT '0',
  `isSpecialTeam` tinyint(1) NOT NULL DEFAULT '0',
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
  `password` varchar(128) NOT NULL COMMENT 'User password (hashed).',
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
  `shareCode` tinyint(1) NOT NULL DEFAULT '0',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'user avatar path',
  `signature` varchar(255) DEFAULT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT 'Whether the user is active(1) or blocked(0).',
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;
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
  KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `variable`
--

DROP TABLE IF EXISTS `variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `variable` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `cid` int(11) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `stringValue` varchar(255) DEFAULT NULL,
  `booleanValue` tinyint(1) DEFAULT NULL,
  `intValue` int(11) DEFAULT NULL,
  `textValue` text,
  `type` enum('boolean','int','string','text') NOT NULL DEFAULT 'string',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
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
  `avatar` varchar(255) DEFAULT NULL,
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

-- Dump completed on 2014-03-20 11:58:18
