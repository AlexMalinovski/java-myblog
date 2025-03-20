package ru.practicum.myblog.repositories;

import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.PostReaction;

import java.util.Map;
import java.util.Set;

public interface PostReactionRepository {
    Map<Long, Long> getPostsNumReactions(Set<Long> postsIds, ReactionType reactionType);

    PostReaction createNewPostReaction(PostReaction reaction);
}
