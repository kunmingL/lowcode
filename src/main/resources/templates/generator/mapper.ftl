package ${package};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${basePackage}.infrastructure.persistence.po.${names.po};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * ${table.comments!''} 数据访问接口
 */
@Mapper
public interface ${names.mapper} extends BaseMapper<${names.po}> {
    /**
     * 根据条件查询列表
     * @param condition 查询条件
     * @return 实体列表
     */
    List<${names.po}> selectByCondition(@Param("condition") ${names.po} condition);
    
    /**
     * 根据条件分页查询
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<${names.po}> selectPage(IPage<${names.po}> page, @Param("condition") ${names.po} condition);
} 