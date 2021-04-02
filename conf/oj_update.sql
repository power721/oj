ALTER TABLE contest CHANGE `start_time` `startTime` int(11) DEFAULT '0',
                    CHANGE `end_time` `endTime` int(11) DEFAULT '0',
                    CHANGE `pass` `password` varchar(255) DEFAULT NULL,
                    CHANGE `freeze` `freeze` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE contest_clarify ADD `num` tinyint(5) DEFAULT NULL AFTER `cid`;

ALTER TABLE contest_problem CHANGE `accept` `accepted` int(5) NOT NULL DEFAULT '0',
                            CHANGE `submit` `submission` int(5) NOT NULL DEFAULT '0',
                            CHANGE `first_blood` `firstBloodUid` int(9) NOT NULL DEFAULT '0' COMMENT 'first user(uid) solved this problem',
                            CHANGE `first_blood_time` `firstBloodTime` int(9) NOT NULL DEFAULT '-1' COMMENT 'first time(minutes) solved this problem';

ALTER TABLE board CHANGE `A_time` `A_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `A_WrongSubmits` `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `B_time` `B_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `B_WrongSubmits` `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `C_time` `C_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `C_WrongSubmits` `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `D_time` `D_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `D_WrongSubmits` `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `E_time` `E_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `E_WrongSubmits` `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `F_time` `F_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `F_WrongSubmits` `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `G_time` `G_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `G_WrongSubmits` `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `H_time` `H_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `H_WrongSubmits` `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `I_time` `I_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `I_WrongSubmits` `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `J_time` `J_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `J_WrongSubmits` `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `K_time` `K_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `K_WrongSubmits` `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `L_time` `L_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `L_WrongSubmits` `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `M_time` `M_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `M_WrongSubmits` `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `N_time` `N_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `N_WrongSubmits` `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `O_time` `O_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `O_WrongSubmits` `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `P_time` `P_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `P_WrongSubmits` `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `Q_time` `Q_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `Q_WrongSubmits` `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `R_time` `R_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `R_WrongSubmits` `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `S_time` `S_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `S_WrongSubmits` `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `T_time` `T_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `T_WrongSubmits` `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `U_time` `U_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `U_WrongSubmits` `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `V_time` `V_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `V_WrongSubmits` `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `W_time` `W_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `W_WrongSubmits` `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `X_time` `X_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `X_WrongSubmits` `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `Y_time` `Y_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `Y_WrongSubmits` `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
                  CHANGE `Z_time` `Z_SolvedTime` int(11) DEFAULT '0',
                  CHANGE `Z_WrongSubmits` `Z_WrongNum` tinyint(5) unsigned DEFAULT '0';

ALTER TABLE freeze_board CHANGE `A_time` `A_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `A_WrongSubmits` `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `B_time` `B_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `B_WrongSubmits` `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `C_time` `C_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `C_WrongSubmits` `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `D_time` `D_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `D_WrongSubmits` `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `E_time` `E_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `E_WrongSubmits` `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `F_time` `F_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `F_WrongSubmits` `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `G_time` `G_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `G_WrongSubmits` `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `H_time` `H_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `H_WrongSubmits` `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `I_time` `I_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `I_WrongSubmits` `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `J_time` `J_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `J_WrongSubmits` `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `K_time` `K_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `K_WrongSubmits` `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `L_time` `L_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `L_WrongSubmits` `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `M_time` `M_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `M_WrongSubmits` `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `N_time` `N_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `N_WrongSubmits` `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `O_time` `O_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `O_WrongSubmits` `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `P_time` `P_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `P_WrongSubmits` `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `Q_time` `Q_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `Q_WrongSubmits` `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `R_time` `R_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `R_WrongSubmits` `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `S_time` `S_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `S_WrongSubmits` `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `T_time` `T_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `T_WrongSubmits` `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `U_time` `U_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `U_WrongSubmits` `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `V_time` `V_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `V_WrongSubmits` `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `W_time` `W_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `W_WrongSubmits` `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `X_time` `X_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `X_WrongSubmits` `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `Y_time` `Y_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `Y_WrongSubmits` `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
                         CHANGE `Z_time` `Z_SolvedTime` int(11) DEFAULT '0',
                         CHANGE `Z_WrongSubmits` `Z_WrongNum` tinyint(5) unsigned DEFAULT '0';

