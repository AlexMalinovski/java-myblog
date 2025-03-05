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
import ru.practicum.myblog.dto.postfeed.NewCommentDto;
import ru.practicum.myblog.repositories.PostRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerItTest extends AbstractIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Override
    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        super.setUp();
    }

    @Test
    @SneakyThrows
    void getPostById() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        mockMvc.perform(get(BlogUrls.Posts.PostId.FULL.replaceAll("\\{postId}", post.getId().toString())))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deletePostById() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        mockMvc.perform(get(BlogUrls.Posts.PostId.Delete.FULL.replaceAll("\\{postId}", post.getId().toString())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BlogUrls.Posts.FULL));
    }

    @Test
    @SneakyThrows
    void likePost() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        mockMvc.perform(get(BlogUrls.Posts.PostId.Like.FULL.replaceAll("\\{postId}", post.getId().toString())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + post.getId() + "/"));
    }

    @Test
    @SneakyThrows
    void addPostComment() {
        Post post = postRepository.save(Post.builder().title("title").body("body").build());
        mockMvc.perform(post(BlogUrls.Posts.PostId.Comments.FULL.replaceAll("\\{postId}", post.getId().toString()))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(objectMapper.writeValueAsBytes(new NewCommentDto(null, "body"))))
                .andExpect(status().isOk());
    }
}