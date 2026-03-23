package spring_boot_batch.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
//import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
//import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


//@RequiredArgsConstructor
@Component
@Slf4j
public class BatchScheduler {

    //private final JobLauncher jobLauncher;
    //private final JobRegistry jobRegistry;
    
	//@Autowired
    //private JobRegistry jobRegistry;
    
    @Autowired
    private JobOperator jobOperator;
    
    @Autowired
    private Job testJob;
    
    /*
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
        processor.setJobRegistry(jobRegistry);
        return processor;
    }
    */

    //@Bean
    //@Lazy
    //public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
        //JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        //jobProcessor.setJobRegistry(jobRegistry);
        //return jobProcessor;
    //}
    
    /*
    @Bean
    @DependsOn("jobRegistryBeanPostProcessor")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/rg?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true")
                .username("rg")
                .password("jiseob9123")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
	*/

    //@Bean
	//public Job testJob(JobRepository jobRepository) {
		//return new JobBuilder("testJob", jobRepository)
				//define job flow as needed
				//.build();
	//}
	
    @Scheduled(cron = "0 0/5 * * * *") // 5분마다 실행
    public void runJob() {
        String time = LocalDateTime.now().toString();
        log.info("time : " + time);
        try {
            //Job job = jobRegistry.getJob("testJob"); // job 이름
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            //jobLauncher.run(job, jobParam.toJobParameters());
            jobOperator.start(testJob, jobParam.toJobParameters());
        //} catch (NoSuchJobException e) {
            //throw new RuntimeException(e);
        //} catch (JobInstanceAlreadyCompleteException |
                 //JobExecutionAlreadyRunningException |
                 //JobParametersInvalidException |
                 //JobRestartException e
        //) {
            //throw new RuntimeException(e);
        //}
        
        } catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
		} catch (JobRestartException e) {
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
		} catch (InvalidJobParametersException e) {
			e.printStackTrace();
		}
    }
}
