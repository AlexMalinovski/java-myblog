package ru.practicum.myblog.services.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.myblog.Util;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    private final CommentMapper mapper = new CommentMapperImpl();

    @Test
    void toPostComment() {
        NewCommentDto newCommentDto = Util.getNewCommentDto();

        var actual = mapper.toPostComment(newCommentDto, 100L);

        assertEquals(100L, actual.getPostId());
        assertEquals(newCommentDto.id(), actual.getId());
        assertEquals(newCommentDto.body(), actual.getBody());
    }

    @Test
    void updateComment() {
        PostComment comment = PostComment.builder().id(100L).postId(200L).build();
        NewCommentDto newCommentDto = Util.getNewCommentDto();

        var actual = mapper.updateComment(comment.toBuilder(), newCommentDto);

        assertEquals(comment.getId(), actual.getId());
        assertEquals(comment.getPostId(), actual.getPostId());
        assertEquals(newCommentDto.body(), actual.getBody());
    }

    @Test
    void toPostCommentDto() {
        PostComment comment = Util.getNewPostComment();

        var actual = mapper.toPostCommentDto(comment);

        assertEquals(comment.getId(), actual.getId());
        assertEquals(comment.getBody(), actual.getBody());
    }

    @Test
    void toPostCommentDto_List() {
        var comment = List.of(Util.getNewPostComment());

        var actual = mapper.toPostCommentDto(comment);

        assertEquals(1, actual.size());
        assertEquals(comment.get(0).getId(), actual.get(0).getId());
        assertEquals(comment.get(0).getBody(), actual.get(0).getBody());
    }
}