package com.changjiang.lowcode.interfaces.web;

import com.changjiang.lowcode.domain.service.GeneratorService;
import com.changjiang.lowcode.interfaces.dto.GeneratorRequest;
import com.changjiang.lowcode.interfaces.dto.MergeRequest;
import com.changjiang.lowcode.interfaces.dto.MergeFileRequest;
import com.changjiang.lowcode.interfaces.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {

    private final GeneratorService generatorService;

    @GetMapping
    public String index() {
        return "generator";
    }

    @PostMapping("/generate")
    @ResponseBody
    public Result generate(@RequestBody GeneratorRequest request) {
        try {
            log.info("接收到代码生成请求，参数: {}", request);
            Result result = generatorService.generate(request);
            log.info("代码生成完成，结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("代码生成失败", e);
            return Result.failure(e.getMessage());
        }
    }

    @PostMapping("/merge")
    @ResponseBody
    public Result merge(@RequestBody MergeRequest request) {
        try {
            log.info("接收到文件合并请求: {}", request);
            // 根据用户选择处理文件合并
            for (MergeFileRequest file : request.getFiles()) {
                log.info("处理文件: {}", file.getPath());
                if (file.isUseOld()) {
                    log.info("保持原文件不变");
                    continue;
                }
                
                if (file.isUseNew()) {
                    log.info("使用新文件覆盖");
                    Files.write(Paths.get(file.getPath()), file.getNewContent().getBytes());
                } else {
                    log.info("使用合并后的内容");
                    Files.write(Paths.get(file.getPath()), file.getMergedContent().getBytes());
                }
            }
            return Result.success();
        } catch (Exception e) {
            log.error("文件合并失败", e);
            return Result.failure(e.getMessage());
        }
    }
}
