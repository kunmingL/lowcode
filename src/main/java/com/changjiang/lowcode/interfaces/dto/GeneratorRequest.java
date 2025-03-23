package com.changjiang.lowcode.interfaces.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    private String mybatisXmlPath;
    
    @NotBlank(message = "DTO-Domain转换器路径不能为空")
    private String dtoDomainMapperPath;
    
    // 添加框架选择字段
    private String framework = "mybatis"; // 默认为mybatis

    // 添加日志方法
    @Override
    public String toString() {
        return "GeneratorRequest{" +
                "tableName='" + tableName + '\'' +
                ", basePackage='" + basePackage + '\'' +
                ", apiModulePath='" + apiModulePath + '\'' +
                ", serviceModulePath='" + serviceModulePath + '\'' +
                ", domainPath='" + domainPath + '\'' +
                ", daoPath='" + daoPath + '\'' +
                ", poPath='" + poPath + '\'' +
                ", converterPath='" + converterPath + '\'' +
                ", repositoryPath='" + repositoryPath + '\'' +
                ", repositoryImplPath='" + repositoryImplPath + '\'' +
                ", mybatisXmlPath='" + mybatisXmlPath + '\'' +
                ", dtoDomainMapperPath='" + dtoDomainMapperPath + '\'' +
                ", framework='" + framework + '\'' +
                '}';
    }
} 