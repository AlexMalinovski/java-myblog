package ru.practicum.myblog.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.dto.postfeed.FeedRowDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.dto.postfeed.PostFullDto;
import ru.practicum.myblog.exceptoins.NotFoundException;
import ru.practicum.myblog.repositories.PostRepository;
import ru.practicum.myblog.services.CommentService;
import ru.practicum.myblog.services.PostReactionService;
import ru.practicum.myblog.services.PostService;
import ru.practicum.myblog.services.TagService;
import ru.practicum.myblog.services.mappers.PostMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagService tagService;
    private final CommentService commentService;
    private final PostReactionService postReactionService;
    private final PostMapper postMapper;

    @Override
    public Page<FeedRowDto> findPaginated(Pageable pageable, PostFilter filter) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> posts = postRepository.findByFilter(filter);

        List<FeedRowDto> list;
        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            final Set<Long> postsIds = posts.subList(startItem, toIndex)
                    .stream()
                    .map(Post::getId)
                    .collect(Collectors.toSet());
            Map<Long, Long> numComments = commentService.getPostsNumComments(postsIds);
            Map<Long, Long> numLikes = postReactionService.getPostsNumLikes(postsIds);
            list = posts.subList(startItem, toIndex)
                    .stream()
                    .map(post -> post.toBuilder().tags(tagService.findPostTags(post.getId())).build())
                    .map(post -> postMapper.toFeedRowDto(post, numLikes.getOrDefault(post.getId(), 0L),
                            numComments.getOrDefault(post.getId(), 0L)))
                    .toList();
        }
        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), posts.size());
    }

    @Override
    @Transactional
    public void savePost(PostEditDto postDto) {
        Objects.requireNonNull(postDto);
        var post = postMapper.toPost(postDto);
        final var postId = post.getId();
        List<Tag> savedTags = Optional.ofNullable(post.getTags())
                .stream()
                .flatMap(Collection::stream)
                .map(tagService::save)
                .toList();
        var savedPost = postRepository.save(post);
        if (!savedTags.isEmpty()) {
            if (postId != null) {
                tagService.detachPostTags(postId);
            }
            tagService.attachPostTags(savedPost.getId(), savedTags);
        }
    }

    @Override
    public Optional<PostEditDto> getEditablePostById(long id) {
        return postRepository.findById(id)
                .map(Post::toBuilder)
                .map(builder -> builder.tags(tagService.findPostTags(id)))
                .map(Post.PostBuilder::build)
                .map(postMapper::toPostEditDto);
    }

    @Override
    public PostEditDto getEmpty() {
        return new PostEditDto(null, "", "", null, List.of());
    }

    @Override
    public PostFullDto getPostFullDto(long postId) {
        List<PostComment> comments = commentService.getPostComments(postId);
        Long numLikes = postReactionService.getPostsNumLikes(Set.of(postId))
                .getOrDefault(postId, 0L);
        return postRepository.findById(postId)
                .map(Post::toBuilder)
                .map(b -> b.tags(tagService.findPostTags(postId)))
                .map(builder -> builder.comments(comments))
                .map(Post.PostBuilder::build)
                .map(post -> postMapper.toPostFullDto(post, numLikes))
                .orElseThrow(() -> new NotFoundException(String.format("Post with id %d is not found", postId)));
    }

    @Override
    public void removePost(long postId) {
        postRepository.removeById(postId);
    }
}
