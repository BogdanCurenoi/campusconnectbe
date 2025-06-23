package com.components.campusconnectbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.components.campusconnectbackend.repository.mongo")
public class MongoConfig {
    // MongoDB configuration will be handled by Spring Boot auto-configuration
    // Additional custom configuration can be added here if needed
}