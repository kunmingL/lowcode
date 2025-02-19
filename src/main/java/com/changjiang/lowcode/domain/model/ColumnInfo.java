package com.changjiang.lowcode.domain.model;

import lombok.Data;

@Data
public class ColumnInfo {
    private String columnName;
    private String fieldName;
    private String dataType;
    private String javaType;
    private String comments;
    private boolean primaryKey;
    private int columnSize;
    private boolean nullable;
} 