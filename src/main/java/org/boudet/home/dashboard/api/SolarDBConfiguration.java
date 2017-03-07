package org.boudet.home.dashboard.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SolarDBConfiguration {

    @Bean(name = "solarDataSourceProperties")
    @ConfigurationProperties("solar.db")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "solarDBDataSource")
    @ConfigurationProperties("solar.db")
    public DataSource dataSource(@Qualifier("solarDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().create().build();
    }

    @Bean(name = "solarDBJdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource solarDBDataSource) {
        return new JdbcTemplate(solarDBDataSource);
    }
}