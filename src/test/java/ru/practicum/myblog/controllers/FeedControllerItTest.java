package ru.practicum.myblog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.myblog.AbstractIntegrationTest;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.repositories.PostRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FeedControllerItTest extends AbstractIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostRepository postRepository;
    private MockMvc mockMvc;

    @Override
    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        super.setUp();
    }

    @Test
    @SneakyThrows
    void setPostFilter() {
        mockMvc.perform(post(BlogUrls.Posts.Filter.FULL)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsBytes(new PostFilter("tag"))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BlogUrls.Posts.FULL));
    }

    @Test
    @SneakyThrows
    void resetPostFilter() {
        mockMvc.perform(get(BlogUrls.Posts.Filter.Reset.FULL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BlogUrls.Posts.FULL));
    }

    @Test
    @SneakyThrows
    void getPosts() {
        mockMvc.perform(get(BlogUrls.Posts.FULL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @SneakyThrows
    void editPost() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        mockMvc.perform(get(BlogUrls.Posts.Editor.FULL + String.format("?id=%d", post.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("editPost"));
    }

    @Test
    @SneakyThrows
    void editPost_whenCreate() {
        mockMvc.perform(get(BlogUrls.Posts.Editor.FULL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("editPost"));
    }

    @Test
    @SneakyThrows
    void savePost() {
        PostEditDto post = new PostEditDto(null, "title", "Тело поста", null, List.of());
        mockMvc.perform(post(BlogUrls.Posts.Editor.FULL)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsBytes(post)))
                .andExpect(status().isOk());
    }
}