package com.power.oj.core.bean;

// actual class should extend Model
public interface Solution {

	public Integer getSid();

	public Solution setSid(Integer value);

	public Integer getUid();

	public Solution setUid(Integer value);

	public Integer getPid();

	public Solution setPid(Integer value);

	public Integer getCid();

	public Solution setCid(Integer value);

	public Integer getNum();

	public Solution setNum(Integer value);

	public Integer getTime();

	public Solution setTime(Integer value);

	public Integer getMemory();

	public Solution setMemory(Integer value);

	public Integer getResult();

	public Solution setResult(Integer value);

	public Integer getLanguage();

	public Solution setLanguage(Integer value);

	public String getSource();

	public Solution setSource(String value);

	public Integer getCodeLen();

	public Solution setCodeLen(Integer value);

	public String getError();

	public Solution setError(String value);

	public String getSystemError();

	public Solution setSystemError(String value);

	public Integer getCtime();

	public Solution setCtime(Integer value);

	public Integer getMtime();

	public Solution setMtime(Integer value);

	public Integer getTest();

	public Solution setTest(Integer value);

	public Boolean getStatus();

	public Solution setStatus(Boolean value);

}
