package com.cuzer.springbatchadvanced.domain;

import java.util.Date;

public class Employee {
	
	private final int emp_no;
	
	private final Date birth_date;
	
	private final String first_name;
	
	private final String last_name;
	
	private final String gender;
	
	private final Date hire_date;

	public Employee(int emp_no, Date birth_date, String first_name, String last_name, String gender, Date hire_date) {
		this.emp_no = emp_no;
		this.birth_date = birth_date;
		this.first_name = first_name;
		this.last_name = last_name;
		this.gender = gender;
		this.hire_date = hire_date;
	}

	@Override
	public String toString() {
		return "Employee [emp_no=" + emp_no + ", birth_date=" + birth_date + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", gender=" + gender + ", hire_date=" + hire_date + "]";
	}
	
	
	
	

}