ALTER TABLE friend CHANGE `user`  `userId` int(9) NOT NULL,
                   CHANGE `friend`  `friendUid` int(9) NOT NULL;

#ALTER TABLE level CHANGE `exp` `experience` int(9) NOT NULL;

ALTER TABLE loginlog DROP `password`,
                     DROP `info`;

#ALTER TABLE mail CHANGE `user` `userId` int(9) NOT NULL COMMENT 'user id',
#                 CHANGE `peer` `peerUid` int(9) NOT NULL COMMENT 'peer uid';

ALTER TABLE mail_banlist CHANGE `user` `uid` int(9) NOT NULL,
                         CHANGE `ban_user` `bannedUid` int(9) NOT NULL;

ALTER TABLE mail_content CHANGE `from` `fromUid` int(9) NOT NULL,
                         CHANGE `to` `toUid` int(9) NOT NULL;

ALTER TABLE notice CHANGE `editor` `editorUid` int(9) DEFAULT NULL,
                   CHANGE `start_time` `startTime` int(11) NOT NULL,
                   CHANGE `end_time` `endTime` int(11) NOT NULL;

ALTER TABLE problem DROP `in_path`,
                    DROP `out_path`,
                    CHANGE `sample_Program` `sampleProgram` text,
                    CHANGE `sample_input` `sampleInput` text,
                    CHANGE `sample_output` `sampleOutput` text,
                    CHANGE `time_limit` `timeLimit` int(5) NOT NULL DEFAULT '1000',
                    CHANGE `memory_limit` `memoryLimit` int(5) NOT NULL DEFAULT '65536',
                    CHANGE `accept` `accepted` int(5) NOT NULL DEFAULT '0',
                    CHANGE `submit` `submission` int(5) NOT NULL DEFAULT '0',
                    CHANGE `submit_user` `submitUser` int(5) NOT NULL DEFAULT '0';

ALTER TABLE program_language CHANGE `ext_time` `extTime` int(9) NOT NULL DEFAULT '0',
                             CHANGE `ext_memory` `extMemory` int(9) NOT NULL DEFAULT '0',
                             CHANGE `time_factor` `timeFactor` tinyint(3) NOT NULL DEFAULT '1',
                             CHANGE `memory_factor` `memoryFactor` tinyint(3) NOT NULL DEFAULT '1',
                             CHANGE `complie_order` `complieOrder` tinyint(3) NOT NULL DEFAULT '0',
                             CHANGE `compile_cmd` `compileCmd` varchar(255) NOT NULL;

ALTER TABLE session CHANGE `session_id` `sessionId` varchar(40) NOT NULL DEFAULT '0',
                    CHANGE `ip_address` `ipAddress` varchar(45) NOT NULL DEFAULT '0',
                    CHANGE `user_agent` `userAgent` varchar(255) DEFAULT '',
                    CHANGE `last_activity` `lastActivity` int(11) NOT NULL DEFAULT '0',
                    CHANGE `session_expires` `sessionExpires` int(11) NOT NULL;

ALTER TABLE solution CHANGE `code_len` `codeLen` int(9) NOT NULL DEFAULT '0',
                     CHANGE `system_error` `systemError` text;

ALTER TABLE team CHANGE `stu_id1` `stuId1` varchar(8) NOT NULL,
                 CHANGE `stu_id2` `stuId2` varchar(8) NOT NULL,
                 CHANGE `stu_id3` `stuId3` varchar(8) NOT NULL,
                 CHANGE `girl_team` `isGirlTeam` tinyint(1) NOT NULL DEFAULT '0',
                 CHANGE `new_team` `isRookieTeam` tinyint(1) NOT NULL DEFAULT '0',
                 CHANGE `sp_team` `isSpecialTeam` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE topic ADD `view` int(9) NOT NULL DEFAULT '0';

