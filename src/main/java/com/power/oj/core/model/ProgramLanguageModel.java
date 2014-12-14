package com.power.oj.core.model;

import com.jfinal.plugin.activerecord.Model;

public class ProgramLanguageModel extends Model<ProgramLanguageModel> {
	private static final long serialVersionUID = 1L;

	public static final ProgramLanguageModel dao = new ProgramLanguageModel();

	public static final String TABLE_NAME = "program_language";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String EXT_TIME = "extTime";
	public static final String EXT_MEMORY = "extMemory";
	public static final String TIME_FACTOR = "timeFactor";
	public static final String MEMORY_FACTOR = "memoryFactor";
	public static final String EXT = "ext";
	public static final String EXE = "exe";
	public static final String COMPLIE_ORDER = "complieOrder";
	public static final String COMPILE_CMD = "compileCmd";
	public static final String BRUSH = "brush";
	public static final String SCRIPT = "script";
	public static final String STATUS = "status";

	public Integer getId() {
		return getInt(ID);
	}

	public ProgramLanguageModel setId(Integer value) {
		return set(ID, value);
	}

	public String getName() {
		return getStr(NAME);
	}

	public ProgramLanguageModel setName(String value) {
		return set(NAME, value);
	}

	public String getDescription() {
		return getStr(DESCRIPTION);
	}

	public ProgramLanguageModel setDescription(String value) {
		return set(DESCRIPTION, value);
	}

	public Integer getExtTime() {
		return getInt(EXT_TIME);
	}

	public ProgramLanguageModel setExtTime(Integer value) {
		return set(EXT_TIME, value);
	}

	public Integer getExtMemory() {
		return getInt(EXT_MEMORY);
	}

	public ProgramLanguageModel setExtMemory(Integer value) {
		return set(EXT_MEMORY, value);
	}

	public Integer getTimeFactor() {
		return getInt(TIME_FACTOR);
	}

	public ProgramLanguageModel setTimeFactor(Integer value) {
		return set(TIME_FACTOR, value);
	}

	public Integer getMemoryFactor() {
		return getInt(MEMORY_FACTOR);
	}

	public ProgramLanguageModel setMemoryFactor(Integer value) {
		return set(MEMORY_FACTOR, value);
	}

	public String getExt() {
		return getStr(EXT);
	}

	public ProgramLanguageModel setExt(String value) {
		return set(EXT, value);
	}

	public String getExe() {
		return getStr(EXE);
	}

	public ProgramLanguageModel setExe(String value) {
		return set(EXE, value);
	}

	public Integer getComplieOrder() {
		return getInt(COMPLIE_ORDER);
	}

	public ProgramLanguageModel setComplieOrder(Integer value) {
		return set(COMPLIE_ORDER, value);
	}

	public String getCompileCmd() {
		return getStr(COMPILE_CMD);
	}

	public ProgramLanguageModel setCompileCmd(String value) {
		return set(COMPILE_CMD, value);
	}

	public String getBrush() {
		return getStr(BRUSH);
	}

	public ProgramLanguageModel setBrush(String value) {
		return set(BRUSH, value);
	}

	public Boolean getScript() {
		return getBoolean(SCRIPT);
	}

	public ProgramLanguageModel setScript(Boolean value) {
		return set(SCRIPT, value);
	}

	public Boolean getStatus() {
		return getBoolean(STATUS);
	}

	public ProgramLanguageModel setStatus(Boolean value) {
		return set(STATUS, value);
	}

}
