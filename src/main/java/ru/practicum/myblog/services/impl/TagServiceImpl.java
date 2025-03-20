package ru.practicum.myblog.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.repositories.TagRepository;
import ru.practicum.myblog.services.TagService;
import ru.practicum.myblog.services.mappers.TagMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<String> getAvailableTags() {
        return tagMapper.tagToString(
                tagRepository.getAvailableTags());
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.findByName(tag.getName())
                .orElseGet(() -> tagRepository.save(tag));
    }

    @Override
    public List<Tag> findPostTags(long postId) {
        return tagRepository.findPostTags(postId);
    }

    @Override
    @Transactional
    public void attachPostTags(Long postId, List<Tag> savedTags) {
        savedTags.forEach(tag -> tagRepository.attachPostTag(postId, tag));
    }

    @Override
    public void detachPostTags(Long postId) {
        tagRepository.detachPostTags(postId);
    }
}
