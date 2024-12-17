package com.daengdaeng_eodiga.project.user.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class UserScheduler {
	private final JobLauncher jobLauncher;
	private final Job deleteUser;

	@Autowired
	public UserScheduler(@Qualifier(value = "jobLauncher") JobLauncher jobLauncher,
						 @Qualifier(value = "deleteUser") Job deleteUser) {
		this.jobLauncher = jobLauncher;
		this.deleteUser = deleteUser;
	}

	@Scheduled(cron = "0 30 1 * * ?")
	public void deleteUserS() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {
		log.info("Scheduled task started - deleteUser");

		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("time", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(deleteUser, jobParameters);

		log.info("Scheduled task completed  - deleteUser");
	}
}
