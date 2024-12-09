package com.daengdaeng_eodiga.project.user.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserJobConfig {
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job deleteUser(JobRepository jobRepository) {
		return new JobBuilder("deleteUser", jobRepository)
			.start(deleteUserStep(jobRepository, transactionManager))
			.build();
	}

	@Bean
	public Step deleteUserStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("deleteUserStep", jobRepository)
			.tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
				String sql = """
                        DELETE FROM Users 
                        WHERE CURDATE() = DATE(deleted_at) + INTERVAL 30 DAY
                    """;

				try (Connection connection = dataSource.getConnection();
					 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

					int rowsDeleted = preparedStatement.executeUpdate();

					log.info("Users deleted: " + rowsDeleted);

				} catch (SQLException e) {
					log.info("Error while deleting users: " + e.getMessage());
					throw new RuntimeException("Error executing delete tasklet", e);
				}

				return RepeatStatus.FINISHED;
			}, transactionManager)
			.build();
	}

}