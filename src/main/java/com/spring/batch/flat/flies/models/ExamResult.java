package com.spring.batch.flat.flies.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Step 3 : Develop Model Class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResult {
	
	private Integer id;
	private Date dob;
	private double percentage;
	private Integer sem;

}
