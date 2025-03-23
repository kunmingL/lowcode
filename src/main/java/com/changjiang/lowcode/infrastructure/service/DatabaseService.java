package com.changjiang.lowcode.infrastructure.service;

import com.changjiang.lowcode.domain.model.ColumnInfo;
import com.changjiang.lowcode.domain.model.TableInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库服务类
 * 负责读取数据库表结构信息
 */
@Service
@RequiredArgsConstructor
public class DatabaseService {
    private final DataSource dataSource;
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);
    
    /**
     * 将数据库字段名转换为Java属性名（驼峰命名，首字母小写）
     */
    private String convertToCamelCase(String columnName) {
        // 先将字段名转为小写，并按下划线分割
        String[] parts = columnName.toLowerCase().split("_");
        StringBuilder result = new StringBuilder(parts[0]); // 第一部分保持小写
        
        // 从第二部分开始，首字母大写
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].length() > 0) {
                result.append(Character.toUpperCase(parts[i].charAt(0)))
                      .append(parts[i].substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }

    /**
     * 将表名转换为类名（驼峰命名，首字母大写）
     */
    private String convertToClassName(String tableName) {
        // 移除表名前缀（如 t_ 或 tbl_）
        String nameWithoutPrefix = tableName.replaceFirst("^(t_|tbl_)", "");
        
        // 转换为驼峰命名
        String[] parts = nameWithoutPrefix.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        
        // 所有部分的首字母都大写
        for (String part : parts) {
            if (part.length() > 0) {
                result.append(Character.toUpperCase(part.charAt(0)))
                      .append(part.substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
    
    /**
     * 获取表信息
     * @param tableName 表名
     * @return 表信息对象
     */
    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setClassName(convertToClassName(tableName));
        
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // 获取表注释
            try (ResultSet rs = metaData.getTables(null, null, tableName, null)) {
                if (rs.next()) {
                    tableInfo.setComments(rs.getString("REMARKS"));
                }
            }
            
            // 获取主键信息
            Set<String> primaryKeys = new HashSet<>();
            try (ResultSet rs = metaData.getPrimaryKeys(null, null, tableName)) {
                while (rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"));
                }
            }
            
            // 获取列信息
            List<ColumnInfo> columns = new ArrayList<>();
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                while (rs.next()) {
                    ColumnInfo column = new ColumnInfo();
                    String columnName = rs.getString("COLUMN_NAME");
                    column.setColumnName(columnName);
                    column.setFieldName(convertToCamelCase(columnName));
                    column.setDataType(rs.getString("TYPE_NAME"));
                    column.setJavaType(convertToJavaType(rs.getInt("DATA_TYPE")));
                    column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                    column.setComments(rs.getString("REMARKS"));
                    column.setPrimaryKey(primaryKeys.contains(columnName));
                    column.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                    columns.add(column);
                }
            }
            tableInfo.setColumns(columns);
            
            log.info("获取到表信息: {}", tableInfo);
            return tableInfo;
        } catch (SQLException e) {
            log.error("获取表信息失败", e);
            throw new RuntimeException("获取表信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 转换为Java类型
     * @param sqlType SQL类型代码
     * @return Java类型名称
     */
    private String convertToJavaType(int sqlType) {
        switch (sqlType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return "String";
            case Types.NUMERIC:
            case Types.DECIMAL:
                return "BigDecimal";
            case Types.BIT:
                return "Boolean";
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return "Integer";
            case Types.BIGINT:
                return "Long";
            case Types.REAL:
                return "Float";
            case Types.FLOAT:
            case Types.DOUBLE:
                return "Double";
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return "byte[]";
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return "LocalDateTime";
            default:
                return "String";
        }
    }
} 