package ru.practicum.myblog.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.practicum.myblog.dto.postfeed.FeedRowDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.services.PostService;
import ru.practicum.myblog.services.TagService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {
    @Mock
    private TagService tagService;

    @Mock
    private PostService postService;

    @InjectMocks
    private FeedController feedController;

    @Test
    void getAvailableTags() {
        List<String> expected = List.of();
        when(tagService.getAvailableTags()).thenReturn(expected);

        var actual = feedController.getAvailableTags();

        verify(tagService).getAvailableTags();
        assertEquals(expected, actual);
    }

    @Test
    void getAvailablePageSizes() {
        var actual = feedController.getAvailablePageSizes();
        assertEquals(List.of(10, 20, 50), actual);
    }

    @Test
    void getFilter() {
        var actual = feedController.getFilter();
        assertEquals(new PostFilter(), actual);
    }

    @Test
    void resetPostFilter() {
        Model model = Mockito.mock(Model.class);

        feedController.resetPostFilter(model);

        verify(model).addAttribute("postFilter", new PostFilter());
    }

    @Test
    void getPosts() {
        Page<FeedRowDto> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 40);
        Model model = Mockito.mock(Model.class);
        when(postService.findPaginated(any(), any())).thenReturn(page);

        feedController.getPosts(model, 10, 1);

        verify(model).addAttribute("pageNumbers", List.of(1, 2, 3, 4));
        verify(model).addAttribute("postPage", page);
    }

    @Test
    void editPost_whenId_thenEditExist() {
        Model model = Mockito.mock(Model.class);
        PostEditDto post = new PostEditDto(1L, "title", "body", null, null);
        when(postService.getEditablePostById(1L)).thenReturn(Optional.of(post));

        feedController.editPost(1L, model);

        verify(postService).getEditablePostById(1L);
        verify(model).addAttribute("editPost", post);
    }

    @Test
    void editPost_whenNullId_thenCreateNew() {
        Model model = Mockito.mock(Model.class);
        PostEditDto post = new PostEditDto(1L, null, null, null, null);
        when(postService.getEmpty()).thenReturn(post);

        feedController.editPost(null, model);

        verify(postService).getEmpty();
        verify(model).addAttribute("editPost", post);
    }

    @Test
    void savePost_whenInvalid_thenContinueEditing() {
        BindingResult result = Mockito.mock(BindingResult.class);
        Model model = Mockito.mock(Model.class);
        PostEditDto post = new PostEditDto(1L, "title", "body", null, null);
        when(result.hasErrors()).thenReturn(true);

        var view = feedController.savePost(post, result, model);

        assertEquals("editor", view);
        verify(model).addAttribute("editPost", post);
    }

    @Test
    void savePost_whenValid_thenSave() {
        BindingResult result = Mockito.mock(BindingResult.class);
        Model model = Mockito.mock(Model.class);
        PostEditDto post = new PostEditDto(1L, "title", "body", null, null);
        when(result.hasErrors()).thenReturn(false);

        var view = feedController.savePost(post, result, model);

        assertEquals("redirect:" + BlogUrls.Posts.FULL, view);
        verify(postService).savePost(post);
    }
}