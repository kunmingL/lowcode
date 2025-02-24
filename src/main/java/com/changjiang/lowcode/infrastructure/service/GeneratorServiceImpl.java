package com.changjiang.lowcode.infrastructure.service;

import com.changjiang.lowcode.domain.model.TableInfo;
import com.changjiang.lowcode.domain.service.GeneratorService;
import com.changjiang.lowcode.interfaces.dto.GeneratorRequest;
import com.changjiang.lowcode.interfaces.dto.Result;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.io.StringWriter;

/**
 * 代码生成服务实现类
 * 负责生成DDD架构下的各层代码
 */
@Slf4j  // 添加Lombok的日志注解
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
    private final DatabaseService databaseService;
    private final Configuration freemarkerConfig;
    private final FileDiffService fileDiffService;
    private GeneratorRequest request;
    
    /**
     * 生成代码
     * @param request 生成请求参数
     * @return 生成结果
     */
    @Override
    public Result generate(GeneratorRequest request) {
        try {
            this.request = request;
            log.info("开始生成代码，表名：{}，包名：{}", request.getTableName(), request.getBasePackage());
            
            TableInfo tableInfo = databaseService.getTableInfo(request.getTableName());
            log.info("获取到表信息：{}", tableInfo);
            
            String basePackage = request.getBasePackage();
            
            // 检查文件是否存在并比较差异
            List<FileDiffService.DiffResult> diffs = new ArrayList<>();
            checkAndCompareDiff(diffs, request, tableInfo, basePackage);
            
            // 如果有差异文件，返回差异信息让用户确认
            if (!diffs.isEmpty()) {
                log.info("发现{}个文件需要确认", diffs.size());
                return Result.needMerge(diffs);
            }
            
            // 如果没有差异或文件不存在，直接生成
            log.info("没有发现需要确认的文件，开始生成代码");
            generateFiles(tableInfo, basePackage);
            return Result.success();
        } catch (Exception e) {
            log.error("代码生成失败", e);
            return Result.failure(e.getMessage());
        }
    }
    
    private void checkAndCompareDiff(List<FileDiffService.DiffResult> diffs, 
                                   GeneratorRequest request, 
                                   TableInfo tableInfo, 
                                   String basePackage) throws Exception {
        // 检查所有需要生成的文件
        checkFileDiff(diffs, "dto.ftl", 
            request.getApiModulePath() + "/" + tableInfo.getClassName() + "DTO.java",
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "domain.ftl", 
            request.getDomainPath() + "/" + tableInfo.getClassName() + ".java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "mapper.ftl", 
            request.getDaoPath() + "/" + tableInfo.getClassName() + "Mapper.java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "po.ftl", 
            request.getPoPath() + "/" + tableInfo.getClassName() + "PO.java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "converter.ftl", 
            request.getConverterPath() + "/" + tableInfo.getClassName() + "Converter.java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "repository.ftl", 
            request.getRepositoryPath() + "/" + tableInfo.getClassName() + "Repository.java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "repository-impl.ftl", 
            request.getRepositoryImplPath() + "/" + tableInfo.getClassName() + "RepositoryImpl.java", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "mapper.xml.ftl", 
            request.getMybatisXmlPath() + "/" + tableInfo.getClassName() + "Mapper.xml", 
            tableInfo, basePackage);
        
        checkFileDiff(diffs, "dto-domain-mapper.ftl", 
            request.getDtoDomainMapperPath() + "/" + tableInfo.getClassName() + "DTOMapper.java", 
            tableInfo, basePackage);
    }
    
    private void checkFileDiff(List<FileDiffService.DiffResult> diffs,
                             String templateName,
                             String fullPath,
                             TableInfo tableInfo,
                             String basePackage) throws Exception {
        File file = new File(fullPath);
        if (file.exists()) {
            log.info("检查文件: {}", fullPath);
            // 读取现有文件内容
            String oldContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            
            // 生成新内容
            Map<String, String> names = getClassNames(tableInfo);
            Map<String, Object> model = new HashMap<>();
            model.put("package", getPackagePath(templateName, basePackage));
            model.put("basePackage", basePackage);
            model.put("table", tableInfo);
            model.put("names", names);
            String newContent = processTemplateToString(templateName, model);
            
            // 只有当内容不同时才添加到差异列表
            if (!oldContent.equals(newContent)) {
                log.info("文件 {} 存在差异", fullPath);
                diffs.add(fileDiffService.compareFiles(fullPath, oldContent, newContent));
            } else {
                log.info("文件 {} 内容相同，无需更新", fullPath);
            }
        } else {
            log.info("文件 {} 不存在，将直接生成", fullPath);
        }
    }
    
    private String processTemplateToString(String templateName, Map<String, Object> model) 
        throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return writer.toString();
    }
    
    private Map<String, String> getClassNames(TableInfo tableInfo) {
        Map<String, String> names = new HashMap<>();
        String baseName = tableInfo.getClassName();  // 例如：UserInfo
        
        // 各层对象的命名规则
        names.put("dto", baseName + "DTO");         // UserInfoDTO
        names.put("domain", baseName);              // UserInfo
        names.put("po", baseName + "PO");          // UserInfoPO
        names.put("mapper", baseName + "Mapper");   // UserInfoMapper
        names.put("converter", baseName + "Converter"); // UserInfoConverter
        names.put("repository", baseName + "Repository"); // UserInfoRepository
        names.put("repositoryImpl", baseName + "RepositoryImpl"); // UserInfoRepositoryImpl
        names.put("dtoDomainMapper", baseName + "DTOMapper");  // 添加 DTO-Domain 转换器命名
        
        return names;
    }
    
    private void generateDto(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".api.dto");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);  // 添加统一的命名映射
        
        String fileName = names.get("dto") + ".java";
        String fullPath = request.getApiModulePath() + "/" + fileName;
        processTemplate("dto.ftl", model, fullPath);
    }
    
    private void generateDomain(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".domain.model");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);  // 添加统一的命名映射
        
        String fileName = names.get("domain") + ".java";
        String fullPath = request.getDomainPath() + "/" + fileName;
        processTemplate("domain.ftl", model, fullPath);
    }
    
    private void generateDao(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.dao");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        
        String fileName = tableInfo.getClassName() + "DAO.java";
        String fullPath = request.getDaoPath() + "/" + fileName;
        processTemplate("dao.ftl", model, fullPath);
    }
    
    private void generatePo(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.persistence.po");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("po") + ".java";
        String fullPath = request.getPoPath() + "/" + fileName;
        processTemplate("po.ftl", model, fullPath);
    }
    
    private void generateMapper(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.dao");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("mapper") + ".java";
        String fullPath = request.getDaoPath() + "/" + fileName;
        processTemplate("mapper.ftl", model, fullPath);
    }
    
    private void generateConverter(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.converter");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("converter") + ".java";
        String fullPath = request.getConverterPath() + "/" + fileName;
        processTemplate("converter.ftl", model, fullPath);
    }
    
    private void generateRepository(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        
        // 生成 Repository 接口
        model.put("package", basePackage + ".domain.repository");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("repository") + ".java";
        String fullPath = request.getRepositoryPath() + "/" + fileName;
        processTemplate("repository.ftl", model, fullPath);
    }
    
    private void generateRepositoryImpl(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        
        // 生成 Repository 实现类
        model.put("package", basePackage + ".infrastructure.repository");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("repositoryImpl") + ".java";
        String fullPath = request.getRepositoryImplPath() + "/" + fileName;
        processTemplate("repository-impl.ftl", model, fullPath);
    }
    
    private void generateMybatisXml(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.mapper");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("mapper") + ".xml";
        String fullPath = request.getMybatisXmlPath() + "/" + fileName;
        processTemplate("mapper.xml.ftl", model, fullPath);
    }
    
    private void generateDtoDomainMapper(TableInfo tableInfo, String basePackage) throws Exception {
        Map<String, String> names = getClassNames(tableInfo);
        Map<String, Object> model = new HashMap<>();
        model.put("package", basePackage + ".infrastructure.converter.dto");
        model.put("basePackage", basePackage);
        model.put("table", tableInfo);
        model.put("names", names);
        
        String fileName = names.get("dtoDomainMapper") + ".java";
        String fullPath = request.getDtoDomainMapperPath() + "/" + fileName;
        processTemplate("dto-domain-mapper.ftl", model, fullPath);
    }
    
    /**
     * 处理模板生成代码文件
     * @param templateName 模板名称
     * @param model 模板数据模型
     * @param fullPath 输出路径
     */
    private void processTemplate(String templateName, Map<String, Object> model, String fullPath) 
        throws Exception {
        log.info("开始处理模板: {}, 输出路径: {}", templateName, fullPath);
        
        Template template = freemarkerConfig.getTemplate(templateName);
        
        File outputFile = new File(fullPath);
        File parentDir = outputFile.getParentFile();
        
        if (!parentDir.exists()) {
            log.info("创建目录: {}", parentDir.getAbsolutePath());
            if (!parentDir.mkdirs()) {
                throw new IOException("无法创建目录: " + parentDir.getAbsolutePath());
            }
        }
        
        if (!parentDir.canWrite()) {
            throw new IOException("没有写入权限: " + parentDir.getAbsolutePath());
        }
        
        log.info("写入文件: {}", outputFile.getAbsolutePath());
        try (Writer writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
            template.process(model, writer);
        }
        
        if (!outputFile.exists()) {
            throw new IOException("文件生成失败: " + outputFile.getAbsolutePath());
        }
        
        log.info("文件生成成功: {}", outputFile.getAbsolutePath());
    }
    
    private void generateFiles(TableInfo tableInfo, String basePackage) throws Exception {
        log.info("开始生成所有文件...");
        try {
            // 生成所有文件
            generateDto(tableInfo, basePackage);
            generateDomain(tableInfo, basePackage);
            generateDtoDomainMapper(tableInfo, basePackage);
            generateMapper(tableInfo, basePackage);
            generatePo(tableInfo, basePackage);
            generateConverter(tableInfo, basePackage);
            generateRepository(tableInfo, basePackage);
            generateRepositoryImpl(tableInfo, basePackage);
            generateMybatisXml(tableInfo, basePackage);
            
            log.info("所有文件生成完成");
        } catch (Exception e) {
            log.error("文件生成失败", e);
            throw new RuntimeException("文件生成失败: " + e.getMessage(), e);
        }
    }

    private String getPackagePath(String templateName, String basePackage) {
        switch (templateName) {
            case "dto.ftl":
                return basePackage + ".api.dto";
            case "domain.ftl":
                return basePackage + ".domain.model";
            case "mapper.ftl":
                return basePackage + ".infrastructure.dao";
            case "po.ftl":
                return basePackage + ".infrastructure.persistence.po";
            case "converter.ftl":
                return basePackage + ".infrastructure.converter";
            case "repository.ftl":
                return basePackage + ".domain.repository";
            case "repository-impl.ftl":
                return basePackage + ".infrastructure.repository";
            case "dto-domain-mapper.ftl":
                return basePackage + ".infrastructure.converter.dto";
            default:
                return basePackage + ".infrastructure.dao";
        }
    }
} 