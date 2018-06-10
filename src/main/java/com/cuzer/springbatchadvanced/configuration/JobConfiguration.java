package com.cuzer.springbatchadvanced.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cuzer.springbatchadvanced.domain.Employee;
import com.cuzer.springbatchadvanced.domain.EmployeeRowMapper;

@Configuration
public class JobConfiguration {

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public DataSource dataSource1;

	// @Bean
	// @ConfigurationProperties(prefix="app.ds2")
	// public DataSource dataSource1() {
	// return DataSourceBuilder.create().build();
	// }
	//

	@Bean
	public JdbcPagingItemReader<Employee> jdbcPagingItemReader(DataSource dataSource1) {
		JdbcPagingItemReader<Employee> jdbcPagingItemReader = new JdbcPagingItemReader<>();

		jdbcPagingItemReader.setDataSource(dataSource1);
		jdbcPagingItemReader.setFetchSize(100);
		jdbcPagingItemReader.setRowMapper(new EmployeeRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("emp_no, birth_date, first_name, last_name, gender, hire_date");
		queryProvider.setFromClause("from testDB.employees");
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

	public Step step1() {
		return stepBuilderFactory.get("step1234").<Employee, Employee>chunk(100).reader(jdbcPagingItemReader(dataSource1))
				.writer(itemWriter()).build();
	}

	@Bean
	public Job job1() {
		return jobBuilderFactory.get("job12345").start(step1()).build();
	}

}
