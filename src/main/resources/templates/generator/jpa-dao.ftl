package ${package};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ${basePackage}.infrastructure.persistence.po.${names.po};
import org.springframework.stereotype.Repository;

/**
 * ${table.comments!''} 数据访问层
 */
@Repository
public interface ${names.dao} extends JpaRepository<${names.po}, <#list table.columns as column><#if column.primaryKey>${column.javaType}</#if></#list>>, 
                                    JpaSpecificationExecutor<${names.po}> {
    
    // 可以添加自定义查询方法
} 