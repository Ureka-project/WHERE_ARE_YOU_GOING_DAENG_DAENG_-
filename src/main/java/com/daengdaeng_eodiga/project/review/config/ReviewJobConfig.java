package com.daengdaeng_eodiga.project.review.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.daengdaeng_eodiga.project.review.dto.ReviewAVGScore;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ReviewJobConfig {
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job calculateReviewScore(JobRepository jobRepository) {
		return new JobBuilder("calculateReviewScore", jobRepository)
			.start(calculateReviewStep(jobRepository, transactionManager))
			.build();
	}
	@Bean
	public Step calculateReviewStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("calculateReviewStep", jobRepository)
			.<ReviewAVGScore, ReviewAVGScore>chunk(100, transactionManager)
			.reader(reviewScoreReader())
			.writer(reviewScoreWriter())
			.build();
	}

	@Bean
	public JdbcPagingItemReader<ReviewAVGScore> reviewScoreReader() {
		JdbcPagingItemReader<ReviewAVGScore> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		MySqlPagingQueryProvider queryProvider = (MySqlPagingQueryProvider)createPagingQueryProvider();

		reader.setQueryProvider(queryProvider);
		reader.setRowMapper(new BeanPropertyRowMapper<>(ReviewAVGScore.class));
		reader.setPageSize(100);

		return reader;
	}

	private PagingQueryProvider createPagingQueryProvider() {
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("SELECT place_id as placeId, AVG(score) AS avgScore, count(review_id) AS reviewCount");
		queryProvider.setFromClause("FROM review");
		queryProvider.setWhereClause("WHERE  DATE(created_at) = CURDATE() - INTERVAL 1 DAY");
		queryProvider.setGroupClause("GROUP BY place_id");
		queryProvider.setSortKeys(Map.of("placeId", Order.ASCENDING));

		return queryProvider;
	}

	@Bean
	public JdbcBatchItemWriter<ReviewAVGScore> reviewScoreWriter() {
		JdbcBatchItemWriter<ReviewAVGScore> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		String sql = """
            INSERT INTO place_score (place_id, score, review_count,created_at, updated_at)
            VALUES (:placeId, :avgScore, :reviewCount, NOW(), NOW())
            ON DUPLICATE KEY UPDATE
                score = (VALUES(score)*VALUES(review_count) +  score*review_count )/ (review_count + VALUES(review_count)),
                review_count = review_count + VALUES(review_count),
                updated_at = VALUES(updated_at);
        """;

		writer.setSql(sql);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		return writer;
	}
}
