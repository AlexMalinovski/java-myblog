package ru.practicum.myblog.services.mappers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.myblog.Util;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFullDto;
import ru.practicum.myblog.dto.postfeed.PostPreviewDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    @Mock
    private TagMapper tagMapper;

    @Mock
    private CommentMapper commentMapper;

    @Spy
    @InjectMocks
    private PostMapperImpl postMapper;

    @Test
    void mapImageBytesToBase64() {
        byte[] arr = {(byte) 0xaa, (byte) 0xcc};
        String expected = "qsw=";

        String actual = postMapper.mapImageBytesToBase64(arr);

        assertEquals(expected, actual);
    }

    @Test
    void shortPostBody() {
        String expected = "expected string";
        String input = String.format("""
                %s
                dskfbnlk sdkljfnvlkjs skdjfnvlksjdfv skdjfnvlkj
                                
                skjehbksjvh
                """, expected);

        String actual = postMapper.shortPostBody(input);

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    void mapMultipartToBytes() {
        byte[] expected = {1, 2};
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(expected);

        byte[] actual = postMapper.mapMultipartToBytes(file);

        assertArrayEquals(expected, actual);
    }

    @Test
    void toPostFullDto() {
        List<String> expectedTags = List.of();
        when(tagMapper.tagToString(anyList())).thenReturn(expectedTags);

        Post post = Util.getNewPost(100L);
        PostFullDto actual = postMapper.toPostFullDto(post, 100500L);

        verify(postMapper).mapImageBytesToBase64(post.getImage());
        verify(tagMapper).tagToString(post.getTags());
        verify(commentMapper).toPostCommentDto(post.getComments());
        assertEquals(post.getId(), actual.getId());
        assertEquals(post.getBody(), actual.getBody());
        assertEquals(post.getTitle(), actual.getTitle());
    }

    @Test
    void toPostPreviewDto() {
        Post post = Util.getNewPost(100L);

        PostPreviewDto actual = postMapper.toPostPreviewDto(post);

        verify(postMapper).shortPostBody(post.getBody());
        verify(postMapper).mapImageBytesToBase64(post.getImage());
        assertEquals(post.getId(), actual.getId());
        assertEquals(post.getTitle(), actual.getTitle());
    }

    @Test
    void toPost() {
        MultipartFile image = Mockito.mock(MultipartFile.class);
        PostEditDto dto = Util.getPostEditDto(100L, image);

        Post actual = postMapper.toPost(dto);

        verify(tagMapper).stringToTag(dto.tags());
        verify(postMapper).mapMultipartToBytes(dto.image());
        assertEquals(dto.id(), actual.getId());
        assertEquals(dto.body(), actual.getBody());
        assertEquals(dto.title(), actual.getTitle());
    }

    @Test
    void toPostEditDto() {
        Post post = Util.getNewPost(100L);

        PostEditDto actual = postMapper.toPostEditDto(post);

        verify(tagMapper).tagToString(post.getTags());
        assertNull(actual.image());
        assertEquals(post.getId(), actual.id());
        assertEquals(post.getBody(), actual.body());
        assertEquals(post.getTitle(), actual.title());
    }

    @Test
    void toFeedRowDto() {
        Post post = Util.getNewPost(100L);

        var actual = postMapper.toFeedRowDto(post, 100500L, 100L);

        verify(postMapper).toPostPreviewDto(post);
        assertEquals("100500", actual.getNumLikes());
        assertEquals("100", actual.getNumComments());
    }
}