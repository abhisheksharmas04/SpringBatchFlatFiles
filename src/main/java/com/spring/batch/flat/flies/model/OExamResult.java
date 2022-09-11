package com.spring.batch.flat.flies.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class OExamResult {
	
	@Id
	private Integer id;
	private LocalDate dob;
	private Float percentage;
	private Integer sem;

}
