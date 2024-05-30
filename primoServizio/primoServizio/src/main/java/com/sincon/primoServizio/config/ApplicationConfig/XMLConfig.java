package com.sincon.primoServizio.config.ApplicationConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "xml.edotto")
@Qualifier("XMLConfig")
public class XMLConfig {

    private String path;

    private String pattern;
}
