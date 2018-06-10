package com.cuzer.springbatchadvanced.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee>{

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Employee(
						rs.getInt("emp_no")
					   ,rs.getDate("birth_date")
					   ,rs.getString("first_name")
					   ,rs.getString("last_name")
					   ,rs.getString("gender")
					   ,rs.getDate("hire_date")
				);
	}
  
}
