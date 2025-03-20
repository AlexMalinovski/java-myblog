package ru.practicum.myblog.repositories;

import ru.practicum.myblog.data.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<Tag> getAvailableTags();

    Tag save(Tag tag);

    Optional<Tag> findByName(String name);

    List<Tag> findPostTags(long postId);

    void attachPostTag(long postId, Tag tag);

    void detachPostTags(Long postId);
}
