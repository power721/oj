package com.power.oj.core.model;

import java.io.Serializable;
import java.util.List;
import com.jfinal.kit.JsonKit;

/**
 * datatables模型对象
 * 
 * @author huxiang
 * 
 */
public class DataTablesModel implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = -7248682808131659142L;

  /**
   * 当前页号
   */
  private int page;

  /**
   * 每页显示记录数
   */
  private int rp;

  /**
   * 总记录数
   */
  private int total;

  /**
   * 返回记录的id数组
   */
  private List<String> ids;

  /**
   * 返回的数据
   */
  private List<List<String>> rows;

  public DataTablesModel(int page, int rp, int total, List<String> ids, List<List<String>> rows)
  {
    this.setPage(page);
    this.setRp(rp);
    this.setTotal(total);
    this.setIds(ids);
    this.setRows(rows);
  }

  /**
   * @return the page
   */
  public int getPage()
  {
    return page;
  }

  /**
   * @param page
   *          the page to set
   */
  public void setPage(int page)
  {
    this.page = page;
  }

  /**
   * @return the rp
   */
  public int getRp()
  {
    return rp;
  }

  /**
   * @param rp
   *          the rp to set
   */
  public void setRp(int rp)
  {
    this.rp = rp;
  }

  /**
   * @return the total
   */
  public int getTotal()
  {
    return total;
  }

  /**
   * @param total
   *          the total to set
   */
  public void setTotal(int total)
  {
    this.total = total;
  }

  public List<String> getIds()
  {
    return ids;
  }

  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }

  public List<List<String>> getRows()
  {
    return rows;
  }

  public void setRows(List<List<String>> rows)
  {
    this.rows = rows;
  }

  public String toJSONString()
  {
    return JsonKit.toJson(this);
  }

}
