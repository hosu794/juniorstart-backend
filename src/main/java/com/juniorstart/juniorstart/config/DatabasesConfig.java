package com.juniorstart.juniorstart.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Adrian Stypiński
 * This class is responsible for creating multiple databases for one application instance
 *
 */
@Configuration
@EnableTransactionManagement
public class DatabasesConfig {

    // Primary database
    @Bean
    @Primary
    @ConfigurationProperties("entities.datasource")
    public DataSourceProperties entitiesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("entities.datasource")
    public DataSource entitiesDataSource() {
        return entitiesDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    // Log database
    @Bean
    @ConfigurationProperties("logs.datasource")
    public DataSourceProperties logsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("logs.datasource")
    public DataSource logsDataSource() {
        return entitiesDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
