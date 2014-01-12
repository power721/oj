package com.power.oj.core.bean;

public class ResultType
{
  public static final int AC = 0;
  public static final int PE = 1;
  public static final int TLE = 2;
  public static final int MLE = 3;
  public static final int WA = 4;
  public static final int RE = 5;
  public static final int OLE = 6;
  public static final int CE = 7;
  public static final int SE = 98;
  public static final int VE = 99;
  public static final int WAIT = 10000;

  private int id;
  private String name;
  private String longName;

  public ResultType()
  {

  }

  public ResultType(int id, String name, String longName)
  {
    this.id = id;
    this.name = name;
    this.longName = longName;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getLongName()
  {
    return longName;
  }

  public int setId(int id)
  {
    this.id = id;
    return this.id;
  }

  public String setName(String name)
  {
    this.name = name;
    return this.name;
  }

  public String setLongName(String longName)
  {
    this.longName = longName;
    return this.longName;
  }
}

/*
 * public class ResultType {
 * 
 * public static final int AC = 0; public static final int PE = 1; public static
 * final int TLE = 2; public static final int MLE = 3; public static final int
 * WA = 4; public static final int RE = 5; public static final int OLE = 6;
 * public static final int CE = 7; public static final int SE = 98; public
 * static final int VE = 99; public static final int Wait = 10000;
 * 
 * private static boolean inited = false; public static final IntHashMap
 * resultName = new IntHashMap(); public static final IntHashMap resultLongName
 * = new IntHashMap();
 * 
 * public ResultType() {
 * 
 * }
 * 
 * public static void init() { if(inited) return; resultName.put(AC, "AC");
 * resultName.put(PE, "PE"); resultName.put(TLE, "TLE"); resultName.put(MLE,
 * "MLE"); resultName.put(WA, "WA"); resultName.put(RE, "RE");
 * resultName.put(OLE, "OLE"); resultName.put(CE, "CE"); resultName.put(SE,
 * "SE"); resultName.put(VE, "VE"); resultName.put(Wait, "Wait");
 * 
 * resultLongName.put(AC, "Accepted"); resultLongName.put(PE,
 * "Presentation Error"); resultLongName.put(TLE, "Time Limit Exceed");
 * resultLongName.put(MLE, "Memory Limit Exceed"); resultLongName.put(WA,
 * "Wrong Answer"); resultLongName.put(RE, "Runtime Error");
 * resultLongName.put(OLE, "Output Limit Exceed"); resultLongName.put(CE,
 * "Compile Error"); resultLongName.put(SE, "System Error");
 * resultLongName.put(VE, "Validate Error"); resultLongName.put(Wait,
 * "Waiting");
 * 
 * inited = true; }
 * 
 * public static String getResultName(int id) { if(resultName.containsKey(id))
 * return (String) resultName.get(id); return "Other"; }
 * 
 * public static String getResultLongName(int id) {
 * if(resultLongName.containsKey(id)) return (String) resultLongName.get(id);
 * return "Other"; } }
 */
