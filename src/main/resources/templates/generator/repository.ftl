package ${package};

import ${basePackage}.domain.model.${names.domain};
import java.util.List;

public interface ${names.repository} {
    ${names.domain} findById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>);
    
    List<${names.domain}> findAll();
    
    void save(${names.domain} entity);
    
    void update(${names.domain} entity);
    
    void deleteById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>);
} 