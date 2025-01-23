package mr.demonid.logger.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    /**
     * Цепочка фильтров безопасности.
     * Доступ:
     *      1) /api/logger/read: "ADMIN", "USER" и "SERVICE", но только со "SCOPE_read"
     *      2) /api/logger/write: "SERVICE", со "SCOPE_write"
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)              // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/logger/read")
                        .access(new WebExpressionAuthorizationManager(
                                "hasAnyRole('USER', 'ADMIN', 'SERVICE') and hasAuthority('SCOPE_read')"))
                        .requestMatchers("/api/logger/write", "/api/logger/confirm")
                        .access(new WebExpressionAuthorizationManager(
                                "hasRole('SERVICE') and hasAuthority('SCOPE_write')"))
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
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");       // Для scope
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");   // Из поля "scope"

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Set<GrantedAuthority> authorities = new HashSet<>(grantedAuthoritiesConverter.convert(jwt));
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

}

