package ru.practicum.myblog.repositories;

import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.dto.postfeed.PostFilter;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(long postId);

    List<Post> findByFilter(PostFilter filter);

    Post save(Post post);

    void removeById(long postId);
}
