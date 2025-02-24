package ${package};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${basePackage}.domain.model.${names.domain};
import ${basePackage}.domain.repository.${names.repository};
import ${basePackage}.infrastructure.converter.${names.converter};
import ${basePackage}.infrastructure.persistence.po.${names.po};
import ${basePackage}.infrastructure.dao.${names.mapper};
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * ${table.comments!''} 仓储实现类
 */
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
    public List<${names.domain}> findByCondition(${names.domain} condition) {
        ${names.po} po = converter.toPO(condition);
        List<${names.po}> poList = ${table.className?uncap_first}Mapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<${names.domain}> findByPage(int page, int size, ${names.domain} condition) {
        Page<${names.po}> pageParam = new Page<>(page, size);
        ${names.po} po = converter.toPO(condition);
        
        IPage<${names.po}> poPage = ${table.className?uncap_first}Mapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<${names.domain}> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
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