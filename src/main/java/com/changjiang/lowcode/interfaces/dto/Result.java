package com.changjiang.lowcode.interfaces.dto;

import lombok.Data;
import java.util.List;
import com.changjiang.lowcode.infrastructure.service.FileDiffService;

@Data
public class Result {
    private boolean success;
    private String message;
    private boolean needMerge;
    private List<FileDiffService.DiffResult> diffs;

    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    public static Result failure(String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public static Result needMerge(List<FileDiffService.DiffResult> diffs) {
        Result result = new Result();
        result.setSuccess(false);
        result.setNeedMerge(true);
        result.setDiffs(diffs);
        return result;
    }
} 