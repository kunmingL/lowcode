package com.changjiang.lowcode.infrastructure.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import java.nio.file.*;

@Service
public class FileDiffService {
    
    @Data
    public static class DiffResult {
        private String path;
        private String oldContent;
        private String newContent;
        private List<DiffBlock> diffBlocks = new ArrayList<>();
    }
    
    @Data
    public static class DiffBlock {
        private int oldStart;
        private int oldLength;
        private int newStart;
        private int newLength;
        private List<DiffLine> lines = new ArrayList<>();
    }
    
    @Data
    public static class DiffLine {
        private DiffType type;
        private String content;
        private int oldNumber;
        private int newNumber;
        
        public DiffLine(DiffType type, String content, int oldNumber, int newNumber) {
            this.type = type;
            this.content = content;
            this.oldNumber = oldNumber;
            this.newNumber = newNumber;
        }
    }
    
    public enum DiffType {
        CONTEXT, // 上下文
        ADDED,   // 新增
        REMOVED  // 删除
    }
    
    public DiffResult compareFiles(String path, String oldContent, String newContent) {
        DiffResult result = new DiffResult();
        result.setPath(path);
        result.setOldContent(oldContent);
        result.setNewContent(newContent);
        
        String[] oldLines = oldContent.split("\n");
        String[] newLines = newContent.split("\n");
        
        // 使用最长公共子序列算法计算差异
        List<DiffBlock> blocks = computeDiff(oldLines, newLines);
        result.setDiffBlocks(blocks);
        
        return result;
    }
    
    private List<DiffBlock> computeDiff(String[] oldLines, String[] newLines) {
        // 这里实现差异计算算法
        // 可以使用开源库如google-diff-match-patch或者自己实现简单的差异算法
        // 为了演示，这里使用一个简化版本
        List<DiffBlock> blocks = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < oldLines.length || j < newLines.length) {
            DiffBlock block = new DiffBlock();
            block.setOldStart(i);
            block.setNewStart(j);
            
            // 找到不同的行
            while (i < oldLines.length && j < newLines.length && 
                   oldLines[i].equals(newLines[j])) {
                block.getLines().add(new DiffLine(DiffType.CONTEXT, oldLines[i], i+1, j+1));
                i++;
                j++;
            }
            
            // 处理删除的行
            while (i < oldLines.length && (j >= newLines.length || 
                   !oldLines[i].equals(newLines[j]))) {
                block.getLines().add(new DiffLine(DiffType.REMOVED, oldLines[i], i+1, -1));
                i++;
            }
            
            // 处理新增的行
            while (j < newLines.length && (i >= oldLines.length || 
                   !oldLines[i].equals(newLines[j]))) {
                block.getLines().add(new DiffLine(DiffType.ADDED, newLines[j], -1, j+1));
                j++;
            }
            
            if (!block.getLines().isEmpty()) {
                blocks.add(block);
            }
        }
        
        return blocks;
    }
} 