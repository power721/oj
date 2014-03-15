alter table contest change `start_time` `startTime` int(11) DEFAULT '0';
alter table contest change `end_time` `endTime` int(11) DEFAULT '0';
alter table contest change `pass` `password` varchar(255) DEFAULT NULL;
alter table contest change `freeze` `freeze` tinyint(1) NOT NULL DEFAULT '0';

alter table contest_problem change `accept` `accepted` int(5) NOT NULL DEFAULT '0';
alter table contest_problem change `submit` `submission` int(5) NOT NULL DEFAULT '0';
alter table contest_problem change `first_blood` `firstBloodUid` int(9) NOT NULL DEFAULT '0' COMMENT 'first user(uid) solved this problem';
alter table contest_problem change `first_blood_time` `firstBloodTime` int(9) NOT NULL DEFAULT '-1' COMMENT 'first time(minutes) solved this problem';

alter table board change `A_time` `A_SolvedTime` int(11) DEFAULT '0',
alter table board change `A_WrongSubmits` `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `B_time` `B_SolvedTime` int(11) DEFAULT '0',
alter table board change `B_WrongSubmits` `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `C_time` `C_SolvedTime` int(11) DEFAULT '0',
alter table board change `C_WrongSubmits` `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `D_time` `D_SolvedTime` int(11) DEFAULT '0',
alter table board change `D_WrongSubmits` `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `E_time` `E_SolvedTime` int(11) DEFAULT '0',
alter table board change `E_WrongSubmits` `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `F_time` `F_SolvedTime` int(11) DEFAULT '0',
alter table board change `F_WrongSubmits` `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `G_time` `G_SolvedTime` int(11) DEFAULT '0',
alter table board change `G_WrongSubmits` `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `H_time` `H_SolvedTime` int(11) DEFAULT '0',
alter table board change `H_WrongSubmits` `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `I_time` `I_SolvedTime` int(11) DEFAULT '0',
alter table board change `I_WrongSubmits` `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `J_time` `J_SolvedTime` int(11) DEFAULT '0',
alter table board change `J_WrongSubmits` `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `K_time` `K_SolvedTime` int(11) DEFAULT '0',
alter table board change `K_WrongSubmits` `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `L_time` `L_SolvedTime` int(11) DEFAULT '0',
alter table board change `L_WrongSubmits` `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `M_time` `M_SolvedTime` int(11) DEFAULT '0',
alter table board change `M_WrongSubmits` `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `N_time` `N_SolvedTime` int(11) DEFAULT '0',
alter table board change `N_WrongSubmits` `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `O_time` `O_SolvedTime` int(11) DEFAULT '0',
alter table board change `O_WrongSubmits` `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `P_time` `P_SolvedTime` int(11) DEFAULT '0',
alter table board change `P_WrongSubmits` `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `Q_time` `Q_SolvedTime` int(11) DEFAULT '0',
alter table board change `Q_WrongSubmits` `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `R_time` `R_SolvedTime` int(11) DEFAULT '0',
alter table board change `R_WrongSubmits` `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `S_time` `S_SolvedTime` int(11) DEFAULT '0',
alter table board change `S_WrongSubmits` `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `T_time` `T_SolvedTime` int(11) DEFAULT '0',
alter table board change `T_WrongSubmits` `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `U_time` `U_SolvedTime` int(11) DEFAULT '0',
alter table board change `U_WrongSubmits` `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `V_time` `V_SolvedTime` int(11) DEFAULT '0',
alter table board change `V_WrongSubmits` `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `W_time` `W_SolvedTime` int(11) DEFAULT '0',
alter table board change `W_WrongSubmits` `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `X_time` `X_SolvedTime` int(11) DEFAULT '0',
alter table board change `X_WrongSubmits` `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `Y_time` `Y_SolvedTime` int(11) DEFAULT '0',
alter table board change `Y_WrongSubmits` `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table board change `Z_time` `Z_SolvedTime` int(11) DEFAULT '0',
alter table board change `Z_WrongSubmits` `Z_WrongNum` tinyint(5) unsigned DEFAULT '0',

alter table freeze_board change `A_time` `A_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `A_WrongSubmits` `A_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `B_time` `B_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `B_WrongSubmits` `B_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `C_time` `C_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `C_WrongSubmits` `C_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `D_time` `D_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `D_WrongSubmits` `D_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `E_time` `E_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `E_WrongSubmits` `E_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `F_time` `F_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `F_WrongSubmits` `F_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `G_time` `G_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `G_WrongSubmits` `G_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `H_time` `H_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `H_WrongSubmits` `H_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `I_time` `I_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `I_WrongSubmits` `I_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `J_time` `J_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `J_WrongSubmits` `J_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `K_time` `K_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `K_WrongSubmits` `K_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `L_time` `L_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `L_WrongSubmits` `L_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `M_time` `M_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `M_WrongSubmits` `M_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `N_time` `N_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `N_WrongSubmits` `N_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `O_time` `O_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `O_WrongSubmits` `O_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `P_time` `P_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `P_WrongSubmits` `P_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `Q_time` `Q_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `Q_WrongSubmits` `Q_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `R_time` `R_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `R_WrongSubmits` `R_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `S_time` `S_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `S_WrongSubmits` `S_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `T_time` `T_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `T_WrongSubmits` `T_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `U_time` `U_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `U_WrongSubmits` `U_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `V_time` `V_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `V_WrongSubmits` `V_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `W_time` `W_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `W_WrongSubmits` `W_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `X_time` `X_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `X_WrongSubmits` `X_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `Y_time` `Y_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `Y_WrongSubmits` `Y_WrongNum` tinyint(5) unsigned DEFAULT '0',
alter table freeze_board change `Z_time` `Z_SolvedTime` int(11) DEFAULT '0',
alter table freeze_board change `Z_WrongSubmits` `Z_WrongNum` tinyint(5) unsigned DEFAULT '0',

