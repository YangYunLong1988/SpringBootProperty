package com.snowstore.terra.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class Config {
	@Bean
	@ConditionalOnMissingBean(ClassPathTldsLoader.class)
	public ClassPathTldsLoader classPathTldsLoader() {
		return new ClassPathTldsLoader();
	}
}
