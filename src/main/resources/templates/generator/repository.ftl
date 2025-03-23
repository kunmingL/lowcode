package ${package};

import ${basePackage}.domain.model.${names.domain};
<#if framework == "jpa">
import org.springframework.data.domain.Page;
<#else>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
</#if>
import java.util.List;

/**
 * ${table.comments!''} 仓储接口
 */
public interface ${names.repository} {
    /**
     * 根据ID查询
     */
    ${names.domain} findById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>);
    
    /**
     * 查询所有
     */
    List<${names.domain}> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<${names.domain}> findByCondition(${names.domain} condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    <#if framework == "jpa">
    Page<${names.domain}> findByPage(int page, int size, ${names.domain} condition);
    <#else>
    IPage<${names.domain}> findByPage(int page, int size, ${names.domain} condition);
    </#if>
    
    /**
     * 保存
     */
    void save(${names.domain} entity);
    
    /**
     * 更新
     */
    void update(${names.domain} entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(<#list table.columns as column><#if column.primaryKey>${column.javaType} ${column.fieldName}</#if></#list>);
} 