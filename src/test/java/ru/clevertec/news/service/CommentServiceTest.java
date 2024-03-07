package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.CommentClient;
import ru.clevertec.news.service.impl.CommentServiceImpl;
import ru.clevertec.news.util.CommentTestBuilder;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void findCommentByIdShouldReturnExpectedCommentWhenFound() {
        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
        Long id = expected.getId();

        when(commentClient.getById(id)).thenReturn(CommentTestBuilder.builder().build().buildCommentDto());

        CommentDto actual = commentService.findCommentById(id);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findCommentByIdShouldReturnExceptionWhenNotFound() {
        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
        Long id = expected.getId();

        when(commentClient.getById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentClient.getById(id));
    }

    @Test
    void searchCommentsByTextReturnExpectedPageComments() {
        String text = CommentTestBuilder.builder().build().getText();
        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        ));

        when(commentClient.searchByText(OFFSET, LIMIT, text)).thenReturn(commentDtoPage);

        var actual = commentService.searchCommentsByText(OFFSET, LIMIT, text);

        assertEquals(commentDtoPage.getTotalElements(), actual.getTotalElements());
    }

    @Test
    void searchCommentsByTextReturnException() {
        String text = CommentTestBuilder.builder().build().getText();

        when(commentClient.searchByText(OFFSET, LIMIT, text)).thenThrow(EmptyListException.class);

        assertThrows(EmptyListException.class, () -> commentClient.searchByText(OFFSET, LIMIT, text));
    }


    @Test
    void searchCommentsByUsernameReturnExpectedPageComments() {
        String username = CommentTestBuilder.builder().build().getUsername();
        Page<CommentDto> commentDtoPage = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildCommentDto()
        ));

        when(commentClient.searchByUsername(OFFSET, LIMIT, username)).thenReturn(commentDtoPage);

        var actual = commentService.searchCommentsByUsername(OFFSET, LIMIT, username);

        assertEquals(commentDtoPage.getTotalElements(), actual.getTotalElements());
    }

    @Test
    void searchCommentsByUsernameReturnException() {
        String username = CommentTestBuilder.builder().build().getText();

        when(commentClient.searchByUsername(OFFSET, LIMIT, username)).thenThrow(EmptyListException.class);

        assertThrows(EmptyListException.class, () -> commentClient.searchByUsername(OFFSET, LIMIT, username));
    }

    @Test
    void createCommentShouldInvokeRepositoryWithoutCommentId() {
        CommentCreateDto commentToCreate = CommentTestBuilder.builder().build().buildCommentCreateDto();
        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
        CommentDto dto = CommentTestBuilder.builder().build().buildCommentDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(commentClient.create(commentToCreate, token)).thenReturn(dto);

        var actual = commentService.createComment(commentToCreate, token);
        assertEquals(expected.getNewsId(), actual.getNewsId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void updateCommentShouldCallsMergeAndSaveWhenCommentFound() {
        CommentUpdateDto commentToUpdate = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        CommentDto expected = CommentTestBuilder.builder().build().buildCommentDto();
        CommentDto dto = CommentTestBuilder.builder().build().buildCommentDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(commentClient.update(commentToUpdate, token)).thenReturn(dto);

        var actual = commentService.updateComment(commentToUpdate, token);
        assertEquals(expected.getNewsId(), actual.getNewsId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void updateCommentShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        CommentUpdateDto dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(commentClient.update(dto, token)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentClient.update(dto, token));
    }

    @Test
    void deleteComment() {
        Long id = CommentTestBuilder.builder().build().getId();
        Long userId = CommentTestBuilder.builder().build().getId();
        String token = CommentTestBuilder.builder().build().getToken();

        commentService.deleteComment(id, userId, token);
        verify(commentClient).delete(id, userId, token);
    }
}
