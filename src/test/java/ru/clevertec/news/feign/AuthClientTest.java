package ru.clevertec.news.feign;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.util.AuthTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 9998)
public class AuthClientTest extends PostgresSqlContainerInitializer {

    @Autowired
    private AuthClient authClient;

    @Test
    public void checkShouldReturnTrue() {
        String token = AuthTestBuilder.builder().build().buildJwtDto().accessToken();
        Long userId = AuthTestBuilder.builder().build().getId();
        String login = AuthTestBuilder.builder().build().getLogin();

        stubFor(post(urlPathTemplate("/api/auth/check"))
                .withQueryParam("token", equalTo(token))
                .withQueryParam("userId", equalTo(String.valueOf(userId)))
                .withQueryParam("login", equalTo(login))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("true")));

        var actual = authClient.check(token, userId, login);

        assertTrue(actual);
    }

    @Test
    public void checkShouldReturnFalse() {
        String token = AuthTestBuilder.builder().build().buildJwtDto().accessToken();
        Long userId = AuthTestBuilder.builder().build().getId();
        String login = AuthTestBuilder.builder().build().getLogin();

        stubFor(post(urlPathTemplate("/api/auth/check"))
                .withQueryParam("token", equalTo(token))
                .withQueryParam("userId", equalTo(String.valueOf(userId)))
                .withQueryParam("login", equalTo(login))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("false")
                ));

        var actual = authClient.check(token, userId, login);

        assertFalse(actual);
    }

    @Test
    public void checkShouldReturnExceptionAndStatus302() {
        String token = AuthTestBuilder.builder().build().buildJwtDto().accessToken();
        Long userId = AuthTestBuilder.builder().build().getId();
        String login = AuthTestBuilder.builder().build().getLogin();

        stubFor(post(urlPathTemplate("/api/auth/check"))
                .withQueryParam("token", matching("^(.*)([A-Za-z][0-9]+)$"))
                .withQueryParam("userId", matching("^(.*)([0-9]+)$"))
                .withQueryParam("login", matching("^(.*)([A-Za-z][0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(302)));

        assertThrows(FeignException.class, () -> authClient.check(token, userId, login));
    }

    @Test
    public void checkShouldReturnExceptionAndStatus403() {
        String token = AuthTestBuilder.builder().build().buildJwtDto().accessToken();
        Long userId = AuthTestBuilder.builder().build().getId();
        String login = AuthTestBuilder.builder().build().getLogin();

        stubFor(post(urlPathTemplate("/api/auth/check"))
                .withQueryParam("token", matching("^(.*)([A-Za-z][0-9]+)$"))
                .withQueryParam("userId", matching("^(.*)([0-9]+)$"))
                .withQueryParam("login", matching("^(.*)([A-Za-z][0-9]+)$"))
                .willReturn(aResponse()
                        .withStatus(403)));

        assertThrows(FeignException.class, () -> authClient.check(token, userId, login));
    }
}