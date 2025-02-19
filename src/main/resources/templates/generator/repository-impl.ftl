package ${package};

import ${basePackage}.domain.model.${names.domain};
import ${basePackage}.domain.repository.${names.repository};
import ${basePackage}.infrastructure.converter.${names.converter};
import ${basePackage}.infrastructure.persistence.po.${names.po};
import ${basePackage}.infrastructure.dao.${names.mapper};
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ${names.repositoryImpl} implements ${names.repository} {
    private final ${names.mapper} ${table.className?uncap_first}Mapper;
    private final ${names.converter} converter = ${names.converter}.INSTANCE;
    
    @Override
    public ${names.domain} findById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>) {
        ${names.po} po = ${table.className?uncap_first}Mapper.selectById(<#list table.columns as column><#if column.primaryKey>${column.fieldName}</#if></#list>);
        return converter.toDomain(po);
    }
    
    @Override
    public List<${names.domain}> findAll() {
        List<${names.po}> poList = ${table.className?uncap_first}Mapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public void save(${names.domain} entity) {
        ${names.po} po = converter.toPO(entity);
        ${table.className?uncap_first}Mapper.insert(po);
    }
    
    @Override
    public void update(${names.domain} entity) {
        ${names.po} po = converter.toPO(entity);
        ${table.className?uncap_first}Mapper.updateById(po);
    }
    
    @Override
    public void deleteById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>) {
        ${table.className?uncap_first}Mapper.deleteById(<#list table.columns as column><#if column.primaryKey>${column.fieldName}</#if></#list>);
    }
} 