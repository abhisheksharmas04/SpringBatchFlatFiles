package com.spring.batch.flat.flies.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBToCsvRunner implements CommandLineRunner {
	
	//Step 12 Create Runner class to run the Application
	
	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job job;
	
	@Override
	public void run(String... args) throws Exception {
		JobParameters parameters = new JobParametersBuilder()
				.addLong("time",System.currentTimeMillis())
				.toJobParameters();
		
		try {
			JobExecution execution = launcher.run(job, parameters);
			System.out.println("Job Compltion Status: " + execution.getStatus());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		

	}

}
