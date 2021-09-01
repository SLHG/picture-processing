package com.cn.service.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Value("${datasource-master.filters}")
    private String masterFilters;

    @Value("${datasource-master.filter-connectProperties}")
    private String masterConnectionProperties;

    @Bean(name = {"masterSource"})
    @ConfigurationProperties(prefix = "datasource-master")
    public DataSource masterDataSource() throws SQLException {
        DruidDataSource build = DruidDataSourceBuilder.create().build();
        build.setFilters(masterFilters);
        build.setConnectionProperties(masterConnectionProperties);
        return build;
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("masterSource") DataSource masterDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(4);
        targetDataSources.put("master", masterDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
