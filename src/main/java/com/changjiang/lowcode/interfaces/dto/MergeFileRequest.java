package com.changjiang.lowcode.interfaces.dto;

import lombok.Data;

@Data
public class MergeFileRequest {
    private String path;
    private boolean useOld;
    private boolean useNew;
    private String mergedContent;
    private String newContent;
} 