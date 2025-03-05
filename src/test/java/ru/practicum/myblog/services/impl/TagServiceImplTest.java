package ru.practicum.myblog.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.repositories.TagRepository;
import ru.practicum.myblog.services.mappers.TagMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getAvailableTags() {
        var tags = List.of(new Tag("tag"));
        when(tagRepository.getAvailableTags()).thenReturn(tags);
        when(tagMapper.tagToString(tags)).thenReturn(List.of("tag"));

        var actual = tagService.getAvailableTags();

        assertEquals(List.of("tag"), actual);
        verify(tagRepository).getAvailableTags();
        verify(tagMapper).tagToString(tags);
    }

    @Test
    void save_whenExist_thenNothing() {
        Tag tag = new Tag("tagname");
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));

        tagService.save(tag);

        verify(tagRepository).findByName(tag.getName());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void save_whenNew_thenSave() {
        Tag tag = new Tag("tagname");
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());

        tagService.save(tag);

        verify(tagRepository).findByName(tag.getName());
        verify(tagRepository).save(tag);
    }

    @Test
    void findPostTags() {
        tagService.findPostTags(100L);

        verify(tagRepository).findPostTags(100L);
    }

    @Test
    void attachPostTags() {
        var tags = List.of(new Tag("tag1"));
        tagService.attachPostTags(100L, tags);
        verify(tagRepository).attachPostTag(100L, tags.get(0));
    }

    @Test
    void detachPostTags() {
        tagService.detachPostTags(100L);
        verify(tagRepository).detachPostTags(100L);
    }
}