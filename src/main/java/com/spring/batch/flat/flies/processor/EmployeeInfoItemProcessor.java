package com.spring.batch.flat.flies.processor;

import org.springframework.batch.item.ItemProcessor;

import com.spring.batch.flat.flies.model.Employee;

public class EmployeeInfoItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee employee) throws Exception {
		/*if(employee.getSalary() >= 100000) {
			employee.setNetSalary(Math.round(employee.getSalary() + employee.getSalary() * 0.4f));
			return employee;
		}else { 
			return null; // the null object given by ItemProcessor will not go to item writer
		}*//*else {
			employee.setNetSalary(Math.round(employee.getSalary()));
			}*/
		employee.setNetSalary(Math.round(employee.getSalary() + employee.getSalary() * 0.4f));
		
		return employee;
	}

}
