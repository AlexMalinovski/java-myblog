package ru.practicum.myblog.repositories.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.myblog.AbstractIntegrationTest;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.repositories.PostRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentRepositoryImplItTest extends AbstractIntegrationTest {

    @Autowired
    private CommentRepositoryImpl commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void getPostsNumComments() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        PostComment comment = commentRepository.save(PostComment.builder().postId(post.getId()).body("body").build());

        Map<Long, Long> actual = commentRepository.getPostsNumComments(Set.of(post.getId()));

        assertEquals(1, actual.size());
        assertTrue(actual.containsKey(post.getId()));
        assertEquals(1, actual.get(post.getId()));
    }

    @Test
    void save() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());

        PostComment saved = commentRepository.save(PostComment.builder().postId(post.getId()).body("body").build());

        Optional<PostComment> founded = commentRepository.findById(saved.getId());
        assertTrue(founded.isPresent());
        assertEquals(saved, founded.get());
    }

    @Test
    void getPostComments() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        PostComment comment = commentRepository.save(PostComment.builder().postId(post.getId()).body("body").build());

        List<PostComment> postComments = commentRepository.getPostComments(post.getId());

        assertEquals(1, postComments.size());
        assertEquals(comment, postComments.getFirst());
    }
}