package ru.practicum.myblog.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;
import ru.practicum.myblog.repositories.CommentRepository;
import ru.practicum.myblog.services.CommentService;
import ru.practicum.myblog.services.mappers.CommentMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public void createPostComment(long postId, NewCommentDto newCommentDto) {
        commentRepository.save(
                commentMapper.toPostComment(newCommentDto, postId));
    }

    @Override
    @Transactional
    public void updateComment(NewCommentDto editedComment) {
        PostComment existComment = Optional.of(editedComment.id())
                .flatMap(commentRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));
        PostComment updates = commentMapper.updateComment(existComment.toBuilder(), editedComment);
        commentRepository.save(updates);
    }

    @Override
    public Map<Long, Long> getPostsNumComments(Set<Long> postsIds) {
        if (postsIds == null || postsIds.isEmpty()) {
            return Map.of();
        }
        return commentRepository.getPostsNumComments(postsIds);
    }

    @Override
    public List<PostComment> getPostComments(long postId) {
        return commentRepository.getPostComments(postId);
    }
}
