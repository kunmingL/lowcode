package ${package};

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ${basePackage}.domain.model.${names.domain};
import ${basePackage}.infrastructure.persistence.po.${names.po};
import java.util.List;

@Mapper
public interface ${names.converter} {
    ${names.converter} INSTANCE = Mappers.getMapper(${names.converter}.class);
    
    ${names.domain} toDomain(${names.po} po);
    
    ${names.po} toPO(${names.domain} domain);
    
    List<${names.domain}> toDomainList(List<${names.po}> poList);
    
    List<${names.po}> toPOList(List<${names.domain}> domainList);
} 