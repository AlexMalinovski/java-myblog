package ru.practicum.myblog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;
import ru.practicum.myblog.exceptoins.BadRequestException;
import ru.practicum.myblog.services.CommentService;
import ru.practicum.myblog.services.PostReactionService;
import ru.practicum.myblog.services.PostService;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final PostReactionService postReactionService;

    @GetMapping(BlogUrls.Posts.PostId.FULL)
    public String getPostById(@PathVariable(name = "postId") long postId, ModelMap model) {
        model.put("selectedPost", postService.getPostFullDto(postId));
        model.putIfAbsent("newComment", NewCommentDto.EMPTY);
        return "post";
    }

    @GetMapping(BlogUrls.Posts.PostId.Delete.FULL)
    public String deletePostById(@PathVariable(name = "postId") long postId) {
        postService.removePost(postId);
        return "redirect:" + BlogUrls.Posts.FULL;
    }

    @GetMapping(BlogUrls.Posts.PostId.Like.FULL)
    public String likePost(@PathVariable(name = "postId") long postId) {
        postReactionService.addLike(postId);
        return "redirect:" + "/posts/" + postId + "/";
    }

    @PostMapping(path = BlogUrls.Posts.PostId.Comments.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addPostComment(
            @ModelAttribute(name = "newComment") @Valid NewCommentDto newComment, BindingResult result,
            @PathVariable(name = "postId") long postId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("selectedPost", postService.getPostFullDto(postId));
            model.addAttribute("newComment", newComment);
            return "post";
        }
        commentService.createPostComment(postId, newComment);
        return "redirect:" + "/posts/" + postId + "/";
    }

    @PostMapping(path = BlogUrls.Posts.PostId.Comments.Edit.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editPostComment(
            @ModelAttribute(name = "editedComment") @Valid NewCommentDto editedComment, BindingResult result,
            @PathVariable(name = "postId") long postId) {
        if (result.hasErrors()) {
            throw new BadRequestException("Недопустимый комментарий");
        }
        commentService.updateComment(editedComment);
        return "redirect:" + "/posts/" + postId + "/";
    }
}