alter table friend change `user`  `userId` int(9) NOT NULL;
alter table friend change `friend`  `friendUid` int(9) NOT NULL;

alter table level change `exp` `experience` int(9) NOT NULL;

ALTER TABLE loginlog DROP `password`;
ALTER TABLE loginlog DROP `info`;

alter table mail change `user` `userId` int(9) NOT NULL COMMENT 'user id';
alter table mail change `peer` `peerUid` int(9) NOT NULL COMMENT 'peer uid';

alter table mail_banlist change `user` `uid` int(9) NOT NULL;
alter table mail_banlist change `ban_user` `bannedUid` int(9) NOT NULL;

alter table mail_content change `from` `fromUid` int(9) NOT NULL;
alter table mail_content change `to` `toUid` int(9) NOT NULL;

alter table notice change `editor` `editorUid` int(9) DEFAULT NULL;
alter table notice change `start_time` `startTime` int(11) NOT NULL;
alter table notice change `end_time` `endTime` int(11) NOT NULL;

ALTER TABLE problem DROP `in_path`;
ALTER TABLE problem DROP `out_path`;
alter table problem change `sample_Program` `sampleProgram` text,
alter table problem change `sample_input` `sampleInput` text,
alter table problem change `sample_output` `sampleOutput` text,
alter table problem change `time_limit` `timeLimit` int(5) NOT NULL DEFAULT '1000',
alter table problem change `memory_limit` `memoryLimit` int(5) NOT NULL DEFAULT '65536',
alter table problem change `accept` `accepted` int(5) NOT NULL DEFAULT '0',
alter table problem change `submit` `submission` int(5) NOT NULL DEFAULT '0',
alter table problem change `submit_user` `submitUser` int(5) NOT NULL DEFAULT '0',

alter table problem_language change `ext_time` `extTime` int(9) NOT NULL DEFAULT '0',
alter table problem_language change `ext_memory` `extMemory` int(9) NOT NULL DEFAULT '0',
alter table problem_language change `time_factor` `timeFactor` tinyint(3) NOT NULL DEFAULT '1',
alter table problem_language change `memory_factor` `memoryFactor` tinyint(3) NOT NULL DEFAULT '1',
alter table problem_language change `complie_order` `complieOrder` tinyint(3) NOT NULL DEFAULT '0',
alter table problem_language change `compile_cmd` `compileCmd` varchar(255) NOT NULL,

alter table session change `session_id` `sessionId` varchar(40) NOT NULL DEFAULT '0',
alter table session change `ip_address` `ipAddress` varchar(45) NOT NULL DEFAULT '0',
alter table session change `user_agent` `userAgent` varchar(255) DEFAULT '',
alter table session change `last_activity` `lastActivity` int(11) unsigned NOT NULL DEFAULT '0',
alter table session change `session_expires` `sessionExpires` int(11) NOT NULL,

alter table solution change `code_len` `codeLen` int(9) NOT NULL DEFAULT '0',
alter table solution change `system_error` `systemError` text,

alter table team change `girl_team` `isGirlTeam` tinyint(1) NOT NULL DEFAULT '0',
alter table team change `new_team` `isRookieTeam` tinyint(1) NOT NULL DEFAULT '0',
alter table team change `sp_team` `isSpecialTeam` tinyint(1) NOT NULL DEFAULT '0',

alter table user change `pass` `password` varchar(128) NOT NULL COMMENT 'User’s password (hashed).',
alter table user change `realname` `realName` varchar(35) DEFAULT NULL,
alter table user change `reg_email` `regEmail` varchar(64) NOT NULL,
alter table user change `email_verified` `emailVerified` tinyint(1) NOT NULL DEFAULT '0',
alter table user change `accept` `accepted` int(6) NOT NULL DEFAULT '0',
alter table user change `submit` `submission` int(6) NOT NULL DEFAULT '0' COMMENT 'the number of user submit code',
alter table user change `login` `loginTime` int(11) NOT NULL DEFAULT '0' COMMENT 'Timestamp for user last login.',
alter table user change `login_P` `loginIP` varchar(64) DEFAULT NULL,
alter table user change `comefrom` `comeFrom` varchar(35) DEFAULT NULL,
alter table user change `sign` `signature` varchar(255) DEFAULT NULL,
alter table user change `share` `shareCode` tinyint(1) NOT NULL DEFAULT '0',

alter table user_ext change `exp` `experience` int(9) NOT NULL DEFAULT '0',
alter table user_ext change `realname` `realName` varchar(35) DEFAULT NULL,
alter table user_ext change `share` `shareCode` tinyint(1) NOT NULL DEFAULT '0',
alter table user_ext change `checkin_times` `checkinTimes` tinyint(5) NOT NULL DEFAULT '0',
alter table user_ext change `total_checkin` `totalCheckin` int(9) NOT NULL DEFAULT '0',
alter table user_ext change `last_send_drift` `lastSendDrift` int(11) NOT NULL DEFAULT '0',
alter table user_ext change `send_drift` `sendDriftNum` tinyint(3) NOT NULL DEFAULT '0',
alter table user_ext change `last_get_drift` `lastGetDrift` int(11) NOT NULL DEFAULT '0',
alter table user_ext change `get_drift` `getDriftNum` tinyint(3) NOT NULL DEFAULT '0',

alter table variable change `value` `stringValue` varchar(255) DEFAULT NULL;