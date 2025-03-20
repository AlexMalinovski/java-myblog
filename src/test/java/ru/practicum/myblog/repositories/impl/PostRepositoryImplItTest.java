package ru.practicum.myblog.repositories.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.myblog.AbstractIntegrationTest;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.repositories.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostRepositoryImplItTest extends AbstractIntegrationTest {

    @Autowired
    private PostRepositoryImpl postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void findById() {
        Post saved = postRepository.save(Post.builder().title("title").body("body").build());

        var founded = postRepository.findById(saved.getId());

        assertTrue(founded.isPresent());
        assertEquals(saved, founded.get());
    }

    @Test
    void findByFilter() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        postRepository.save(Post.builder().title("title2").body("body2").build());
        Tag tag = tagRepository.save(new Tag("tagname"));

        List<Post> founded = postRepository.findByFilter(new PostFilter("tagname"));
        assertTrue(founded.isEmpty());

        tagRepository.attachPostTag(post.getId(), tag);
        founded = postRepository.findByFilter(new PostFilter("tagname"));
        assertEquals(1, founded.size());
        assertEquals(post, founded.getFirst());
    }
}