ALTER TABLE user CHANGE `pass` `password` varchar(128) NOT NULL COMMENT 'User’s password (hashed).',
                 CHANGE `realname` `realName` varchar(35) DEFAULT NULL,
                 CHANGE `reg_email` `regEmail` varchar(64) NOT NULL,
                 CHANGE `email_verified` `emailVerified` tinyint(1) NOT NULL DEFAULT '0',
                 CHANGE `accept` `accepted` int(6) NOT NULL DEFAULT '0',
                 CHANGE `submit` `submission` int(6) NOT NULL DEFAULT '0' COMMENT 'the number of user submit code',
                 CHANGE `login` `loginTime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for user last login.',
                 CHANGE `login_IP` `loginIP` varchar(64) DEFAULT NULL,
                 CHANGE `comefrom` `comeFrom` varchar(35) DEFAULT NULL,
                 CHANGE `sign` `signature` varchar(255) DEFAULT NULL,
                 CHANGE `share` `shareCode` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE user_ext CHANGE `exp` `experience` int(9) NOT NULL DEFAULT '0',
                     CHANGE `realname` `realName` varchar(35) DEFAULT NULL,
                     CHANGE `share` `shareCode` tinyint(1) NOT NULL DEFAULT '0',
                     CHANGE `checkin_times` `checkinTimes` tinyint(5) NOT NULL DEFAULT '0',
                     CHANGE `total_checkin` `totalCheckin` int(9) NOT NULL DEFAULT '0',
                     CHANGE `last_send_drift` `lastSendDrift` int(11) NOT NULL DEFAULT '0',
                     CHANGE `send_drift` `sendDriftNum` tinyint(3) NOT NULL DEFAULT '0',
                     CHANGE `last_get_drift` `lastGetDrift` int(11) NOT NULL DEFAULT '0',
                     CHANGE `get_drift` `getDriftNum` tinyint(3) NOT NULL DEFAULT '0';

ALTER TABLE web_login CHANGE `open_id` `openId` varchar(64) NOT NULL;

ALTER TABLE variable
                     CHANGE `booleanValue` `booleanValue` tinyint(1) DEFAULT NULL,
                     CHANGE `intValue` `intValue` int(11) DEFAULT NULL,
                     CHANGE `textValue` `textValue` text DEFAULT NULL;
#2014-04-05
UPDATE solution SET language=language+1 WHERE language<5;
UPDATE contest_solution SET language=language+1 WHERE language<5;
UPDATE solution SET language=3-language WHERE language<3;
UPDATE contest_solution SET language=3-language WHERE language<3;
#2014-04-06
ALTER TABLE solution ADD `mtime` int(11) NOT NULL AFTER `ctime`,
                      ADD `test` tinyint(3) NOT NULL DEFAULT '0' AFTER `mtime`;
ALTER TABLE contest_solution ADD `mtime` int(11) NOT NULL AFTER `ctime`,
                              ADD `test` tinyint(3) NOT NULL DEFAULT '0' AFTER `mtime`;
#2014-04-16
ALTER TABLE solution ADD `status` tinyint(1) NOT NULL DEFAULT '1';
ALTER TABLE contest_solution ADD `status` tinyint(1) NOT NULL DEFAULT '1';
#2014-04-18
ALTER TABLE board CHANGE `A_WrongNum` `A_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `B_WrongNum` `B_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `C_WrongNum` `C_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `D_WrongNum` `D_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `E_WrongNum` `E_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `F_WrongNum` `F_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `G_WrongNum` `G_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `H_WrongNum` `H_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `I_WrongNum` `I_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `J_WrongNum` `J_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `K_WrongNum` `K_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `L_WrongNum` `L_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `M_WrongNum` `M_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `N_WrongNum` `N_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `O_WrongNum` `O_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `P_WrongNum` `P_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Q_WrongNum` `Q_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `R_WrongNum` `R_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `S_WrongNum` `S_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `T_WrongNum` `T_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `U_WrongNum` `U_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `V_WrongNum` `V_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `W_WrongNum` `W_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `X_WrongNum` `X_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Y_WrongNum` `Y_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Z_WrongNum` `Z_WrongNum` tinyint(5) DEFAULT '0';

ALTER TABLE freeze_board CHANGE `A_WrongNum` `A_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `B_WrongNum` `B_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `C_WrongNum` `C_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `D_WrongNum` `D_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `E_WrongNum` `E_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `F_WrongNum` `F_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `G_WrongNum` `G_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `H_WrongNum` `H_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `I_WrongNum` `I_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `J_WrongNum` `J_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `K_WrongNum` `K_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `L_WrongNum` `L_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `M_WrongNum` `M_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `N_WrongNum` `N_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `O_WrongNum` `O_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `P_WrongNum` `P_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Q_WrongNum` `Q_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `R_WrongNum` `R_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `S_WrongNum` `S_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `T_WrongNum` `T_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `U_WrongNum` `U_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `V_WrongNum` `V_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `W_WrongNum` `W_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `X_WrongNum` `X_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Y_WrongNum` `Y_WrongNum` tinyint(5) DEFAULT '0',
                  CHANGE `Z_WrongNum` `Z_WrongNum` tinyint(5) DEFAULT '0';

#2014-05-01
UPDATE solution SET `result`=9 WHERE `result`=98;
UPDATE solution SET `result`=10 WHERE `result`=99;
UPDATE solution SET `result`=12 WHERE `result`=100;
UPDATE solution SET `result`=11 WHERE `result`=10000;

UPDATE contest_solution SET `result`=9 WHERE `result`=98;
UPDATE contest_solution SET `result`=10 WHERE `result`=99;
UPDATE contest_solution SET `result`=12 WHERE `result`=100;
UPDATE contest_solution SET `result`=11 WHERE `result`=10000;

#2014-06-02
ALTER TABLE topic ADD `threadId` int(9) DEFAULT 0,
                  ADD `parentId` int(9) DEFAULT 0;

#2014-06-04
ALTER TABLE topic ADD `orderNum` int(9) DEFAULT 0;

#2014-09-14
ALTER TABLE session CHANGE `ipAddress` `ipAddress` varchar(45) NULL DEFAULT '';

#2014-11-16
CREATE TABLE `news` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `title` varchar(1000) NOT NULL DEFAULT '',
  `content` varchar(10000) DEFAULT '',
  `time` int(11) NOT NULL,
  `author` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `view` int(9) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8;

CREATE TABLE `honors` (
  `level` varchar(255) NOT NULL,
  `contest` varchar(255) NOT NULL,
  `team` varchar(255) DEFAULT NULL,
  `player` varchar(255) NOT NULL DEFAULT '',
  `prize` varchar(255) NOT NULL DEFAULT '',
  `id` int(5) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8;

#2015-05-02
INSERT INTO `role_permission` VALUES ('2', '2', '2');
INSERT INTO `role_permission` VALUES ('3', '3', '77');

ALTER TABLE team CHANGE `stuId1` `stuId1` varchar(16) NOT NULL,
                 CHANGE `stuId2` `stuId2` varchar(16) NULL DEFAULT '',
                 CHANGE `stuId3` `stuId3` varchar(16) NULL DEFAULT '';

#2016-6-25
INSERT INTO `permission` VALUES (174,'user',1,'user:sp:nick','特殊昵称',45,1);
DELETE FROM `role`;
INSERT INTO `role` VALUES (1,'root','root',1);
INSERT INTO `role` VALUES (2,'admin','administrator',1);
INSERT INTO `role` VALUES (3,'member','team member',1);
INSERT INTO `role` VALUES (10,'user','user',1);

UPDATE `user_role` SET rid=10 WHERE rid=3;
DELETE FROM `role_permission`;
INSERT INTO `role_permission` VALUES (1,1,1);
INSERT INTO `role_permission` VALUES (2,2,2);
INSERT INTO `role_permission` VALUES (3,2,31);
INSERT INTO `role_permission` VALUES (4,3,77);
INSERT INTO `role_permission` VALUES (5,3,174);
INSERT INTO `role_permission` VALUES (6,3,84);
INSERT INTO `role_permission` VALUES (7,10,77);

#2016-6-26
ALTER TABLE contest ADD `lockReport` tinyint(1) NOT NULL DEFAULT '0',
                    CHANGE `freeze` `lockBoard`  tinyint(1) NOT NULL DEFAULT '0';

#2016-7-1
ALTER TABLE contest ADD `lockBoardTime` int(9) NOT NULL DEFAULT '60',
                    ADD `unlockBoardTime` int(9) NOT NULL DEFAULT '30';

#2016-7-2
ALTER TABLE user CHANGE `shareCode` `shareCode` tinyint(1) NOT NULL DEFAULT '1';

#2016-7-6
INSERT INTO `variable` VALUES (32,'judgeHost','127.0.0.1',NULL,NULL,NULL,'string',NULL);
INSERT INTO `variable` VALUES (33,'judgePort','12345',NULL,'12345',NULL,'int',NULL);
INSERT INTO `variable` VALUES (34,'judgeSecurity','PowerJudgeV1.1',NULL,NULL,NULL,'string',NULL);

#2016-7-18
ALTER TABLE contest_problem ADD `view` int(5) NOT NULL DEFAULT '0' AFTER `num`;
ALTER TABLE contest ADD `languages` varchar(255) DEFAULT NULL AFTER `password`;

#2016-7-20
ALTER TABLE contest_user ADD `special` tinyint(1) NOT NULL DEFAULT '0' AFTER `cid`;

#2016-7-21
CREATE TABLE `resource` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `uid` int(9) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` text,
  `path` text NOT NULL,
  `ctime` int(11) NOT NULL,
  `download` int(9) DEFAULT 0,
  `access` ENUM('public', 'private', 'security') NOT NULL DEFAULT 'public',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#2016-7-23
