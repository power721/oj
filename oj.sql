/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50159
Source Host           : localhost:3306
Source Database       : oj_setup

Target Server Type    : MYSQL
Target Server Version : 50159
File Encoding         : 65001

Date: 2013-12-13 20:26:35
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `announce`
-- ----------------------------
DROP TABLE IF EXISTS `announce`;
CREATE TABLE `announce` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`title`  varchar(355) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`start_time`  datetime NULL DEFAULT NULL ,
`end_time`  datetime NULL DEFAULT NULL ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`atime`  int(11) NULL DEFAULT NULL ,
`ctime`  int(11) NULL DEFAULT NULL ,
`mtime`  int(11) NULL DEFAULT NULL ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1000

;

-- ----------------------------
-- Records of announce
-- ----------------------------

-- ----------------------------
-- Table structure for `board`
-- ----------------------------
DROP TABLE IF EXISTS `board`;
CREATE TABLE `board` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`cid` int(9) NOT NULL DEFAULT 0,
`uid`  int(9) NOT NULL ,
`accepts`  int(5) NOT NULL DEFAULT 0 ,
`penalty`  int(11) NOT NULL DEFAULT 0 ,
`A_time`  int(11) NULL DEFAULT 0 ,
`A_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`B_time`  int(11) NULL DEFAULT 0 ,
`B_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`C_time`  int(11) NULL DEFAULT 0 ,
`C_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`D_time`  int(11) NULL DEFAULT 0 ,
`D_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`E_time`  int(11) NULL DEFAULT 0 ,
`E_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`F_time`  int(11) NULL DEFAULT 0 ,
`F_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`G_time`  int(11) NULL DEFAULT 0 ,
`G_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`H_time`  int(11) NULL DEFAULT 0 ,
`H_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`I_time`  int(11) NULL DEFAULT 0 ,
`I_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`J_time`  int(11) NULL DEFAULT 0 ,
`J_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`K_time`  int(11) NULL DEFAULT 0 ,
`K_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`L_time`  int(11) NULL DEFAULT 0 ,
`L_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`M_time`  int(11) NULL DEFAULT 0 ,
`M_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`N_time`  int(11) NULL DEFAULT 0 ,
`N_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`O_time`  int(11) NULL DEFAULT 0 ,
`O_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`P_time`  int(11) NULL DEFAULT 0 ,
`P_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Q_time`  int(11) NULL DEFAULT 0 ,
`Q_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`R_time`  int(11) NULL DEFAULT 0 ,
`R_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`S_time`  int(11) NULL DEFAULT 0 ,
`S_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`T_time`  int(11) NULL DEFAULT 0 ,
`T_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`U_time`  int(11) NULL DEFAULT 0 ,
`U_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`V_time`  int(11) NULL DEFAULT 0 ,
`V_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`W_time`  int(11) NULL DEFAULT 0 ,
`W_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`X_time`  int(11) NULL DEFAULT 0 ,
`X_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Y_time`  int(11) NULL DEFAULT 0 ,
`Y_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Z_time`  int(11) NULL DEFAULT 0 ,
`Z_WrongSubmits`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of board
-- ----------------------------

