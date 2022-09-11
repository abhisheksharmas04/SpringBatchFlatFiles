package com.spring.batch.flat.flies.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.spring.batch.flat.flies.document.ExamResult;
import com.spring.batch.flat.flies.listener.JobMonitoringListener;
import com.spring.batch.flat.flies.processor.ExamResultItemProcessor;

// Step 4

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	// Step 5 Inject JobBuilderFactory
	@Autowired private JobBuilderFactory jobFactory;
		
	// Step 6 Inject StepBuilderFactory
	@Autowired private StepBuilderFactory stepFactory;
	
	// step 7 Inject MongoTemplate
	@Autowired private MongoTemplate mongoTemplate;
	
	//Step 8 create Listener
	@Bean
	public JobExecutionListener createListener() {
		return new JobMonitoringListener();
	}
	
	//Step 9 create Processor
	@Bean
	public ExamResultItemProcessor createProcessor() {
		return new ExamResultItemProcessor();
	}
	
	//Step 10 Create FlatFileItemReader
	@Bean
	public FlatFileItemReader<ExamResult>createReader(){
		FlatFileItemReader<ExamResult> reader = new FlatFileItemReader<>();
		
		reader.setResource(new FileSystemResource("D:\\batch\\top-brains.csv"));
		reader.setLineMapper(new DefaultLineMapper<ExamResult>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setDelimiter(DELIMITER_COMMA);
				setNames("id","dob","percentage","sem");
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<ExamResult>() {{
				setTargetType(ExamResult.class);
			}});
		}});
		
		return reader;
	}
	
	//Step 11 Create MongoWriter
	@Bean
	public MongoItemWriter<ExamResult> createWriter(){
		MongoItemWriter<ExamResult>writer = new MongoItemWriter<>();
		writer.setCollection("super-brains");
		writer.setTemplate(mongoTemplate);
		
		return writer;
	}
	
	//Step 12 create Step
	@Bean(name = "step1")
	public Step createStep1() {
		return stepFactory.get("step1")
				.<ExamResult,ExamResult>chunk(3)
				.reader(createReader())
				.writer(createWriter())
				.processor(createProcessor())
				.build();
	}
	
	// Step 13 create Job
	@Bean(name = "job1")
	public Job createJob1() {
		return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(createListener())
				.start(createStep1())
				.build();
	}
	

}
