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
     * 获取表信息
     * @param tableName 表名
     * @return 表信息对象
     */
    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setClassName(convertToCamelCase(tableName.replaceFirst("^t_", "")));
        
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
            try (ResultSet pkRs = metaData.getPrimaryKeys(null, null, tableName)) {
                while (pkRs.next()) {
                    primaryKeys.add(pkRs.getString("COLUMN_NAME"));
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
     * 转换为驼峰命名
     * @param name 原始名称
     * @return 驼峰命名的字符串
     */
    private String convertToCamelCase(String name) {
        // 移除表名前缀 t_ 并转换为驼峰命名
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (char c : name.toLowerCase().toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else if (result.length() == 0) {
                    // 首字母大写
                    result.append(Character.toUpperCase(c));
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
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
                return "LocalDate";
            case Types.TIME:
                return "LocalTime";
            case Types.TIMESTAMP:
                return "LocalDateTime";
            default:
                return "String";
        }
    }
} 