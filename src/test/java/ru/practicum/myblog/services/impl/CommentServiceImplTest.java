package ru.practicum.myblog.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.myblog.Util;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.repositories.CommentRepository;
import ru.practicum.myblog.services.mappers.CommentMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void createPostComment() {
        var commentDto = Util.getNewCommentDto();
        PostComment comment = Util.getNewPostComment();
        when(commentMapper.toPostComment(any(), any())).thenReturn(comment);

        commentService.createPostComment(100L, commentDto);

        verify(commentMapper).toPostComment(commentDto, 100L);
        verify(commentRepository).save(comment);
    }

    @Test
    void updateComment_whenNotFound_thenThrowIllegalArgumentException() {
        var commentDto = Util.getNewCommentDto();
        when(commentRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.updateComment(commentDto));
    }

    @Test
    void updateComment() {
        var commentDto = Util.getNewCommentDto();
        PostComment comment = Util.getNewPostComment();
        PostComment updated = comment.toBuilder().body("newbody").build();
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        when(commentMapper.updateComment(any(), any())).thenReturn(updated);

        commentService.updateComment(commentDto);

        verify(commentRepository).save(updated);
    }

    @Test
    void getPostsNumComments() {
        Set<Long> postsIds = Set.of(1L);
        Map<Long, Long> expected = Map.of();
        when(commentRepository.getPostsNumComments(any())).thenReturn(expected);

        var actual = commentService.getPostsNumComments(postsIds);

        verify(commentRepository).getPostsNumComments(postsIds);
        assertEquals(expected, actual);
    }

    @Test
    void getPostComments() {
        List<PostComment> expected = List.of();
        when(commentRepository.getPostComments(anyLong())).thenReturn(expected);

        var actual = commentService.getPostComments(100L);

        assertEquals(expected, actual);
        verify(commentRepository).getPostComments(100L);
    }
}