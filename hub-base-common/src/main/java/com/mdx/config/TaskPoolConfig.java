package com.mdx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 */
@EnableAsync
@Configuration
@Slf4j
public class TaskPoolConfig {

	@Value("${async.executor.thread.core_pool_size:5}")
	private Integer corePooleSize;
	@Value("${async.executor.thread.max_pool_size:20}")
	private Integer maxPooleSize;
	@Value("${async.executor.thread.queue_capacity:10}")
	private Integer queueCapacity;

	@Bean("task-pool")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePooleSize);
		executor.setMaxPoolSize(maxPooleSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("define_async_Executor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

}
