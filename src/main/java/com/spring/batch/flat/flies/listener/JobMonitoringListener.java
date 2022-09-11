package com.spring.batch.flat.flies.listener;

import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobMonitoringListener implements JobExecutionListener {
	
	private Long start, end;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		start = System.currentTimeMillis();
		System.out.println("Job started at: " + new Date());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		end = System.currentTimeMillis();
		System.out.println("Job completed at : " + new Date());
		System.out.println("Job execution time: " + (end - start));
		System.out.println("Job completion status: " + jobExecution.getStatus());
	}

}
