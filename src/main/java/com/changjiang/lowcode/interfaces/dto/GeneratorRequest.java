package com.changjiang.lowcode.interfaces.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class GeneratorRequest {
    @NotBlank(message = "表名不能为空")
    private String tableName;
    
    @NotBlank(message = "基础包路径不能为空")
    private String basePackage;
    
    @NotBlank(message = "API模块DTO路径不能为空")
    private String apiModulePath;
    
    @NotBlank(message = "Service模块路径不能为空")
    private String serviceModulePath;
    
    @NotBlank(message = "Domain路径不能为空")
    private String domainPath;
    
    @NotBlank(message = "数据访问层路径不能为空")
    private String daoPath;
    
    @NotBlank(message = "PO路径不能为空")
    private String poPath;
    
    @NotBlank(message = "Converter路径不能为空")
    private String converterPath;
    
    @NotBlank(message = "Repository接口路径不能为空")
    private String repositoryPath;
    
    @NotBlank(message = "Repository实现类路径不能为空")
    private String repositoryImplPath;
    
    @NotBlank(message = "MyBatis XML路径不能为空")
    private String mybatisXmlPath;
    
    @NotBlank(message = "DTO-Domain转换器路径不能为空")
    private String dtoDomainMapperPath;
} 