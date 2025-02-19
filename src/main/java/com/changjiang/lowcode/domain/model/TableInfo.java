package com.changjiang.lowcode.domain.model;

import lombok.Data;
import java.util.List;

@Data
public class TableInfo {
    private String tableName;
    private String className;
    private String comments;
    private List<ColumnInfo> columns;
} 