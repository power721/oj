package ${myModel.packageName};

import com.jfinal.plugin.activerecord.Model;

public class ${myModel.modelName} extends Model<${myModel.modelName}>
{
  private static final long serialVersionUID = 1L;
  
  public static final ${myModel.modelName} dao = new ${myModel.modelName}();
  
<#list myModel.columnsNames?keys as key>
  public static final String ${key?upper_case} = "${myModel.columnsNames[key]}";
</#list>

<#list myModel.columnsNames?keys as key>
  public <T> T get${myModel.columnsNames[key]?cap_first}()
  {
    return get(${key?upper_case});
  }
  
  public ${myModel.modelName} set${myModel.columnsNames[key]?cap_first}(Object value)
  {
    return set(${key?upper_case}, value);
  }
  
</#list>
}
