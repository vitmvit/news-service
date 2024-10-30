package ru.clevertec.news.util;

import lombok.Builder;
import ru.clevertec.news.dto.CommentDto;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
public class CommentTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private Long newsId = 1L;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.of(2024, 1, 3, 9, 12, 15, 156);

    @Builder.Default
    private String username = "NameOne";

    @Builder.Default
    private String text = "Text";

    public CommentDto buildCommentDto() {
        return new CommentDto(id, time, text, username, newsId);
    }

    public Long getId() {
        return id;
    }
}