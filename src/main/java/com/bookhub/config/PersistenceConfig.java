package com.bookhub.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.bookhub.entity")
@EnableJpaRepositories(basePackages = "com.bookhub.repository")
public class PersistenceConfig {
}
