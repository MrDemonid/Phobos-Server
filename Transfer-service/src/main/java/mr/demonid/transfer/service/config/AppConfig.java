package mr.demonid.transfer.service.config;

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
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    private int waitTime;
    private int apmBufSize;
    private int devBufSize;
}
