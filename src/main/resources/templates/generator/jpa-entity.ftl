package ${package};

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * ${table.comments!''}
 */
@Data
@Entity
@Table(name = "${table.tableName}")
public class ${names.po} {
<#list table.columns as column>
    /**
     * ${column.comments!''}
     */
    <#if column.primaryKey>
    @Id
    <#if column.dataType?contains("INT") || column.dataType?contains("BIGINT")>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    </#if>
    @Column(name = "${column.columnName}"<#if !column.nullable>, nullable = false</#if><#if column.javaType == "String" && column.columnSize gt 0>, length = ${column.columnSize}</#if>)
    private ${column.javaType} ${column.fieldName};
</#list>
} 