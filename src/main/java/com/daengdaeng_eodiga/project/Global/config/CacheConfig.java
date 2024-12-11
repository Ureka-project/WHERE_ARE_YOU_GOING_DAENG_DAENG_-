package com.daengdaeng_eodiga.project.Global.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {
	@Bean(name = "commonCodeCacheManager")
	public CaffeineCacheManager commonCodeCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager( "commonCode","checkCommonCodeExist");
		cacheManager.setCaffeine(Caffeine.newBuilder().recordStats());
		return cacheManager;
	}
}