ALTER TABLE `contest_user` ADD nick VARCHAR(55) AFTER `special`;
UPDATE contest_user,user SET contest_user.nick=user.nick WHERE contest_user.uid=user.uid;

#2016-7-24
ALTER TABLE contest_problem ADD CONSTRAINT contest_problem_cid_pid_pk UNIQUE (cid, pid);
ALTER TABLE contest_user ADD CONSTRAINT contest_user_cid_uid_pk UNIQUE (cid, uid);
ALTER TABLE board ADD CONSTRAINT board_cid_uid_pk UNIQUE (cid, uid);
ALTER TABLE freeze_board ADD CONSTRAINT freeze_board_cid_uid_pk UNIQUE (cid, uid);
ALTER TABLE user_role ADD CONSTRAINT user_role_uid_rid_pk UNIQUE (uid, rid);

#2016-11-4
ALTER TABLE contest_user ADD `girls` tinyint(1) NOT NULL DEFAULT '0' AFTER `special`;
ALTER TABLE contest_user ADD `freshman` tinyint(1) NOT NULL DEFAULT '0' AFTER `special`;
ALTER TABLE contest_problem ADD `memoryLimit` int(5) NOT NULL DEFAULT '65536' AFTER `title`;
ALTER TABLE contest_problem ADD `timeLimit` int(5) NOT NULL DEFAULT '1000' AFTER `title`;
UPDATE contest_problem cp SET timeLimit=(SELECT timeLimit FROM problem p WHERE p.pid=cp.pid),memoryLimit=(SELECT memoryLimit FROM problem p WHERE p.pid=cp.pid);

