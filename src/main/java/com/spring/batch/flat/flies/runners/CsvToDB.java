package com.spring.batch.flat.flies.runners;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Step 12 develop runner class to run the job using JobLaucher Object
@Component
public class CsvToDB implements CommandLineRunner {
	
	@Autowired
	private JobLauncher laucher;
	
	@Autowired
	private Job job;

	@Override
	public void run(String... args) throws Exception {
		JobParameters params = new JobParametersBuilder().
				addLong("systime",System.currentTimeMillis()).toJobParameters();
		
		JobExecution execution = laucher.run(job, params);
		System.out.println("Execution Status: " + execution.getStatus());

	}

}
