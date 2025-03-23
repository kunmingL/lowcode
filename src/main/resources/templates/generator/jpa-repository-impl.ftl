package ${package};

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ${basePackage}.domain.model.${names.domain};
import ${basePackage}.domain.repository.${names.repository};
import ${basePackage}.infrastructure.converter.${names.converter};
import ${basePackage}.infrastructure.persistence.po.${names.po};
import ${daoPackage}.${names.dao};
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

/**
 * ${table.comments!''} 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class ${names.repositoryImpl} implements ${names.repository} {
    private final ${names.dao} ${table.className?uncap_first}Dao;
    private final ${names.converter} converter = ${names.converter}.INSTANCE;
    
    @Override
    public ${names.domain} findById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>) {
        Optional<${names.po}> poOpt = ${table.className?uncap_first}Dao.findById(<#list table.columns as column><#if column.primaryKey>${column.fieldName}</#if></#list>);
        return poOpt.map(converter::toDomain).orElse(null);
    }
    
    @Override
    public List<${names.domain}> findAll() {
        List<${names.po}> poList = ${table.className?uncap_first}Dao.findAll();
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<${names.domain}> findByCondition(${names.domain} condition) {
        ${names.po} po = converter.toPO(condition);
        Specification<${names.po}> spec = buildSpecification(po);
        List<${names.po}> poList = ${table.className?uncap_first}Dao.findAll(spec);
        return converter.toDomainList(poList);
    }
    
    @Override
    public Page<${names.domain}> findByPage(int page, int size, ${names.domain} condition) {
        ${names.po} po = converter.toPO(condition);
        Specification<${names.po}> spec = buildSpecification(po);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<${names.po}> poPage = ${table.className?uncap_first}Dao.findAll(spec, pageable);
        return poPage.map(converter::toDomain);
    }
    
    @Override
    public void save(${names.domain} entity) {
        ${names.po} po = converter.toPO(entity);
        ${table.className?uncap_first}Dao.save(po);
    }
    
    @Override
    public void update(${names.domain} entity) {
        ${names.po} po = converter.toPO(entity);
        ${table.className?uncap_first}Dao.save(po);
    }
    
    @Override
    public void deleteById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>) {
        ${table.className?uncap_first}Dao.deleteById(<#list table.columns as column><#if column.primaryKey>${column.fieldName}</#if></#list>);
    }
    
    /**
     * 构建查询条件
     */
    private Specification<${names.po}> buildSpecification(${names.po} condition) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            <#list table.columns as column>
            <#if column.javaType == "String">
            if (condition.get${column.fieldName?cap_first}() != null && !condition.get${column.fieldName?cap_first}().isEmpty()) {
                predicates.add(cb.like(root.get("${column.fieldName}"), "%" + condition.get${column.fieldName?cap_first}() + "%"));
            }
            <#else>
            if (condition.get${column.fieldName?cap_first}() != null) {
                predicates.add(cb.equal(root.get("${column.fieldName}"), condition.get${column.fieldName?cap_first}()));
            }
            </#if>
            </#list>
            
            if (predicates.isEmpty()) {
                return null;
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 