package mr.demonid.rs232dev.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Настройки Spring Security из application.yml
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class SecurityProperties {

    private Resourceserver resourceserver;
    private Authorization authorization;

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

    @Getter
    @Setter
    public static class Authorization {
        private String clientId;
        private String clientSecret;
        private String scope;
    }

}
