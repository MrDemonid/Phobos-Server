package mr.demonid.rs232dev.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Настройки из application.yml
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.properties")
public class AppProperties {
    private String auxPort;
    private Integer numRepeaters;
    private Integer waitTime;
}
