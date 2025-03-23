package ${package};

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * ${table.comments!''}
 */
@Data
@TableName("${table.tableName}")
public class ${names.po} {
<#list table.columns as column>
    /**
     * ${column.comments!''}
     */
    <#if column.primaryKey>
    @TableId("${column.columnName}")
    <#else>
    @TableField("${column.columnName}")
    </#if>
    private ${column.javaType} ${column.fieldName};
</#list>
} 