package ru.practicum.myblog.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.PostReaction;
import ru.practicum.myblog.repositories.PostReactionRepository;
import ru.practicum.myblog.services.PostReactionService;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {
    private final PostReactionRepository reactionRepository;
    @Override
    public void addLike(long postId) {
        Long userId = ThreadLocalRandom.current().nextLong(); //костыль чтобы поставить много лайков
        PostReaction reaction = PostReaction.builder()
                .postId(postId)
                .userId(userId)
                .type(ReactionType.LIKE)
                .build();
        try {
            reactionRepository.createNewPostReaction(reaction);
        } catch (Exception ex) {
            /* Ничего. Если совпали сгенерированные костылём ID. Убрать вместе с костылём */
        }
    }

    @Override
    public Map<Long, Long> getPostsNumLikes(Set<Long> postsIds) {
        if (postsIds == null || postsIds.isEmpty()) {
            return Map.of();
        }
        return reactionRepository.getPostsNumReactions(postsIds, ReactionType.LIKE);
    }
}
