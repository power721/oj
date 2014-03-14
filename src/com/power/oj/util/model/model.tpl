package ${myModel.packageName};

import com.jfinal.plugin.activerecord.Model;

public class ${myModel.modelName} extends Model<${myModel.modelName}>
{
  private static final long serialVersionUID = 1L;
  
  public static final ${myModel.modelName} dao = new ${myModel.modelName}();
  
<#list myModel.columns as column>
  public static final String ${column.field?upper_case} = "${column.name}";
</#list>

<#list myModel.columns as column>
  <#assign name=column.name?cap_first >
  <#if name?starts_with("Is")>
  <#assign setName=name?substring(2)>
  public Boolean ${name}()
  {
    return getBoolean(${column.field?upper_case});
  }
  
  public ${myModel.modelName} set${setName}(Boolean value)
  {
    return set(${column.field?upper_case}, value);
  }
  <#else>
  public <T> T get${name}()
  {
    return get(${column.field?upper_case});
  }
  
  public ${myModel.modelName} set${name}(Object value)
  {
    return set(${column.field?upper_case}, value);
  }
  </#if>
  
</#list>
}
