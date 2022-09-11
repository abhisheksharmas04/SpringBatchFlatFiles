package com.spring.batch.flat.flies.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.spring.batch.flat.flies.listener.JobMonitoringListener;
import com.spring.batch.flat.flies.mapper.ExamResultRowMapper;
import com.spring.batch.flat.flies.models.ExamResult;
import com.spring.batch.flat.flies.processor.ExamResultItemProcessor;

// Step 6 Develop BatchConfig class

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFacotry;
	
	// Step 7 Create Listener
	@Bean
	public JobExecutionListener createListener() {
		return new JobMonitoringListener();
	}
	
	// Step 8 Create Processor
	@Bean
	public ItemProcessor<ExamResult,ExamResult> createProcessor(){
		return new ExamResultItemProcessor();
	}
	
	// Step 9 Create Reader
	@Bean
	public JdbcCursorItemReader<ExamResult> createReader(){
		// create object
		JdbcCursorItemReader<ExamResult> reader = new JdbcCursorItemReader<>();
		
		// specify data source:
		reader.setDataSource(ds);
		
		// specify sql query
		reader.setSql("SELECT ID, DOB, PERCENTAGE, SEM FROM EXAMRESULT");
		
		reader.setRowMapper(new ExamResultRowMapper());
		
		// Syntax using Lambda expression
		/*reader.setRowMapper((rs,rowNumber)->new ExamResult(
				rs.getInt(1),rs.getDate(2),rs.getDouble(3),rs.getInt(4)));*/
		
		return reader;
	}
	
	// Create Reader using Builder
	/*public JdbcCursorItemReader<ExamResult> createReader1(){
		return new JdbcCursorItemReaderBuilder<ExamResult>()
				.name("logical-name")
				.dataSource(ds)
				.sql("SELECT ID, DOB, PERCENTAGE, SEM FROM EXAM_RESULT")
				.beanRowMapper(ExamResult.class) // Internally Uses BeanPropertyRowMapper to convert the record of RS to given model class object but DB coloums name and Entity class property names must matched.
				.build();
	}*/
	//Step 9 END
	
	//Step 10 Create FlatFileItemWriter
	/*public FlatFileItemWriter<ExamResult> createWriter(){
		FlatFileItemWriter<ExamResult>writer = new FlatFileItemWriter<>();
		// give the csv file location
		writer.setResource(new ClassPathResource("top-brains.csv"));
		// specify line aggrigator by suppliying delimeter and field extractor
		writer.setLineAggregator(new DelimitedLineAggregator() {{
			setDelimiter(",");
			.setFieldExtractor(new BeanWrapperFieldExtractor() {{
				// specify names to extracted field values
				setNames(new String[] {"id","dob","percentage","sem"});
			}});
		}});
		
		return writer;
	}*/
	// Using builder
	@Bean
	public FlatFileItemWriter<ExamResult> createWriter(){
		return new FlatFileItemWriterBuilder<ExamResult>()
				.name("line123")
				.resource(new FileSystemResource("D:\\batch\\top-brains.csv"))
				.lineSeparator("\r\n")
				.delimited().delimiter(",")
				.names("id","dob","percentage","sem")
				.build();
	}
	// STEP 10 END
	
	//Step 11 Create Step Object
	@Bean(name = "step1")
	public Step createStep1() {
		return stepFacotry.get("step1")
				.<ExamResult,ExamResult>chunk(3)
				.reader(createReader())
				.writer(createWriter())
				.processor(createProcessor())
				.build();
	}
	
	//Step12 Create Job Object
	@Bean(name = "job1")
	public Job createJob1() {
		return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(createListener())
				.start(createStep1())
				.build();
	}
}
