package ${package};

import lombok.Data;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * ${table.comments!''}
 */
@Data
public class ${names.dto} {
<#list table.columns as column>
    /**
     * ${column.comments!''}
     */
    <#if column.javaType == "String">
    @Size(max = ${column.columnSize})
    </#if>
    private ${column.javaType} ${column.fieldName};
</#list>
} 