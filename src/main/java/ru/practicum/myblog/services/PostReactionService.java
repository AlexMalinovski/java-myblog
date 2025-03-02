package ru.practicum.myblog.services;

import java.util.Map;
import java.util.Set;

public interface PostReactionService {
    void addLike(long postId);

    Map<Long, Long> getPostsNumLikes(Set<Long> postsIds);

}
