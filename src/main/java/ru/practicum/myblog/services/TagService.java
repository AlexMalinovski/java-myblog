package ru.practicum.myblog.services;

import ru.practicum.myblog.data.Tag;

import java.util.List;

public interface TagService {
    List<String> getAvailableTags();

    Tag save(Tag tag);

    List<Tag> findPostTags(long postId);

    void attachPostTags(Long postId, List<Tag> savedTags);

    void detachPostTags(Long postId);
}
