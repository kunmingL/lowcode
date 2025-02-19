package com.changjiang.lowcode.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import javax.sql.DataSource;

/**
 * 生成器配置类
 */
@Configuration
public class GeneratorConfig {

    /**
     * 配置SqlSessionFactory
     * 用于读取数据库元数据
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        return sessionFactory.getObject();
    }
}
