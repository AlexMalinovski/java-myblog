package ru.practicum.myblog.repositories;

import ru.practicum.myblog.data.PostComment;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository {
    Map<Long, Long> getPostsNumComments(Set<Long> postsIds);

    PostComment save(PostComment comment);

    Optional<PostComment> findById(Long id);

    List<PostComment> getPostComments(long postId);

}
