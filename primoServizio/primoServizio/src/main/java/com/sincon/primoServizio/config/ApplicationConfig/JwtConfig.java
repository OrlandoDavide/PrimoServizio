package com.sincon.primoServizio.config.ApplicationConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private long EXPIRATION;

    private String SECRET_KEY;
}
