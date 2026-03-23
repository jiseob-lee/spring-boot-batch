package spring_boot_batch.batch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Configuration
//@RequiredArgsConstructor
//@Component
@Slf4j
@EnableBatchProcessing
public class BatchConfig {
	
	//private final JobRegistry jobRegistry;
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/rg?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true");
        dataSource.setUsername("rg");
        dataSource.setPassword("jiseob9123");
        return dataSource;
    }
    
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
	    return new DataSourceTransactionManager(dataSource);
	}
	
    @Bean
    public Job testJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) throws DuplicateJobException {
       
    	Job job = new JobBuilder("testJob",jobRepository)
               .start(testStep(jobRepository,transactionManager))
               .build();
       
       //ReferenceJobFactory factory = new ReferenceJobFactory(job);
       //jobRegistry.register(factory);
       
       //출처: https://meteorkor.tistory.com/87 [Meteor:티스토리]
       return job;
    }

    @Bean
    public Step testStep(JobRepository jobRepository,PlatformTransactionManager transactionManager){
        Step step = new StepBuilder("testStep",jobRepository)
                .tasklet(testTasklet(null),transactionManager)
                .build();
        return step;
    }

    @Bean
    @StepScope
    public Tasklet testTasklet(@Value("#{jobParameters[time]}") String time) {
        return ((contribution, chunkContext) -> {

        	LocalDateTime now = LocalDateTime.now();
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        	String formattedDate = now.format(formatter);
        	
        	log.info(formattedDate + " : ***** hello batch! ***** : " + time);
        	
        	// 원하는 비지니스 로직 작성
        	
        	return RepeatStatus.FINISHED;
        });
    }
}
