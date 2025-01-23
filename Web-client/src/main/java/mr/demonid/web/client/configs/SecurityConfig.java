package mr.demonid.web.client.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/api/web-service/**")
                        .access(new WebExpressionAuthorizationManager("hasAnyRole('ADMIN', 'USER') and hasAnyAuthority('SCOPE_read','SCOPE_write', 'SCOPE_update', 'SCOPE_delete', 'SCOPE_create')"))
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jt -> jt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .formLogin(AbstractHttpConfigurer::disable)    // Отключаем перенаправление на форму входа
                .httpBasic(AbstractHttpConfigurer::disable);   // Отключаем Basic Auth

        return http.build();
    }

    /**
     * Извлекает из полей запроса значения ROLE и SCOPE.
     * Сделано "вручную" с целью в дальнейшем изменить формат токена на свой.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_"); // Для scope
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope"); // Из поля "scope"

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>(grantedAuthoritiesConverter.convert(jwt));    // добавляем в итоговый результат все SCOPE
            // добавляем все роли
            List<String> roles = jwt.getClaimAsStringList("authorities"); // Из поля "authorities"
            if (roles != null) {
                roles.forEach(role -> {
                    if (role.startsWith("ROLE_")) {
                        authorities.add(new SimpleGrantedAuthority(role)); // Роль уже с префиксом
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // Добавляем префикс
                    }
                });
            }
            return authorities;
        });
        return authenticationConverter;
    }

//    @PostConstruct
//    public void clientRunner() {
//        System.out.println("Client runner started");
//        Map<String, String> env = System.getenv();
//        env.forEach((key, value) -> System.out.println(key + " = " + value));
//    }

}