#2017-1-18
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,56);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,72);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,101);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,132);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,142);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,150);
INSERT INTO `role_permission` (`rid`, `pid`) VALUES (2,162);

#2017-4-7
ALTER TABLE contest_user ADD `teamName` varchar(20) NOT NULL DEFAULT 'NoTeamName';
ALTER TABLE team ADD `teamNameChinese` varchar(20) NOT NULL DEFAULT 'NoTeamName';
ALTER TABLE team ADD `teamNameEnglish` varchar(20) NOT NULL DEFAULT 'NoTeamName';
ALTER TABLE `team` CHANGE `year` `cid` int(4) DEFAULT NULL;

#2017-4-13
ALTER TABLE `contest_solution` CHANGE `test` `test` int(9) NOT NULL DEFAULT '0';
ALTER TABLE `solution` CHANGE `test` `test` int(9) NOT NULL DEFAULT '0';

#2017-6-13 Add for WeiXin
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

DROP TABLE IF EXISTS `wxnews_picture`;
CREATE TABLE `wxnews_picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `pic_name` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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

#2017-6-13 add language
UPDATE `oj`.`program_language` SET `id`='1', `name`='GCC11', `description`='C语言推荐', `extTime`='0', `extMemory`='996', `timeFactor`='1', `memoryFactor`='1', `ext`='c', `exe`='exe', `complieOrder`='1', `compileCmd`='C:\\JudgeOnline\\bin\\gcc\\bin\\gcc.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', `brush`='cpp', `script`='0', `status`='1' WHERE (`id`='1');
UPDATE `oj`.`program_language` SET `id`='2', `name`='G++11', `description`='C++推荐', `extTime`='0', `extMemory`='996', `timeFactor`='1', `memoryFactor`='1', `ext`='cc', `exe`='exe', `complieOrder`='1', `compileCmd`='C:\\JudgeOnline\\bin\\gcc\\bin\\g++.exe -fno-asm -s -w -O1 -DONLINE_JUDGE -o %PATH%%NAME% %PATH%%NAME%.%EXT%', `brush`='cpp', `script`='0', `status`='1' WHERE (`id`='2');
UPDATE `oj`.`program_language` SET `id`='3', `name`='Pascal', `description`='', `extTime`='0', `extMemory`='1000', `timeFactor`='1', `memoryFactor`='1', `ext`='pas', `exe`='exe', `complieOrder`='0', `compileCmd`='C:\\JudgeOnline\\bin\\fpc\\fpc.exe -Sg -dONLINE_JUDGE %PATH%%NAME%.%EXT%', `brush`='pascal', `script`='0', `status`='1' WHERE (`id`='3');
UPDATE `oj`.`program_language` SET `id`='4', `name`='Java', `description`='', `extTime`='0', `extMemory`='8000', `timeFactor`='3', `memoryFactor`='3', `ext`='java', `exe`='class', `complieOrder`='2', `compileCmd`='C:\\JudgeOnline\\bin\\Java\\Java.bat %PATH%', `brush`='java', `script`='0', `status`='1' WHERE (`id`='4');
UPDATE `oj`.`program_language` SET `id`='5', `name`='Python2.7', `description`='', `extTime`='0', `extMemory`='7000', `timeFactor`='4', `memoryFactor`='3', `ext`='py', `exe`='exe', `complieOrder`='1', `compileCmd`='C:\\JudgeOnline\\bin\\Python\\Python.bat %PATH% %NAME% %EXT%', `brush`='python', `script`='0', `status`='1' WHERE (`id`='5');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('6', 'GCC99', '', '0', '996', '1', '1', 'c', 'exe', '1', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('7', 'G++98', '', '0', '996', '1', '1', 'cc', 'exe', '0', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('8', 'G++14', ' ', '0', '996', '1', '1', 'cc', 'exe', '1', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('9', 'G++17', ' ', '0', '996', '1', '1', 'cc', 'exe', '0', 'C:\\', 'cpp', '0', '1');
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('10', 'Python3', ' ', '0', '7000', '4', '3', 'py', 'exe', '1', 'C:\\', 'python', '0', '1');

#2017-7-2 Big UPDATE
INSERT INTO `oj`.`program_language` (`id`, `name`, `description`, `extTime`, `extMemory`, `timeFactor`, `memoryFactor`, `ext`, `exe`, `complieOrder`, `compileCmd`, `brush`, `script`, `status`) VALUES ('11', 'Kotlin', '', '0', '8000', '3', '3', 'kt', 'class', '0', '', 'java', '0', '1');
INSERT INTO `oj`.`role` (`id`, `name`, `description`, `status`) VALUES ('4', 'teacher', 'teacher', '1');
INSERT INTO `oj`.`role_permission` (`id`, `rid`, `pid`) VALUES ('15', '4', '77');
INSERT INTO `oj`.`role_permission` (`id`, `rid`, `pid`) VALUES ('16', '4', '175');
INSERT INTO `oj`.`permission` (`id`, `module`, `type`, `name`, `title`, `parentID`, `status`) VALUES ('175', 'user', '1', 'teacher', '教师权限', '0', '1');
insert into role_permission values(17,4,119);
insert into role_permission values(18,4,122);
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for cprogram_user_info
-- ----------------------------
DROP TABLE IF EXISTS `cprogram_user_info`;
CREATE TABLE `cprogram_user_info` (
  `uid` int(11) NOT NULL,
  `class` varchar(255) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

#2017-12-14
ALTER TABLE `contest_solution`
  ADD COLUMN `balloon`  tinyint(1) NOT NULL DEFAULT 0 AFTER `systemError`;

#2017-12-23
insert into variable (name,stringValue) values('astylePath','/usr/local/bin/astyle');

#2017-12-24
ALTER TABLE `oj`.`solution`
ADD COLUMN `wrong` text NULL AFTER `test`;
ALTER TABLE `oj`.`contest_solution`
ADD COLUMN `wrong` text NULL AFTER `test`;

#2017-12-27
ALTER TABLE `oj`.`contest_problem`
  ADD COLUMN `maxSim` int(3) NOT NULL DEFAULT 100 AFTER `firstBloodTime`;

ALTER TABLE `oj`.`contest_solution`
  ADD COLUMN `sim` int(3) NOT NULL DEFAULT 0 AFTER `systemError`,
  ADD COLUMN `sim_id` int(9) NOT NULL DEFAULT 0 AFTER `sim`;

#2017-02-21
ALTER TABLE `resource`
  ADD COLUMN `os`  varchar(255) NULL DEFAULT 'Windows' AFTER `description`,
  ADD COLUMN `arch`  varchar(255) NULL DEFAULT 'x64' AFTER `os`;

#2019-04-22

DROP TABLE IF EXISTS `cprogram_info`;
CREATE TABLE `cprogram_info` (
  `cid` int(11) NOT NULL,
  `type` enum('HOMEWORK','EXPERIMENT','EXPERIMENT_EXAM','COURSE_EXAM') DEFAULT NULL,
  `commit` text,
  `week` int(11) DEFAULT NULL,
  `lecture` int(11) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO cprogram_info (cid,type,`week`,lecture) (SELECT cid,"HOMEWORK",lockBoardTime,unlockBoardTime from contest where type=5);
INSERT INTO cprogram_info (cid,type) (SELECT cid,"EXPERIMENT" from contest where type=6);
INSERT INTO cprogram_info (cid,type,`week`,lecture) (SELECT cid,"EXPERIMENT_EXAM",lockBoardTime,unlockBoardTime from contest where type=7);
INSERT INTO cprogram_info (cid,type,`week`,lecture) (SELECT cid,"COURSE_EXAM",lockBoardTime,unlockBoardTime from contest where type=8);
UPDATE contest SET type=999 WHERE type >= 5;


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

ALTER TABLE `cprogram_user_info`
CHANGE COLUMN `class` `classes`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `uid`;
#2020-10-08
insert into program_language (id,name,description,extTime,extMemory,timeFactor,memoryFactor,ext,exe,complieOrder,compileCmd,brush,script,status) values (12,"G++20","",0,996,1,1,"cc","exe",1,"","cpp",0,1);
insert into program_language (id,name,description,extTime,extMemory,timeFactor,memoryFactor,ext,exe,complieOrder,compileCmd,brush,script,status) values (13,"GCC18","",0,996,1,1,"c","exe",1,"","c",0,1);
insert into program_language (id,name,description,extTime,extMemory,timeFactor,memoryFactor,ext,exe,complieOrder,compileCmd,brush,script,status) values (14,"Golang","",0,996,1,1,"go","exe",1,"","c",0,1);
