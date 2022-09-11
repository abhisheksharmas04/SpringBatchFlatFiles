package com.spring.batch.flat.flies.processor;

import org.springframework.batch.item.ItemProcessor;

// step 5 develop ItemProcessor

import com.spring.batch.flat.flies.models.ExamResult;

public class ExamResultItemProcessor implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult examResult) throws Exception {
		if(examResult.getPercentage() >= 75)
			return examResult;
		else
			return null;
	}

}
