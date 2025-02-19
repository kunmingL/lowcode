package ${package};

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ${basePackage}.api.dto.${names.dto};
import ${basePackage}.domain.model.${names.domain};
import java.util.List;

/**
 * ${table.comments!''} DTO-Domain转换器
 */
@Mapper
public interface ${names.dtoDomainMapper} {
    ${names.dtoDomainMapper} INSTANCE = Mappers.getMapper(${names.dtoDomainMapper}.class);
    
    /**
     * DTO转换为Domain
     */
    ${names.domain} toDomain(${names.dto} dto);
    
    /**
     * Domain转换为DTO
     */
    ${names.dto} toDTO(${names.domain} domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<${names.domain}> toDomainList(List<${names.dto}> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<${names.dto}> toDTOList(List<${names.domain}> domainList);
} 