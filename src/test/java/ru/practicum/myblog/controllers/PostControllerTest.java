package ru.practicum.myblog.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;
import ru.practicum.myblog.dto.postfeed.PostFullDto;
import ru.practicum.myblog.services.CommentService;
import ru.practicum.myblog.services.PostReactionService;
import ru.practicum.myblog.services.PostService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @Mock
    private PostReactionService postReactionService;

    @InjectMocks
    private PostController postController;

    @Test
    void getPostById() {
        PostFullDto post = PostFullDto.builder().build();
        ModelMap model = Mockito.mock(ModelMap.class);
        when(postService.getPostFullDto(1L)).thenReturn(post);

        var view = postController.getPostById(1L, model);

        assertEquals("post", view);
        verify(model).put("selectedPost", post);
        verify(model).putIfAbsent("newComment", NewCommentDto.EMPTY);
    }

    @Test
    void deletePostById() {
        var view = postController.deletePostById(1L);

        assertEquals("redirect:" + BlogUrls.Posts.FULL, view);
        verify(postService).removePost(1L);
    }

    @Test
    void likePost() {
        var view = postController.likePost(1L);

        assertEquals("redirect:" + "/posts/1/", view);
        verify(postReactionService).addLike(1L);
    }

    @Test
    void addPostComment_whenInvalid_thenContinueEdit() {
        NewCommentDto commentDto = new NewCommentDto(1L, "body");
        Model model = Mockito.mock(Model.class);
        BindingResult result = Mockito.mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        var view = postController.addPostComment(commentDto, result, 1L, model);

        assertEquals("post", view);
        verify(model).addAttribute("newComment", commentDto);
    }

    @Test
    void addPostComment_whenValid_thenSave() {
        NewCommentDto commentDto = new NewCommentDto(1L, "body");
        Model model = Mockito.mock(Model.class);
        BindingResult result = Mockito.mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        var view = postController.addPostComment(commentDto, result, 1L, model);

        assertEquals("redirect:" + "/posts/1/", view);
        verify(commentService).createPostComment(1L, commentDto);
    }

    @Test
    void editPostComment() {
        NewCommentDto commentDto = new NewCommentDto(1L, "body");
        BindingResult result = Mockito.mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        var view = postController.editPostComment(commentDto, result, 1L);

        assertEquals("redirect:" + "/posts/1/", view);
        verify(commentService).updateComment(commentDto);
    }
}