-- ----------------------------
-- Table structure for `contest`
-- ----------------------------
DROP TABLE IF EXISTS `contest`;
CREATE TABLE `contest` (
`cid`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`start_time`  int(11) NULL DEFAULT 0 ,
`end_time`  int(11) NULL DEFAULT 0 ,
`description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`type`  tinyint(4) NOT NULL DEFAULT 0 ,
`pass`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`freeze`  tinyint(4) NOT NULL DEFAULT 0 ,
`atime`  int(11) NULL DEFAULT NULL ,
`ctime`  int(11) NULL DEFAULT NULL ,
`mtime`  int(11) NULL DEFAULT NULL ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`cid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1000

;

-- ----------------------------
-- Records of contest
-- ----------------------------

-- ----------------------------
-- Table structure for `contest_problem`
-- ----------------------------
DROP TABLE IF EXISTS `contest_problem`;
CREATE TABLE `contest_problem` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`cid`  int(9) NOT NULL DEFAULT 0 ,
`pid`  int(9) NOT NULL DEFAULT 0 ,
`title`  char(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`num`  int(5) NOT NULL DEFAULT 0 ,
`accept` int(9) NOT NULL DEFAULT '0',
`submit` int(9) NOT NULL DEFAULT '0',
`first_blood` int(9) NOT NULL DEFAULT '0' COMMENT 'first user(uid) solved this problem',
`first_blood_time` int(9) NOT NULL DEFAULT '-1' COMMENT 'first time(minutes) solved this problem',
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of contest_problem
-- ----------------------------

-- ----------------------------
-- Table structure for `freeze_board`
-- ----------------------------
DROP TABLE IF EXISTS `freeze_board`;
CREATE TABLE `freeze_board` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`cid` int(9) NOT NULL DEFAULT 0,
`uid`  int(9) NOT NULL ,
`accepts`  int(5) NOT NULL DEFAULT 0 ,
`penalty`  int(11) NOT NULL DEFAULT 0 ,
`A_time`  int(11) NULL DEFAULT 0 ,
`A_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`B_time`  int(11) NULL DEFAULT 0 ,
`B_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`C_time`  int(11) NULL DEFAULT 0 ,
`C_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`D_time`  int(11) NULL DEFAULT 0 ,
`D_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`E_time`  int(11) NULL DEFAULT 0 ,
`E_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`F_time`  int(11) NULL DEFAULT 0 ,
`F_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`G_time`  int(11) NULL DEFAULT 0 ,
`G_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`H_time`  int(11) NULL DEFAULT 0 ,
`H_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`I_time`  int(11) NULL DEFAULT 0 ,
`I_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`J_time`  int(11) NULL DEFAULT 0 ,
`J_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`K_time`  int(11) NULL DEFAULT 0 ,
`K_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`L_time`  int(11) NULL DEFAULT 0 ,
`L_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`M_time`  int(11) NULL DEFAULT 0 ,
`M_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`N_time`  int(11) NULL DEFAULT 0 ,
`N_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`O_time`  int(11) NULL DEFAULT 0 ,
`O_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`P_time`  int(11) NULL DEFAULT 0 ,
`P_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Q_time`  int(11) NULL DEFAULT 0 ,
`Q_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`R_time`  int(11) NULL DEFAULT 0 ,
`R_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`S_time`  int(11) NULL DEFAULT 0 ,
`S_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`T_time`  int(11) NULL DEFAULT 0 ,
`T_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`U_time`  int(11) NULL DEFAULT 0 ,
`U_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`V_time`  int(11) NULL DEFAULT 0 ,
`V_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`W_time`  int(11) NULL DEFAULT 0 ,
`W_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`X_time`  int(11) NULL DEFAULT 0 ,
`X_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Y_time`  int(11) NULL DEFAULT 0 ,
`Y_WrongSubmits`  int(11) NULL DEFAULT 0 ,
`Z_time`  int(11) NULL DEFAULT 0 ,
`Z_WrongSubmits`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of freeze_board
-- ----------------------------

-- ----------------------------
-- Table structure for `level`
-- ----------------------------
DROP TABLE IF EXISTS `level`;
CREATE TABLE `level` (
  `level` int(5) NOT NULL AUTO_INCREMENT,
  `exp` int(9) NOT NULL,
  PRIMARY KEY (`level`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of level
-- ----------------------------
INSERT INTO `level` VALUES ('1', '10');
INSERT INTO `level` VALUES ('2', '20');
INSERT INTO `level` VALUES ('3', '40');
INSERT INTO `level` VALUES ('4', '80');
INSERT INTO `level` VALUES ('5', '160');
INSERT INTO `level` VALUES ('6', '320');
INSERT INTO `level` VALUES ('7', '640');
INSERT INTO `level` VALUES ('8', '1280');
INSERT INTO `level` VALUES ('9', '2560');
INSERT INTO `level` VALUES ('10', '5120');
INSERT INTO `level` VALUES ('11', '10240');
INSERT INTO `level` VALUES ('12', '20480');
INSERT INTO `level` VALUES ('13', '40960');
INSERT INTO `level` VALUES ('14', '81920');
INSERT INTO `level` VALUES ('15', '163840');
INSERT INTO `level` VALUES ('16', '327680');
INSERT INTO `level` VALUES ('17', '655360');
INSERT INTO `level` VALUES ('18', '1310720');
INSERT INTO `level` VALUES ('19', '2621440');
INSERT INTO `level` VALUES ('20', '5242880');

-- ----------------------------
-- Table structure for `loginlog`
-- ----------------------------
DROP TABLE IF EXISTS `loginlog`;
CREATE TABLE `loginlog` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL DEFAULT 0 ,
`name` varchar(35) DEFAULT NULL ,
`password`  blob NULL DEFAULT NULL ,
`ip`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ctime`  int(11) NOT NULL ,
`success`  tinyint(1) NOT NULL DEFAULT 0 ,
`info`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of loginlog
-- ----------------------------

-- ----------------------------
-- Table structure for `mail`
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `mid` int(9) NOT NULL COMMENT 'mail id',
  `user` int(9) NOT NULL COMMENT 'user id',
  `peer` int(9) NOT NULL COMMENT 'peer uid',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0=new, 1=read, 2=deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail
-- ----------------------------

-- ----------------------------
-- Table structure for `mail_banlist`
-- ----------------------------
DROP TABLE IF EXISTS `mail_banlist`;
CREATE TABLE `mail_banlist` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `user` int(9) NOT NULL,
  `ban_user` int(9) NOT NULL,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_banlist
-- ----------------------------

-- ----------------------------
-- Table structure for `mail_content`
-- ----------------------------
DROP TABLE IF EXISTS `mail_content`;
CREATE TABLE `mail_content` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `from` int(9) NOT NULL,
  `to` int(9) NOT NULL,
  `content` text,
  `ctime` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mail_content
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`pid`  int(9) NOT NULL DEFAULT 0 ,
`cid`  int(9) NOT NULL DEFAULT 0 ,
`reply`  int(9) NULL DEFAULT NULL ,
`thread`  int(9) NOT NULL ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`atime`  int(11) NOT NULL ,
`ctime`  int(11) NOT NULL ,
`mtime`  int(11) NOT NULL ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `problem`
-- ----------------------------
DROP TABLE IF EXISTS `problem`;
CREATE TABLE `problem` (
`pid`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`output`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`in_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`out_path`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sample_input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sample_output`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`hint`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`source`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sample_Program`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`time_limit`  int(5) NOT NULL DEFAULT 1000 ,
`memory_limit`  int(5) NOT NULL DEFAULT 65536 ,
`atime`  int(11) NOT NULL DEFAULT 0 ,
`ctime`  int(11) NOT NULL ,
`mtime`  int(11) NOT NULL ,
`stime`  int(11) NOT NULL DEFAULT 0 ,
`accept`  int(5) NOT NULL DEFAULT 0 ,
`solved`  int(5) NOT NULL DEFAULT 0 ,
`submit`  int(5) NOT NULL DEFAULT 0 ,
`submit_user`  int(5) NOT NULL DEFAULT 0 ,
`error`  int(5) NOT NULL DEFAULT 0 ,
`ratio`  tinyint(4) NOT NULL DEFAULT 0 ,
`difficulty`  tinyint(4) NOT NULL DEFAULT 0 ,
`view`  int(9) NOT NULL DEFAULT 0 ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`pid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1000

;

-- ----------------------------
-- Records of problem
-- ----------------------------

-- ----------------------------
-- Table structure for `program_language`
-- ----------------------------
DROP TABLE IF EXISTS `program_language`;
CREATE TABLE `program_language` (
`id`  tinyint(4) NOT NULL ,
`name`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`ext_time`  int(9) NOT NULL DEFAULT 0 ,
`ext_memory`  int(9) NOT NULL DEFAULT 0 ,
`time_factor`  tinyint(3) NOT NULL DEFAULT 1 ,
`memory_factor`  tinyint(3) NOT NULL DEFAULT 1 ,
`ext`  varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`exe`  varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`complie_order`  tinyint(3) NOT NULL DEFAULT 0 ,
`compile_cmd`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`brush`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'SyntaxHighlighter brush' ,
`script`  tinyint(1) NOT NULL DEFAULT 0 ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of program_language
-- ----------------------------
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('0', 'G++', '', '0', '996', '1', '1', 'cc', 'exe', '1', 'C:\\power\\oj\\JudgeOnline\\bin\\gcc\\bin\\g++.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', 'cpp', '0', '1');
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('1', 'GCC', '', '0', '996', '1', '1', 'c', 'exe', '1', 'C:\\power\\oj\\JudgeOnline\\bin\\gcc\\bin\\gcc.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', 'cpp', '0', '1');
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('2', 'Pascal', '', '0', '1000', '1', '1', 'pas', 'exe', '0', 'C:\\power\\oj\\JudgeOnline\\bin\\fpc\\fpc.exe -Sg -dONLINE_JUDGE %PATH%%NAME%.%EXT%', 'pascal', '0', '1');
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('3', 'Java', '', '0', '8000', '3', '3', 'java', 'class', '2', 'C:\\power\\oj\\JudgeOnline\\bin\\Java\\Java.bat %PATH%', 'java', '0', '1');
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('4', 'Masm32', '', '0', '784', '1', '1', 'asm', 'exe', '1', 'C:\\power\\oj\\JudgeOnline\\bin\\asm\\masm32.bat %PATH% %NAME% %EXT%', 'xml', '0', '0');
INSERT INTO `program_language` (`id`, `name`, `description`, `ext_time`, `ext_memory`, `time_factor`, `memory_factor`, `ext`, `exe`, `complie_order`, `compile_cmd`, `brush`, `script`, `status`) VALUES ('5', 'Python', '', '0', '7000', '4', '3', 'py', 'exe', '1', 'C:\\power\\oj\\JudgeOnline\\bin\\Python\\Python.bat %PATH% %NAME% %EXT%', 'python', '0', '1');

-- ----------------------------
-- Table structure for `session`
-- ----------------------------
DROP TABLE IF EXISTS `session`;
CREATE TABLE `session` (
`session_id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' ,
`uid`  int(9) NOT NULL DEFAULT 0 ,
`name`  varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ip_address`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' ,
`user_agent`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' ,
`ctime` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'Session create time.',
`last_activity`  int(11) UNSIGNED NOT NULL DEFAULT 0 ,
`session_expires`  int(11) NOT NULL ,
`session_data`  blob NULL DEFAULT NULL ,
`user_data`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`uri`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' ,
PRIMARY KEY (`session_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Records of session
-- ----------------------------

-- ----------------------------
-- Table structure for `solution`
-- ----------------------------
DROP TABLE IF EXISTS `solution`;
CREATE TABLE `solution` (
`sid`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`pid`  int(9) NOT NULL ,
`cid`  int(9) NOT NULL DEFAULT 0 ,
`num`  int(3) NULL DEFAULT NULL ,
`time`  int(9) NULL DEFAULT NULL ,
`memory`  int(9) NULL DEFAULT NULL ,
`result`  int(5) NOT NULL ,
`language`  int(3) NOT NULL ,
`ctime`  int(11) NOT NULL ,
`error`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`source`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`code_len`  int(9) NOT NULL DEFAULT 0 ,
`system_error`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`sid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1000

;

-- ----------------------------
-- Records of solution
-- ----------------------------

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`pid`  int(5) UNSIGNED NOT NULL DEFAULT 0 ,
`uid`  int(9) NULL DEFAULT NULL ,
`tag`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`ctime`  int(11) NOT NULL DEFAULT 0 ,
`mtime`  int(11) NOT NULL DEFAULT 0 ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for `team`
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
`tid`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`name1`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`stu_id1`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`college1`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`class1`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`gender1`  enum('female','male') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`contact1`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`name2`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`stu_id2`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`college2`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`class2`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`gender2`  enum('female','male') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`contact2`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`name3`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`stu_id3`  varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`college3`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`class3`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`gender3`  enum('female','male') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`contact3`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`notice`  tinyint(1) NOT NULL DEFAULT 1 ,
`comment`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`status`  tinyint(1) NOT NULL DEFAULT 0 ,
`ctime`  int(11) NOT NULL ,
`mtime`  int(11) NOT NULL ,
`history`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`year`  int(4) NULL DEFAULT NULL ,
`girl_team`  tinyint(1) NOT NULL ,
`new_team`  tinyint(1) NOT NULL DEFAULT 0 ,
`sp_team`  tinyint(1) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`tid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of team
-- ----------------------------

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
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

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', 'root', '1', '*', '所有权限', '0', '1');
INSERT INTO `permission` VALUES ('2', 'admin', '1', 'admin', '管理', '1', '1');
INSERT INTO `permission` VALUES ('3', 'admin', '1', 'admin:view:actionLog', '查看行为日志', '2', '1');
INSERT INTO `permission` VALUES ('4', 'admin', '1', 'admin:delete:actionLog', '删除行为日志', '2', '1');
INSERT INTO `permission` VALUES ('5', 'admin', '1', 'admin:view:setting', '查看设置', '2', '1');
INSERT INTO `permission` VALUES ('6', 'admin', '1', 'admin:view:content', '查看内容管理', '2', '1');
INSERT INTO `permission` VALUES ('7', 'admin', '1', 'admin:view:user', '查看用户管理', '2', '1');
INSERT INTO `permission` VALUES ('8', 'admin', '1', 'admin:edit:user', '编辑用户管理', '2', '1');
INSERT INTO `permission` VALUES ('9', 'admin', '1', 'admin:view:system', '查看系统管理', '2', '1');
INSERT INTO `permission` VALUES ('10', 'admin', '1', 'admin:edit:system', '编辑系统管理', '2', '1');
INSERT INTO `permission` VALUES ('11', 'admin', '1', 'admin:configure', '配置', '2', '1');
INSERT INTO `permission` VALUES ('12', 'admin', '1', 'admin:add:menu', '添加菜单', '2', '1');
INSERT INTO `permission` VALUES ('13', 'admin', '1', 'admin:edit:menu', '编辑菜单', '2', '1');
INSERT INTO `permission` VALUES ('14', 'admin', '1', 'admin:delete:menu', '删除菜单', '2', '1');
INSERT INTO `permission` VALUES ('15', 'admin', '1', 'admin:add:navigation', '添加导航', '2', '1');
INSERT INTO `permission` VALUES ('16', 'admin', '1', 'admin:edit:navigation', '编辑导航', '2', '1');
INSERT INTO `permission` VALUES ('17', 'admin', '1', 'admin:delete:navigation', '删除导航', '2', '1');
INSERT INTO `permission` VALUES ('18', 'admin', '1', 'admin:upload', '上传', '2', '1');
INSERT INTO `permission` VALUES ('19', 'admin', '1', 'admin:download', '下载', '2', '1');
INSERT INTO `permission` VALUES ('20', 'admin', '1', 'admin:add:plugin', '添加插件', '2', '1');
INSERT INTO `permission` VALUES ('21', 'admin', '1', 'admin:delete:plugin', '删除插件', '2', '1');
INSERT INTO `permission` VALUES ('22', 'system', '1', 'system', '系统', '1', '1');
INSERT INTO `permission` VALUES ('23', 'system', '1', 'system:viewInfo', '查看信息', '22', '1');
INSERT INTO `permission` VALUES ('24', 'system', '1', 'system:viewTask', '查看任务', '22', '1');
INSERT INTO `permission` VALUES ('25', 'system', '1', 'system:killTask', '杀死任务', '22', '1');
INSERT INTO `permission` VALUES ('26', 'system', '1', 'system:backup', '备份', '22', '1');
INSERT INTO `permission` VALUES ('27', 'system', '1', 'system:backup:db', '数据库', '26', '1');
INSERT INTO `permission` VALUES ('28', 'system', '1', 'system:restore:db', '恢复', '22', '1');
INSERT INTO `permission` VALUES ('29', 'system', '1', 'system:optimize:db', '优化', '22', '1');
INSERT INTO `permission` VALUES ('30', 'system', '1', 'system:repair:db', '修复', '22', '1');
INSERT INTO `permission` VALUES ('31', 'user', '1', 'user', '用户', '1', '1');
INSERT INTO `permission` VALUES ('32', 'user', '1', 'user:view', '查看', '31', '1');
INSERT INTO `permission` VALUES ('33', 'user', '1', 'user:view:normal', '普通', '32', '1');
INSERT INTO `permission` VALUES ('34', 'user', '1', 'user:view:self', '自己', '32', '1');
INSERT INTO `permission` VALUES ('35', 'user', '1', 'user:view:privacy', '隐私', '32', '1');
INSERT INTO `permission` VALUES ('36', 'user', '1', 'user:view:share', '分享', '32', '1');
INSERT INTO `permission` VALUES ('37', 'admin', '1', 'user:viewLog', '查看日志', '31', '1');
INSERT INTO `permission` VALUES ('38', 'user', '1', 'user:viewLog:self', '自己的', '37', '1');
INSERT INTO `permission` VALUES ('39', 'admin', '1', 'user:deleteLog', '删除日志', '31', '1');
INSERT INTO `permission` VALUES ('40', 'user', '1', 'user:deleteLog:self', '自己的', '39', '1');
INSERT INTO `permission` VALUES ('41', 'user', '1', 'user:search', '搜索', '31', '1');
INSERT INTO `permission` VALUES ('42', 'admin', '1', 'user:archive', '存档', '31', '1');
INSERT INTO `permission` VALUES ('43', 'user', '1', 'user:archive:self', '自己', '42', '1');
INSERT INTO `permission` VALUES ('44', 'admin', '1', 'user:add', '添加', '31', '1');
INSERT INTO `permission` VALUES ('45', 'admin', '1', 'user:edit', '编辑', '31', '1');
INSERT INTO `permission` VALUES ('46', 'user', '1', 'user:edit:self', '自己', '45', '1');
INSERT INTO `permission` VALUES ('47', 'user', '1', 'user:edit:nick', '昵称', '45', '1');
INSERT INTO `permission` VALUES ('48', 'user', '1', 'user:edit:password', '密码', '45', '1');
INSERT INTO `permission` VALUES ('49', 'user', '1', 'user:upload:avatar', '上传头像', '31', '1');
INSERT INTO `permission` VALUES ('50', 'admin', '1', 'user:delete', '删除', '31', '1');
INSERT INTO `permission` VALUES ('51', 'admin', '1', 'user:switch', '切换', '31', '1');
INSERT INTO `permission` VALUES ('52', 'admin', '1', 'user:forbid', '禁用', '31', '1');
INSERT INTO `permission` VALUES ('53', 'admin', '1', 'user:resume', '启用', '31', '1');
INSERT INTO `permission` VALUES ('54', 'admin', '1', 'user:build', '重建统计信息', '31', '1');
INSERT INTO `permission` VALUES ('55', 'admin', '1', 'user:buildRank', '重建排名', '31', '1');
INSERT INTO `permission` VALUES ('56', 'group', '1', 'group', '用户组', '1', '1');
INSERT INTO `permission` VALUES ('57', 'group', '1', 'group:view', '查看', '56', '1');
INSERT INTO `permission` VALUES ('58', 'admin', '1', 'group:add', '添加', '56', '1');
INSERT INTO `permission` VALUES ('59', 'admin', '1', 'group:addUser', '添加用户', '56', '1');
INSERT INTO `permission` VALUES ('60', 'admin', '1', 'group:removeUser', '移除用户', '56', '1');
INSERT INTO `permission` VALUES ('61', 'admin', '1', 'group:edit', '编辑', '56', '1');
INSERT INTO `permission` VALUES ('62', 'admin', '1', 'group:delete', '删除', '56', '1');
INSERT INTO `permission` VALUES ('63', 'admin', '1', 'group:forbid', '禁用', '56', '1');
INSERT INTO `permission` VALUES ('64', 'admin', '1', 'group:resume', '启用', '56', '1');
INSERT INTO `permission` VALUES ('65', 'admin', '1', 'permission', '权限', '1', '1');
INSERT INTO `permission` VALUES ('66', 'admin', '1', 'permission:add', '添加', '65', '1');
INSERT INTO `permission` VALUES ('67', 'admin', '1', 'permission:view', '查看', '65', '1');
INSERT INTO `permission` VALUES ('68', 'admin', '1', 'permission:edit', '编辑', '65', '1');
INSERT INTO `permission` VALUES ('69', 'admin', '1', 'permission:delete', '删除', '65', '1');
INSERT INTO `permission` VALUES ('70', 'admin', '1', 'permission:forbid', '禁用', '65', '1');
INSERT INTO `permission` VALUES ('71', 'admin', '1', 'permission:resume', '启用', '65', '1');
INSERT INTO `permission` VALUES ('72', 'problem', '1', 'problem', '题目', '1', '1');
INSERT INTO `permission` VALUES ('73', '', '1', 'problem:view', '查看', '72', '1');
INSERT INTO `permission` VALUES ('74', 'problem', '1', 'problem:view:normal', '普通', '73', '1');
INSERT INTO `permission` VALUES ('75', 'problem', '1', 'problem:status', '状态', '72', '1');
INSERT INTO `permission` VALUES ('76', 'problem', '1', 'problem:discuss', '讨论', '72', '1');
INSERT INTO `permission` VALUES ('77', 'problem', '1', 'problem:submit', '提交', '72', '1');
INSERT INTO `permission` VALUES ('78', 'problem', '1', 'problem:search', '搜索', '72', '1');
INSERT INTO `permission` VALUES ('79', 'admin', '1', 'problem:add', '添加', '72', '1');
INSERT INTO `permission` VALUES ('80', 'admin', '1', 'problem:edit', '编辑', '72', '1');
INSERT INTO `permission` VALUES ('81', 'admin', '1', 'problem:edit:title', '标题', '80', '1');
INSERT INTO `permission` VALUES ('82', 'admin', '1', 'problem:edit:hint', '提示', '80', '1');
INSERT INTO `permission` VALUES ('83', 'admin', '1', 'problem:edit:source', '来源', '80', '1');
INSERT INTO `permission` VALUES ('84', '', '1', 'problem:addTag', '添加标签', '72', '1');
INSERT INTO `permission` VALUES ('85', 'problem', '1', 'problem:viewTag', '查看标签', '72', '1');
INSERT INTO `permission` VALUES ('86', 'admin', '1', 'problem:editTag', '编辑标签', '72', '1');
INSERT INTO `permission` VALUES ('87', 'admin', '1', 'problem:deleteTag', '删除标签', '72', '1');
INSERT INTO `permission` VALUES ('88', 'admin', '1', 'problem:delete', '删除', '72', '1');
INSERT INTO `permission` VALUES ('89', 'admin', '1', 'problem:forbid', '禁用', '72', '1');
INSERT INTO `permission` VALUES ('90', 'admin', '1', 'problem:resume', '启用', '72', '1');
INSERT INTO `permission` VALUES ('91', 'admin', '1', 'problem:rejudge', '重判', '72', '1');
INSERT INTO `permission` VALUES ('92', 'admin', '1', 'problem:rebuild', '重建统计信息', '72', '1');
INSERT INTO `permission` VALUES ('93', 'admin', '1', 'problem:import', '导入', '72', '1');
INSERT INTO `permission` VALUES ('94', 'admin', '1', 'problem:export', '导出', '72', '1');
INSERT INTO `permission` VALUES ('95', 'admin', '1', 'problem:upload', '上传', '72', '1');
INSERT INTO `permission` VALUES ('96', 'admin', '1', 'problem:upload:image', '图片', '95', '1');
INSERT INTO `permission` VALUES ('97', 'admin', '1', 'problem:upload:data', '数据', '95', '1');
INSERT INTO `permission` VALUES ('98', 'admin', '1', 'problem:viewData', '查看数据', '72', '1');
INSERT INTO `permission` VALUES ('99', 'admin', '1', 'problem:editData', '编辑数据', '72', '1');
INSERT INTO `permission` VALUES ('100', 'admin', '1', 'problem:deleteData', '删除数据', '72', '1');
INSERT INTO `permission` VALUES ('101', 'contest', '1', 'contest', '比赛', '1', '1');
INSERT INTO `permission` VALUES ('102', 'contest', '1', 'contest:view', '查看', '101', '1');
INSERT INTO `permission` VALUES ('103', 'contest', '1', 'contest:view:normal', '普通', '102', '1');
INSERT INTO `permission` VALUES ('104', 'contest', '1', 'contest:view:public', '公开', '102', '1');
INSERT INTO `permission` VALUES ('105', 'contest', '1', 'contest:view:private', '私有', '102', '1');
INSERT INTO `permission` VALUES ('106', 'contest', '1', 'contest:view:password', '密码访问', '102', '1');
INSERT INTO `permission` VALUES ('107', 'contest', '1', 'contest:viewCode', '查看代码', '101', '1');
INSERT INTO `permission` VALUES ('108', 'contest', '1', 'contest:viewCode:self', '自己', '101', '1');
INSERT INTO `permission` VALUES ('109', 'contest', '1', 'contest:submit', '提交', '101', '1');
INSERT INTO `permission` VALUES ('110', 'contest', '1', 'contest:discuss', '讨论', '101', '1');
INSERT INTO `permission` VALUES ('111', 'admin', '1', 'contest:add', '添加', '101', '1');
INSERT INTO `permission` VALUES ('112', 'admin', '1', 'contest:edit', '编辑', '101', '1');
INSERT INTO `permission` VALUES ('113', 'admin', '1', 'contest:edit:title', '标题', '112', '1');
INSERT INTO `permission` VALUES ('114', 'admin', '1', 'contest:edit:time', '时间', '112', '1');
INSERT INTO `permission` VALUES ('115', 'admin', '1', 'contest:edit:type', '类型', '112', '1');
INSERT INTO `permission` VALUES ('116', 'admin', '1', 'contest:edit:description', '描述', '112', '1');
INSERT INTO `permission` VALUES ('117', 'admin', '1', 'contest:freezeBoard', '封榜', '101', '1');
INSERT INTO `permission` VALUES ('118', 'admin', '1', 'contest:notify', '通知', '101', '1');
INSERT INTO `permission` VALUES ('119', 'admin', '1', 'contest:addProblem', '添加题目', '101', '1');
INSERT INTO `permission` VALUES ('120', 'contest', '1', 'contest:viewProblem', '查看题目', '101', '1');
INSERT INTO `permission` VALUES ('121', 'admin', '1', 'contest:editProblem', '编辑题目', '101', '1');
INSERT INTO `permission` VALUES ('122', 'admin', '1', 'contest:removeProblem', '移除题目', '101', '1');
INSERT INTO `permission` VALUES ('123', 'admin', '1', 'contest:addUser', '添加用户', '101', '1');
INSERT INTO `permission` VALUES ('124', 'admin', '1', 'contest:removeUser', '移除用户', '101', '1');
INSERT INTO `permission` VALUES ('125', 'admin', '1', 'contest:addGroup', '添加用户组', '101', '1');
INSERT INTO `permission` VALUES ('126', 'admin', '1', 'contest:removeGroup', '移除用户组', '101', '1');
INSERT INTO `permission` VALUES ('127', 'admin', '1', 'contest:copy', '复制', '101', '1');
INSERT INTO `permission` VALUES ('128', 'admin', '1', 'contest:import', '导入', '101', '1');
INSERT INTO `permission` VALUES ('129', 'admin', '1', 'contest:export', '导出', '101', '1');
INSERT INTO `permission` VALUES ('130', 'admin', '1', 'contest:rejudge', '重判', '101', '1');
INSERT INTO `permission` VALUES ('131', 'admin', '1', 'contest:buildRank', '重建排名', '101', '1');
INSERT INTO `permission` VALUES ('132', 'code', '1', 'code', '代码', '1', '1');
INSERT INTO `permission` VALUES ('133', 'admin', '1', 'code:view', '查看', '132', '1');
INSERT INTO `permission` VALUES ('134', 'code', '1', 'code:view:self', '自己', '133', '1');
INSERT INTO `permission` VALUES ('135', 'code', '1', 'code:view:share', '分享', '133', '1');
INSERT INTO `permission` VALUES ('136', 'code', '1', 'code:search', '搜索', '132', '1');
INSERT INTO `permission` VALUES ('137', 'code', '1', 'code:add', '添加', '132', '1');
INSERT INTO `permission` VALUES ('138', 'code', '1', 'code:edit', '编辑', '132', '1');
INSERT INTO `permission` VALUES ('139', 'code', '1', 'code:edit:self', '自己', '138', '1');
INSERT INTO `permission` VALUES ('140', 'code', '1', 'code:judge', '评测', '132', '1');
INSERT INTO `permission` VALUES ('141', 'admin', '1', 'code:rejudge', '重判', '132', '1');
INSERT INTO `permission` VALUES ('142', 'mail', '1', 'mail', '邮件', '0', '1');
INSERT INTO `permission` VALUES ('143', '', '1', 'mail:view', '查看', '142', '1');
INSERT INTO `permission` VALUES ('144', 'mail', '1', 'mail:view:self', '自己', '143', '1');
INSERT INTO `permission` VALUES ('145', 'mail', '1', 'mail:send', '发送', '142', '1');
INSERT INTO `permission` VALUES ('146', 'admin', '1', 'mail:massSend', '群发', '142', '1');
INSERT INTO `permission` VALUES ('147', '', '1', 'mail:delete', '删除', '142', '1');
INSERT INTO `permission` VALUES ('148', 'mail', '1', 'mail:delete:self', '自己', '147', '1');
INSERT INTO `permission` VALUES ('149', 'mail', '1', 'mail:clean:self', '清空', '142', '1');
INSERT INTO `permission` VALUES ('150', 'comment', '1', 'comment', '评论', '1', '1');
INSERT INTO `permission` VALUES ('151', '', '1', 'comment:view', '查看', '150', '1');
INSERT INTO `permission` VALUES ('152', 'comment', '1', 'comment:view:normal', '普通', '151', '1');
INSERT INTO `permission` VALUES ('153', 'comment', '1', 'comment:add', '添加', '150', '1');
INSERT INTO `permission` VALUES ('154', 'admin', '1', 'comment:edit', '编辑', '150', '1');
INSERT INTO `permission` VALUES ('155', 'comment', '1', 'comment:edit:self', '自己', '154', '1');
INSERT INTO `permission` VALUES ('156', 'admin', '1', 'comment:delete', '删除', '150', '1');
INSERT INTO `permission` VALUES ('157', 'comment', '1', 'comment:delete:self', '自己', '156', '1');
INSERT INTO `permission` VALUES ('158', 'admin', '1', 'comment:audit', '审核', '150', '1');
INSERT INTO `permission` VALUES ('159', 'admin', '1', 'comment:top', '置顶', '150', '1');
INSERT INTO `permission` VALUES ('160', 'admin', '1', 'comment:forbid', '禁用', '150', '1');
INSERT INTO `permission` VALUES ('161', 'admin', '1', 'comment:resume', '启用', '150', '1');
INSERT INTO `permission` VALUES ('162', 'announcement', '1', 'announcement', '公告', '1', '1');
INSERT INTO `permission` VALUES ('163', '', '1', 'announcement:view', '查看', '162', '1');
INSERT INTO `permission` VALUES ('164', 'announcement', '1', 'announcement:view:normal', '普通', '163', '1');
INSERT INTO `permission` VALUES ('165', '', '1', 'announcement:list', '列表', '162', '1');
INSERT INTO `permission` VALUES ('166', 'announcement', '1', 'announcement:list:normal', '普通', '165', '1');
INSERT INTO `permission` VALUES ('167', 'admin', '1', 'announcement:add', '添加', '162', '1');
INSERT INTO `permission` VALUES ('168', 'admin', '1', 'announcement:edit', '编辑', '162', '1');
INSERT INTO `permission` VALUES ('169', 'admin', '1', 'announcement:edit:title', '标题', '168', '1');
INSERT INTO `permission` VALUES ('170', 'admin', '1', 'announcement:edit:time', '时间', '168', '1');
INSERT INTO `permission` VALUES ('171', 'admin', '1', 'announcement:delete', '删除', '168', '1');
INSERT INTO `permission` VALUES ('172', 'admin', '1', 'announcement:forbid', '禁用', '162', '1');
INSERT INTO `permission` VALUES ('173', 'admin', '1', 'announcement:resume', '启用', '162', '1');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(80) NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'root', 'root', '1');
INSERT INTO `role` VALUES ('2', 'admin', 'administrator', '1');

-- ----------------------------
-- Table structure for `role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `rid` int(9) NOT NULL,
  `pid` int(9) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `rid` int(9) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1000', '1', '1');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`uid`  int(9) NOT NULL AUTO_INCREMENT COMMENT 'Unique user ID, internal use.' ,
`tid`  int(9) NOT NULL DEFAULT 0 COMMENT 'refere team id.' ,
`name`  varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'unique user login name.' ,
`pass`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'User’s password (hashed).' ,
`nick`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'nick' ,
`realname`  varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`email`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'User’s e-mail address.' ,
`language`  int(5) NOT NULL DEFAULT 0 ,
`school`  varchar(65) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`solved`  int(6) NOT NULL DEFAULT 0 COMMENT 'the number of problems user solved' ,
`accept`  int(6) NOT NULL DEFAULT 0 ,
`submit`  int(6) NOT NULL DEFAULT 0 COMMENT 'the number of user submit code' ,
`atime`  int(11) NOT NULL DEFAULT 0 COMMENT 'Timestamp for previous time user accessed the site.' ,
`ctime`  int(11) NOT NULL DEFAULT 0 COMMENT 'Timestamp for when user was created.' ,
`mtime`  int(11) NOT NULL DEFAULT 0 COMMENT 'Timestamp for when user edit its profile.' ,
`login`  int(11) NOT NULL DEFAULT 0 COMMENT 'Timestamp for user last login.' ,
`login_ip` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`phone`  varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`qq`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`blog`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`gender`  enum('female','male','secret') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'secret' ,
`comefrom` varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`online`  int(9) NOT NULL DEFAULT 0 ,
`level`  int(5) NOT NULL DEFAULT 1 ,
`credit`  int(9) NOT NULL DEFAULT 0 ,
`share`  tinyint(1) NOT NULL DEFAULT 0 ,
`avatar` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'user avatar path' ,
`sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
`checkin` int(11) NOT NULL DEFAULT '0',
`checkin_times` int(5) NOT NULL DEFAULT '0',
`status`  tinyint(2) NOT NULL DEFAULT 1 COMMENT 'Whether the user is active(1) or blocked(0).' ,
`data`  blob NULL DEFAULT NULL ,
`token`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`uid`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1001

;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` (`uid`, `tid`, `name`, `pass`, `nick`, `realname`, `email`, `language`, `school`, `solved`, `accept`, `submit`, `atime`, `ctime`, `mtime`, `login`, `phone`, `qq`, `blog`, `gender`, `online`, `level`, `credit`, `share`, `avatar`, `status`, `data`, `token`) VALUES ('1000', '0', 'root', '$2a$10$lyKeLNMNYC6eXhmTb6CMb.NvtMS1SfQTIZRCddnoes6sGfk4gwsQS', null, null, '', '0', null, '0', '0', '0', '0', '0', '0', '0', null, null, null, 'secret', '0', '1', '0', '0', null, '1', null, null);

-- ----------------------------
-- Table structure for `user_ext`
-- ----------------------------
DROP TABLE IF EXISTS `user_ext`;
CREATE TABLE `user_ext` (
  `uid` int(9) NOT NULL,
  `tid` int(9) NOT NULL DEFAULT '0',
  `realname` varchar(35) DEFAULT NULL,
  `phone` varchar(35) DEFAULT NULL,
  `qq` varchar(15) DEFAULT NULL,
  `blog` varchar(255) DEFAULT NULL,
  `share` tinyint(1) NOT NULL DEFAULT '0',
  `online` int(9) NOT NULL DEFAULT '0',
  `level` int(9) NOT NULL DEFAULT '1',
  `credit` int(9) NOT NULL DEFAULT '0',
  `exp` int(9) NOT NULL DEFAULT '0',
  `checkin` int(11) NOT NULL DEFAULT '0',
  `checkin_times` tinyint(3) NOT NULL DEFAULT '0',
  `last_send_drift` int(11) NOT NULL DEFAULT '0',
  `send_drift` tinyint(3) NOT NULL DEFAULT '0',
  `last_get_drift` int(11) NOT NULL DEFAULT '0',
  `get_drift` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_ext
-- ----------------------------
INSERT INTO `user_ext` VALUES ('1000', '0', null, null, null, null, '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `variable`
-- ----------------------------
DROP TABLE IF EXISTS `variable`;
CREATE TABLE `variable` (
`id`  int(10) NOT NULL AUTO_INCREMENT ,
`cid`  int(11) NULL DEFAULT NULL ,
`name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`value`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`boolean_value`  tinyint(1) NULL DEFAULT NULL ,
`int_value`  int(11) NULL DEFAULT NULL ,
`text_value`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`type`  enum('boolean','int','string','text') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'string' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=15

;

-- ----------------------------
-- Records of variable
-- ----------------------------
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('1', null, 'work_path', 'C:\\power\\oj\\temp', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('2', null, 'data_path', 'C:\\power\\oj\\data', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('3', null, 'run_shell', 'C:\\power\\oj\\JudgeOnline\\bin\\run.exe', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('4', null, 'compile_shell', 'C:\\power\\oj\\JudgeOnline\\bin\\com.exe', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('5', null, 'admin_name', 'root', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('6', null, 'admin_mail', 'root@localhost.com', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('7', null, 'debug_file', 'debug.log', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('8', null, 'error_file', 'error.log', null, null, null, 'string', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('9', null, 'delete_tmp_file', null, '1', null, null, 'boolean', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('10', null, 'debug', null, '0', null, null, 'boolean', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('11', null, 'enable_login', null, '1', null, null, 'boolean', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('12', null, 'enable_mail', null, '1', null, null, 'boolean', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('13', null, 'enable_source', null, '1', null, null, 'boolean', null);
INSERT INTO `variable` (`id`, `cid`, `name`, `value`, `boolean_value`, `int_value`, `text_value`, `type`, `description`) VALUES ('14', null, 'enable_archive', null, '1', null, null, 'boolean', null);

-- ----------------------------
-- Auto increment value for `announce`
-- ----------------------------
ALTER TABLE `announce` AUTO_INCREMENT=1000;

-- ----------------------------
-- Auto increment value for `board`
-- ----------------------------
ALTER TABLE `board` AUTO_INCREMENT=1;

-- ----------------------------
-- Indexes structure for table `contest`
-- ----------------------------
CREATE INDEX `search` USING BTREE ON `contest`(`title`) ;

-- ----------------------------
-- Auto increment value for `contest`
-- ----------------------------
ALTER TABLE `contest` AUTO_INCREMENT=1000;

-- ----------------------------
-- Auto increment value for `contest_problem`
-- ----------------------------
ALTER TABLE `contest_problem` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `freeze_board`
-- ----------------------------
ALTER TABLE `freeze_board` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `loginlog`
-- ----------------------------
ALTER TABLE `loginlog` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `mail`
-- ----------------------------
ALTER TABLE `mail` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `message`
-- ----------------------------
ALTER TABLE `message` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `problem`
-- ----------------------------
ALTER TABLE `problem` AUTO_INCREMENT=1000;

-- ----------------------------
-- Auto increment value for `role`
-- ----------------------------
ALTER TABLE `roles` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for `solution`
-- ----------------------------
ALTER TABLE `solution` AUTO_INCREMENT=1000;

-- ----------------------------
-- Auto increment value for `tag`
-- ----------------------------
ALTER TABLE `tag` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `team`
-- ----------------------------
ALTER TABLE `team` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `user`
-- ----------------------------
ALTER TABLE `user` AUTO_INCREMENT=1001;

-- ----------------------------
-- Auto increment value for `variable`
-- ----------------------------
ALTER TABLE `variable` AUTO_INCREMENT=15;

-- ----------------------------
-- View structure for `contest_solution`
-- ----------------------------
DROP VIEW IF EXISTS `contest_solution`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `contest_solution` AS select * from `solution` where (`solution`.`cid` <> 0);
