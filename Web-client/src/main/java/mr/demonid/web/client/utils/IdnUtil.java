package mr.demonid.web.client.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class IdnUtil {

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Возвращает идентификатор авторизированного пользователя.
     */
    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return jwtToken.getToken().getClaimAsString("user_id");
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return oidcUser.getIdToken().getClaimAsString("user_id");
        }
        return null;
    }

    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }


    /**
     * Возвращает идентификатор анонимного пользователя, которого
     * ранее пометили в куки.
     */
    public static String getAnonymousId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "ANON_ID".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static Cookie getCookie(String name, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Добавление куки "ANON_ID" для анонимного пользователя.
     * Но лучше это делать через фильтры (как и реализовано
     * в этом клиенте).
     */
    public void setAnonymousCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("ANON_ID", UUID.randomUUID().toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
        response.addCookie(cookie);
    }


    /**
     * Возвращает Jwt-токен текущего пользователя.
     *
     * @return Строка токена, или null - если пользователь не аутентифицирован.
     */
    public static String getCurrentUserToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return jwtToken.getToken().getTokenValue();
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return oidcUser.getIdToken().getTokenValue();
        }
        return null;
    }

    /**
     * Возвращает список прав текущего пользователя.
     * @return null - при ошибке, или если пользователь не авторизирован.
     */
    public static List<String> extractScopesFromToken() {
        try {
            String token = getCurrentUserToken();
            if (token != null) {
                SignedJWT signedJWT = SignedJWT.parse(token);
                return signedJWT.getJWTClaimsSet().getStringListClaim("scope");
            }
        } catch (Exception ignored) {}
        return Collections.emptyList();
    }
}
