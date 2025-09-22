package com.internalworkflow.config;

import com.internalworkflow.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BootstrapConfig {
	@Bean
	CommandLineRunner bootstrap(UserService userService) {
		return args -> userService.seedAdminIfMissing();
	}
}
