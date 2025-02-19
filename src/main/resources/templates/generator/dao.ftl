package ${package};

import org.springframework.stereotype.Repository;
import ${basePackage}.infrastructure.persistence.po.${table.className}PO;
import ${basePackage}.infrastructure.mapper.${table.className}Mapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * ${table.comments} DAO层实现
 */
@Repository
public class ${table.className}DAO extends ServiceImpl<${table.className}Mapper, ${table.className}PO> {
    // 可以添加自定义的数据访问方法
} 