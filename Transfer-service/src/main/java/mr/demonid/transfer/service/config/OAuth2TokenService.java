package mr.demonid.transfer.service.config;

import mr.demonid.transfer.service.dto.CustomTokenResponce;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;

/**
 * Сервис для получения токена от сервера авторизации.
 */
@Service
public class OAuth2TokenService {

    private static final long DELAY_TIME = 5;   // макс. время на сетевые задержки при передаче токена в запросах (сек)

    private final SecurityProperties properties;
    private final WebClient webClient;

    private String accessToken;             // кэшируем сам токен
    private Instant tokenExpirationTime;       // и время его действия (в миллисекундах)


    public OAuth2TokenService(SecurityProperties properties, WebClient.Builder webClientBuilder) {
        this.properties = properties;
        // создаём webClient, настроенный на адрес сервера аутентификации
        this.webClient = webClientBuilder
                .baseUrl(this.properties.getResourceserver().getJwt().getIssuerUri())
                .build();
    }

    /**
     * Собственно сам запрос сервера на получение токена.
     * @return Если все нормально, вернёт токен, иначе пустую строку.
     */
    public String getAccessToken() {
        if (isTokenValid()) {
            return accessToken;             // текущий токен все еще валиден, используем его.
        }
        // отправляем POST-запрос для получения токена
        CustomTokenResponce response = this.webClient.post()
                .uri("/oauth2/token")
                // формируем заголовок с clientId и clientSecret, а так же типом контента
                .headers(hdr -> {
                    hdr.setBasicAuth(properties.getAuthorization().getClientId(), properties.getAuthorization().getClientSecret());
                    hdr.set("Content-Type", "application/x-www-form-urlencoded");
                })
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("scope", properties.getAuthorization().getScope())
                )
                .retrieve()
                .bodyToMono(CustomTokenResponce.class)                      // преобразуем ответ в OAuth2AccessTokenResponse
                .block();                                                               // ожидаем результат
        if (response == null || response.getAccess_token() == null) {
            // не удалось получить токен
            return "";
        }
        // кэшируем токен и запоминаем время его действия
        accessToken = response.getAccess_token();
        tokenExpirationTime = Instant.now().plus(Duration.ofSeconds(response.getExpires_in() - DELAY_TIME));

        return accessToken;
    }

    private boolean isTokenValid() {
        return accessToken != null && Instant.now().isBefore(tokenExpirationTime);
    }

}