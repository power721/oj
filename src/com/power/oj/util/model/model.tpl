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
  <#assign name=myModel.columnsNames[key]?cap_first >
  <#if name?starts_with("Is")>
  <#assign setName=name?substring(2)>
  public Boolean ${name}()
  {
    return getBoolean(${key?upper_case});
  }
  
  public ${myModel.modelName} set${setName}(Boolean value)
  {
    return set(${key?upper_case}, value);
  }
  <#else>
  public <T> T get${name}()
  {
    return get(${key?upper_case});
  }
  
  public ${myModel.modelName} set${name}(Object value)
  {
    return set(${key?upper_case}, value);
  }
  </#if>
  
</#list>
}
