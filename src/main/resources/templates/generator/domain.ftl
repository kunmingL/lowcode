package ${package};

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * ${table.comments!''}
 */
@Data
public class ${names.domain} {
<#list table.columns as column>
    /**
     * ${column.comments!''}
     */
    private ${column.javaType} ${column.fieldName};
</#list>
} 