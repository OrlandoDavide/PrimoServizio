package com.sincon.primoServizio.config.ApplicationConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    @Setter
    private long EXPIRATION;

    @Setter
    private String SECRET_KEY;
}
