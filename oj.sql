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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of freeze_board
-- ----------------------------

-- ----------------------------
-- Table structure for `loginlog`
-- ----------------------------
DROP TABLE IF EXISTS `loginlog`;
CREATE TABLE `loginlog` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL DEFAULT 0 ,
`password`  blob NULL DEFAULT NULL ,
`ip`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ctime`  int(11) NOT NULL ,
`succeed`  tinyint(1) NOT NULL DEFAULT 0 ,
`info`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
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
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`from`  int(9) NOT NULL ,
`to`  int(9) NOT NULL ,
`title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`reply`  int(9) NULL DEFAULT NULL ,
`read`  tinyint(1) NOT NULL DEFAULT 0 ,
`atime`  int(11) NULL DEFAULT NULL ,
`ctime`  int(11) NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of mail
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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
`id`  int(9) NOT NULL AUTO_INCREMENT ,
`uid`  int(9) NOT NULL ,
`role`  enum('root','administrator','member','source_browser','title') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'title' ,
`status`  tinyint(1) NOT NULL DEFAULT 1 ,
PRIMARY KEY (`id`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` (`id`, `uid`, `role`, `status`) VALUES ('1', '1000', 'root', '1');

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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
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
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of team
-- ----------------------------

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
`login`  int(11) NOT NULL DEFAULT 0 COMMENT 'Timestamp for user\'s last login.' ,
`phone`  varchar(35) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`qq`  int(11) NULL DEFAULT NULL ,
`blog`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`gender`  enum('female','male','secret') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'secret' ,
`online`  int(9) NOT NULL DEFAULT 0 ,
`level`  int(5) NOT NULL DEFAULT 1 ,
`credit`  int(9) NOT NULL DEFAULT 0 ,
`share`  tinyint(1) NOT NULL DEFAULT 0 ,
`avatar`  enum('bmp','gif','png','jpeg','jpg') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'uid.suffix(e.g.15.png)' ,
`role`  enum('title','source_browser','member','administrator','root') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`status`  tinyint(2) NOT NULL DEFAULT 1 COMMENT 'Whether the user is active(1) or blocked(0).' ,
`data`  blob NULL DEFAULT NULL ,
`token`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`uid`)
)
ENGINE=MyISAM
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1001

;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` (`uid`, `tid`, `name`, `pass`, `nick`, `realname`, `email`, `language`, `school`, `solved`, `accept`, `submit`, `atime`, `ctime`, `mtime`, `login`, `phone`, `qq`, `blog`, `gender`, `online`, `level`, `credit`, `share`, `avatar`, `role`, `status`, `data`, `token`) VALUES ('1000', '0', 'root', '$2a$10$lyKeLNMNYC6eXhmTb6CMb.NvtMS1SfQTIZRCddnoes6sGfk4gwsQS', null, null, '', '0', null, '0', '0', '0', '0', '0', '0', '0', null, null, null, 'secret', '0', '1', '0', '0', null, null, '1', null, null);

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
ENGINE=MyISAM
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
ALTER TABLE `role` AUTO_INCREMENT=2;

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
