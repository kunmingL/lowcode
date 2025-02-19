package com.changjiang.lowcode.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

/**
 * Freemarker配置类
 * 用于配置模板引擎
 */
@Configuration
public class FreemarkerConfig {

    /**
     * 配置Freemarker模板引擎
     * @return Freemarker配置对象
     */
    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() {
        freemarker.template.Configuration configuration = 
            new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/generator");
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }
}