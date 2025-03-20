package ru.practicum.myblog.repositories.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.myblog.AbstractIntegrationTest;
import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.PostReaction;
import ru.practicum.myblog.repositories.PostRepository;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostReactionRepositoryImplItTest extends AbstractIntegrationTest {
    @Autowired
    PostReactionRepositoryImpl postReactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void getPostsNumReactions() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        var reaction = postReactionRepository.createNewPostReaction(PostReaction.builder()
                .postId(post.getId()).type(ReactionType.LIKE).userId(1L).build());

        Map<Long, Long> actual = postReactionRepository.getPostsNumReactions(Set.of(post.getId()), ReactionType.LIKE);

        assertEquals(1, actual.size());
        assertTrue(actual.containsKey(post.getId()));
        assertEquals(1, actual.get(post.getId()));
    }
}