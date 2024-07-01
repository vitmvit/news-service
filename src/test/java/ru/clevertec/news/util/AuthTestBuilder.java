package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.auth.JwtDto;
import ru.clevertec.news.dto.constant.RoleName;

@Builder(setterPrefix = "with")
public class AuthTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String login = "TestUser";

    @Builder.Default
    private String password = "TestUser";

    @Builder.Default
    private RoleName role = RoleName.ADMIN;

    public JwtDto buildJwtDto() {
        return new JwtDto("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJTVUJTQ1JJQkVSIiwidXNlcm5hbWUiOiJTVUJTQ1JJQkVSIiwicm9sZSI6IlNVQlNDUklCRVIiLCJleHAiOjE3MDkxNzU1NTV9.uuZclt5mJniONm3Ax_8zAElwOgzk-QqQtoXgMQqPiXo");
    }

    public String getLogin() {
        return login;
    }

    public Long getId() {
        return id;
    }
}