package ru.practicum.myblog.services;

import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommentService {
    void createPostComment(long postId, NewCommentDto newCommentDto);

    void updateComment(NewCommentDto editedComment);

    Map<Long, Long> getPostsNumComments(Set<Long> postsIds);

    List<PostComment> getPostComments(long postId);
}
