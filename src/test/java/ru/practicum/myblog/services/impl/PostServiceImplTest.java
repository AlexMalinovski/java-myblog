package ru.practicum.myblog.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.dto.postfeed.PostFullDto;
import ru.practicum.myblog.repositories.PostRepository;
import ru.practicum.myblog.services.CommentService;
import ru.practicum.myblog.services.PostReactionService;
import ru.practicum.myblog.services.TagService;
import ru.practicum.myblog.services.mappers.PostMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @Mock
    private CommentService commentService;

    @Mock
    private PostReactionService postReactionService;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void findPaginated() {
        List<Post> posts = List.of(
                Post.builder().id(1L).build(),
                Post.builder().id(2L).build(),
                Post.builder().id(3L).build(),
                Post.builder().id(4L).build(),
                Post.builder().id(5L).build()
        );
        Pageable pageable = Mockito.mock(Pageable.class);
        PostFilter filter = new PostFilter("tagname");
        when(pageable.getPageSize()).thenReturn(2);
        when(pageable.getPageNumber()).thenReturn(0);
        when(postRepository.findByFilter(any())).thenReturn(posts);

        var paginated = postService.findPaginated(pageable, filter);

        verify(postRepository).findByFilter(filter);
        assertEquals(3, paginated.getTotalPages());
        assertEquals(posts.size(), paginated.getTotalElements());
    }

    @Test
    void savePost() {
        var tag = new Tag("tag");
        PostEditDto postDto = new PostEditDto(null, null, null, null, null);
        Post post = Post.builder().tags(List.of(tag)).build();
        when(postMapper.toPost(any())).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post.toBuilder().id(100L).build());
        when(tagService.save(tag)).thenReturn(tag);

        postService.savePost(postDto);

        verify(tagService).save(tag);
        verify(postRepository).save(post);
        verify(tagService).attachPostTags(100L, List.of(tag));
    }

    @Test
    void getEditablePostById() {
        PostEditDto postEditDto = new PostEditDto(null, null, null, null, null);
        when(postMapper.toPostEditDto(any())).thenReturn(postEditDto);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(Post.builder().build()));

        var actual = postService.getEditablePostById(100L);

        assertEquals(postEditDto, actual.get());
        verify(postRepository).findById(100L);
        verify(tagService).findPostTags(100L);
    }

    @Test
    void getEmpty() {
        var expected = new PostEditDto(null, "", "", null, List.of());
        assertEquals(expected, postService.getEmpty());
    }

    @Test
    void getPostFullDto() {
        PostFullDto postFullDto = PostFullDto.builder().build();
        when(postRepository.findById(100L)).thenReturn(Optional.of(Post.builder().build()));
        when(postMapper.toPostFullDto(any(), anyLong())).thenReturn(postFullDto);

        postService.getPostFullDto(100L);

        verify(commentService).getPostComments(100L);
        verify(postReactionService).getPostsNumLikes(Set.of(100L));
        verify(postRepository).findById(100L);
        verify(tagService).findPostTags(100L);
    }

    @Test
    void removePost() {
        postService.removePost(100L);

        verify(postRepository).removeById(100L);
    }
}