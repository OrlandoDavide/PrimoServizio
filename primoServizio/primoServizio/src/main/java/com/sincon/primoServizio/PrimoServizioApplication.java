package com.sincon.primoServizio;

import com.sincon.primoServizio.config.ApplicationConfig.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = "com.sincon.primoServizio.model")
@EnableJpaRepositories(basePackages = "com.sincon.primoServizio.repository")
@EnableConfigurationProperties(JwtConfig.class)
public class PrimoServizioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimoServizioApplication.class, args);
	}

}
