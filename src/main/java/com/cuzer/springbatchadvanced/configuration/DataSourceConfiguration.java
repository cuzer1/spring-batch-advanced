package com.cuzer.springbatchadvanced.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class DataSourceConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties(prefix="app.datasource.ds3")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties(prefix="app.datasource.ds2")
	public DataSource bl1datasource() {
		return DataSourceBuilder.create().build();
	}
	
}
