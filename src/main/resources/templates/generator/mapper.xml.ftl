<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${names.mapper}">
    
    <resultMap id="BaseResultMap" type="${basePackage}.infrastructure.persistence.po.${names.po}">
    <#list table.columns as column>
        <#if column.primaryKey>
        <id column="${column.columnName}" property="${column.fieldName}"/>
        <#else>
        <result column="${column.columnName}" property="${column.fieldName}"/>
        </#if>
    </#list>
    </resultMap>
    
    <sql id="Base_Column_List">
        <#list table.columns as column>${column.columnName}<#if column_has_next>, </#if></#list>
    </sql>
    
    <!-- 动态查询条件 -->
    <sql id="Condition_Where_Clause">
        <where>
        <#list table.columns as column>
            <#if column.javaType == "String">
            <if test="condition.${column.fieldName} != null and condition.${column.fieldName} != ''">
                AND ${column.columnName} LIKE CONCAT('%', <#noparse>#{condition.</#noparse>${column.fieldName}<#noparse>}</#noparse>, '%')
            </if>
            <#else>
            <if test="condition.${column.fieldName} != null">
                AND ${column.columnName} = <#noparse>#{condition.</#noparse>${column.fieldName}<#noparse>}</#noparse>
            </if>
            </#if>
        </#list>
        </where>
    </sql>
    
    <!-- 条件查询 -->
    <select id="selectByCondition" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        <include refid="Condition_Where_Clause"/>
    </select>
    
    <!-- 分页条件查询 -->
    <select id="selectPage" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        <include refid="Condition_Where_Clause"/>
    </select>
    
    <!-- 基础的增删改查由 MyBatis-Plus 提供 -->
</mapper> 