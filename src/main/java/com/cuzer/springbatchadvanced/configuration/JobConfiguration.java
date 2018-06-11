package com.cuzer.springbatchadvanced.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cuzer.springbatchadvanced.domain.Employee;
import com.cuzer.springbatchadvanced.domain.EmployeeRowMapper;

@Configuration
@Import(DataSourceConfiguration.class)
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
public class JobConfiguration {

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Autowired
	public DataSource bl1datasource;

	@Bean
	public JobParametersIncrementer runIdIncrementer() {
		return new RunIdIncrementer();
	}

	@Bean
	public JdbcPagingItemReader<Employee> jdbcPagingItemReader(@Qualifier("bl1datasource") DataSource dataSource) {
		JdbcPagingItemReader<Employee> jdbcPagingItemReader = new JdbcPagingItemReader<>();

		jdbcPagingItemReader.setDataSource(dataSource);
		jdbcPagingItemReader.setFetchSize(100);
		jdbcPagingItemReader.setRowMapper(new EmployeeRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("emp_no, birth_date, first_name, last_name, gender, hire_date");
		queryProvider.setFromClause("from employees.employees");
		queryProvider.setWhereClause("WHERE first_name = 'Mary'");

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("emp_no", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		jdbcPagingItemReader.setQueryProvider(queryProvider);

		return jdbcPagingItemReader;
	}

	public ItemWriter<Employee> itemWriter() {
		return items -> {
			for (Employee item : items) {
				System.out.println(item.toString());
			}
		};

	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("firstStep").<Employee, Employee>chunk(100)
				.reader(jdbcPagingItemReader(bl1datasource)).writer(itemWriter()).build();
	}

	@Bean
	public Job job1() {
		return jobBuilderFactory.get("springAdvanced").incrementer(runIdIncrementer()).start(step1()).build();
	}

}
