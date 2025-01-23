package mr.demonid.rs232dev.service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

/**
 * Перехватчик Feign-запросов, для вставки в них jwt-токена.
 */
@AllArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final OAuth2TokenService tokenService;

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenService.getAccessToken();
        template.header("Authorization", "Bearer " + token);
    }
}