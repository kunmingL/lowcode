<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package}.${table.className}Mapper">
    
    <resultMap id="BaseResultMap" type="${basePackage}.infrastructure.persistence.po.${table.className}PO">
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
    
    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        WHERE 
        <#list table.columns as column>
            <#if column.primaryKey>
            ${column.columnName} = <#noparse>#{</#noparse>${column.fieldName}<#noparse>}</#noparse>
            </#if>
        </#list>
    </select>
    
    <select id="selectList" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        <where>
            <if test="ew != null">
                <#noparse>${ew.sqlSegment}</#noparse>
            </if>
        </where>
    </select>
    
    <select id="selectPage" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        <where>
            <if test="ew != null">
                <#noparse>${ew.sqlSegment}</#noparse>
            </if>
        </where>
    </select>
    
    <insert id="insert" parameterType="${basePackage}.infrastructure.persistence.po.${table.className}PO">
        INSERT INTO ${table.tableName} (
            <include refid="Base_Column_List"/>
        )
        VALUES (
        <#list table.columns as column>
            <#noparse>#{</#noparse>${column.fieldName}<#noparse>}</#noparse><#if column_has_next>, </#if>
        </#list>
        )
    </insert>
    
    <update id="updateById" parameterType="${basePackage}.infrastructure.persistence.po.${table.className}PO">
        UPDATE ${table.tableName}
        <set>
        <#list table.columns as column>
            <#if !column.primaryKey>
            ${column.columnName} = <#noparse>#{</#noparse>${column.fieldName}<#noparse>}</#noparse><#if column_has_next>,</#if>
            </#if>
        </#list>
        </set>
        WHERE 
        <#list table.columns as column>
            <#if column.primaryKey>
            ${column.columnName} = <#noparse>#{</#noparse>${column.fieldName}<#noparse>}</#noparse>
            </#if>
        </#list>
    </update>
    
    <delete id="deleteById">
        DELETE FROM ${table.tableName}
        WHERE 
        <#list table.columns as column>
            <#if column.primaryKey>
            ${column.columnName} = <#noparse>#{</#noparse>${column.fieldName}<#noparse>}</#noparse>
            </#if>
        </#list>
    </delete>
</mapper> 