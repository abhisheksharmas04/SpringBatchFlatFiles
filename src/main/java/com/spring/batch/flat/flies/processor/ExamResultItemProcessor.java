package com.spring.batch.flat.flies.processor;

import java.time.LocalDate;

import org.springframework.batch.item.ItemProcessor;

// Step 2 Create ItemProcessor

import com.spring.batch.flat.flies.document.ExamResult;
import com.spring.batch.flat.flies.model.OExamResult;

public class ExamResultItemProcessor implements ItemProcessor<ExamResult, OExamResult> {

	@Override
	public OExamResult process(ExamResult item) throws Exception {
		OExamResult result = new OExamResult();
		result.setId(item.getId());
		result.setDob(LocalDate.parse(item.getDob()));
		result.setPercentage(item.getPercentage());
		result.setSem(item.getSem());
		
		return result;
	}

}
