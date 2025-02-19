package com.changjiang.lowcode.interfaces.dto;

import lombok.Data;
import java.util.List;

@Data
public class MergeRequest {
    private List<MergeFileRequest> files;
} 