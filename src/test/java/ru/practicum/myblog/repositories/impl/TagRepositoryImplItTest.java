package ru.practicum.myblog.repositories.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.myblog.AbstractIntegrationTest;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.repositories.PostRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagRepositoryImplItTest extends AbstractIntegrationTest {
    @Autowired
    private TagRepositoryImpl tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void getAvailableTags() {
        tagRepository.save(new Tag("tag1"));
        tagRepository.save(new Tag("tag2"));

        var actual = tagRepository.getAvailableTags();
        assertEquals(2, actual.size());
        var collect = actual.stream().map(Tag::getName).collect(Collectors.toSet());
        assertEquals(Set.of("tag1", "tag2"), collect);
    }

    @Test
    void findByName() {
        Tag expected = new Tag("tag1");
        tagRepository.save(expected);
        tagRepository.save(new Tag("tag2"));

        var actual = tagRepository.findByName("tag1").orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void findPostTags() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        Post postOther = postRepository.save(Post.builder().title("title2").body("body2").build());
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tagOther = tagRepository.save(new Tag("tag2"));
        tagRepository.attachPostTag(post.getId(), tag1);

        List<Tag> postTags = tagRepository.findPostTags(post.getId());
        List<Tag> postOtherTags = tagRepository.findPostTags(postOther.getId());

        assertTrue(postOtherTags.isEmpty());
        assertEquals(1, postTags.size());
        assertEquals(tag1, postTags.getFirst());
    }

    @Test
    void detachPostTags() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        tagRepository.attachPostTag(post.getId(), tag1);
        tagRepository.attachPostTag(post.getId(), tag2);

        tagRepository.detachPostTags(post.getId());

        List<Tag> tags = tagRepository.findPostTags(post.getId());
        assertTrue(tags.isEmpty());
    }
}