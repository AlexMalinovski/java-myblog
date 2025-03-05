package ru.practicum.myblog.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.PostReaction;
import ru.practicum.myblog.repositories.PostReactionRepository;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostReactionServiceImplTest {

    @Mock
    private PostReactionRepository reactionRepository;

    @InjectMocks
    private PostReactionServiceImpl postReactionService;

    @Test
    void addLike() {
        var captor = ArgumentCaptor.forClass(PostReaction.class);

        postReactionService.addLike(100L);

        verify(reactionRepository).createNewPostReaction(captor.capture());
        PostReaction actual = captor.getValue();
        assertEquals(100L, actual.getPostId());
        assertEquals(ReactionType.LIKE, actual.getType());
    }

    @Test
    void getPostsNumLikes() {
        Set<Long> postsIds = Set.of(100L);
        Map<Long, Long> expected = Map.of();
        when(reactionRepository.getPostsNumReactions(any(), any())).thenReturn(expected);

        var actual = postReactionService.getPostsNumLikes(postsIds);

        assertEquals(expected, actual);
        verify(reactionRepository).getPostsNumReactions(postsIds, ReactionType.LIKE);
    }
}