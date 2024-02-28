package ru.clevertec.news.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign-клиент для взаимодействия с микросервисом аутентификации
 */
@FeignClient(contextId = "authClient", value = "authService", url = "http://localhost:8081/api/auth")
public interface AuthClient {

    /**
     * Проверка валидности JWT-токена
     *
     * @param token  JWT-токен
     * @param userId идентификатор пользователя
     * @param login  логин пользователя
     * @return true, если JWT-токен действителен; false в противном случае
     */
    @PostMapping("/check")
    Boolean check(@RequestParam("token") String token,
                  @RequestParam(name = "userId", required = false) Long userId,
                  @RequestParam(name = "login", required = false) String login);
}