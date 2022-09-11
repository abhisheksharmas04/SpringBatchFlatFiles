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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.spring.batch.flat.flies.listener.JobMonitoringListener;
import com.spring.batch.flat.flies.model.Employee;
import com.spring.batch.flat.flies.processor.EmployeeInfoItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private DataSource ds;
	// STEP 8 Inject JobBuilderFactory, StepBuilderFactory, to BatchConfig using @Autowire
	@Autowired
	private JobBuilderFactory jobFactory;

	@Autowired
	private StepBuilderFactory stepFactory;
	// STEP 8 END

	// Develop listener as a bean
	// STEP 9 Configure JobExecutionListener as spring bean using @Bean method
	@Bean
	public JobExecutionListener createListener() {
		return new JobMonitoringListener();
	}
	//STEP 9 END

	// Develop ItemProcessor as a bean
	//STEP7: Configure ItemProcessor in BatchConfig using @Bean Method
	@Bean
	public ItemProcessor<Employee, Employee> createProcessor() {
		return new EmployeeInfoItemProcessor();
	}
	

	//Developing ItemReader with Builders
	// STEP 6 Configure ItemReader
	/*@Bean(name = "reader")
	public ItemReader<Employee> createReader(){
		ClassPathResource resource = new ClassPathResource("employee_info.csv");
		System.out.println(resource.getFilename());
		return new FlatFileItemReaderBuilder<Employee>()
			.name("file-reader")
			.resource(new ClassPathResource("employee_info.csv"))
			.delimited().delimiter(",") // Makes us to use DefaultLineMapper, DefaultLineTokensizer
			.names("empno","ename","firstName","middleName","lastName","gender","email","salary")
			.targetType(Employee.class)
			.build();
	}*/
	
	/*@Bean Recomended Code 
	public ItemReader<Employee> createReader(){
		// create object for FlatFileItemWriter
		FlatFileItemReader<Employee>reader = new FlatFileItemReader<>();
		
		//set source csv file location
		reader.setResource(new ClassPathResource("employee_info.csv"));
		//reader.setResource(new FileSystemResource(GIVE YOUR FILE LOACTION));
		//reader.setResource(new UrlResource(GIVE FILE LOCATION ON INTERNET));
		
		// set Line Mapper
		reader.setLineMapper(new DefaultLineMapper<Employee>() {instance block starting{
			// set LineTokenizer
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setDelimiter(DELIMITER_COMMA);
				setNames("empno","Ename","FirstName","LastName","Gender","EMail","Salary");
			}});
			//set FieldSetMapper to write each line content to Model class object
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
				setTargetType(Employee.class);
			}});
		}});
		return reader;
	}
	*/
	
	// code without using the annonsym sub class
	@Bean
	public ItemReader<Employee> createReader() {
		// create object for FlatFileItemWriter
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
	
		// set source csv file location
		reader.setResource(new ClassPathResource("employee_info.csv"));
		
		//Line Mapper
		DefaultLineMapper<Employee>lineMapper = new DefaultLineMapper<>();
		
		//Line Tokenizer
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames("empno","ename","firstName","middleName","lastName","gender","email","salary");
		
		//FielSetMapper
		BeanWrapperFieldSetMapper<Employee>fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employee.class);
		
		// add line tokenizer, FileSetMapper to LineMapper
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		//add line mapper to Reader
		reader.setLineMapper(lineMapper);
		
		return reader;
	}
	// STEP 6 END
	
	// STEP 11 Configure ItemWriter
	/*@Bean(name="writer")
	public ItemWriter<Employee> createItemWriter(){
		JdbcBatchItemWriter<Employee>writer = new JdbcBatchItemWriter<>();
		//set datasoruce to decide target data
		writer.setDataSource(ds);
		
		// set sql query with named param
		writer.setSql("INSERT INTO BATCH_EMPLOYEE VALUES(:empno, :ename,:firstName,:middleName,:lastName,:gender,:email,:salary,:netSalary)");
		
		// set Model class object as sql paramter source provider(Here named param names and model class properties names must match)
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		return writer;
	}*/
	
	// Through Builder
	@Bean(name="writer")
	public ItemWriter<Employee> createItemWriter(){
		return new JdbcBatchItemWriterBuilder<Employee>()
			.dataSource(ds)
			.sql("INSERT INTO BATCH_EMPLOYEE VALUES(:empno, :ename,:firstName,:middleName,:lastName,:gender,:email,:salary,:netSalary)")
			.beanMapped() // This internally use BeanPropertyItemSqlParameterSourceProvider
			.build();
	}
	
	// STEP 12-- Create step object, Job object as spring beans
	@Bean(name = "step1")
	public Step createStep1() {
		return stepFactory.get("step1")
				.<Employee,Employee>chunk(3)
				.reader(createReader())
				.writer(createItemWriter())
				.processor(createProcessor())
				.build();
	}
	
	@Bean(name = "job1")
	public Job createJob1(){
		return jobFactory.get("job1")
		.incrementer(new RunIdIncrementer())
		.listener(createListener())
		.start(createStep1())
		.build();
		/*return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(createListener())
				.start(createStep1())
				.build();*/
	}
	
	
}
