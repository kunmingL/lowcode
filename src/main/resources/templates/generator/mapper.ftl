package ${package};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${basePackage}.infrastructure.persistence.po.${names.po};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ${names.mapper} extends BaseMapper<${names.po}> {
    // 基础的CRUD方法由 BaseMapper 提供
} 