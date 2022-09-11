package com.spring.batch.flat.flies.model;

import lombok.Data;

@Data
public class Employee {

	private String empno;
	private String ename;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private String email;
	private Long salary;
	private Integer netSalary;
}
