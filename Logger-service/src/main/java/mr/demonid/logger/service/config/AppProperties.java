package mr.demonid.logger.service.config;

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
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class AppProperties {

    private Resourceserver resourceserver;

    @Getter
    @Setter
    public static class Resourceserver {
        private Jwt jwt;

        @Getter
        @Setter
        public static class Jwt {
            private String issuerUri;
        }
    }
}
