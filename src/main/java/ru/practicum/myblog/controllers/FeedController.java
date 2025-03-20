package ru.practicum.myblog.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.practicum.myblog.dto.postfeed.FeedRowDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.exceptoins.NotFoundException;
import ru.practicum.myblog.services.PostService;
import ru.practicum.myblog.services.TagService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@SessionAttributes("postFilter")
@RequiredArgsConstructor
public class FeedController {
    private static final List<Integer> DEFAULT_PAGE_SIZES = List.of(10, 20, 50);
    private final TagService tagService;
    private final PostService postService;

    @ModelAttribute("availableTags")
    public List<String> getAvailableTags() {
        return tagService.getAvailableTags();
    }

    @ModelAttribute("availableSizes")
    public List<Integer> getAvailablePageSizes() {
        return DEFAULT_PAGE_SIZES;
    }

    @ModelAttribute("postFilter")
    public PostFilter getFilter() {
        return new PostFilter();
    }

    @PostMapping(path = BlogUrls.Posts.Filter.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String setPostFilter(
            @ModelAttribute("postFilter") PostFilter postFilter) {
        return "redirect:" + BlogUrls.Posts.FULL;
    }

    @GetMapping(path = BlogUrls.Posts.Filter.Reset.FULL)
    public String resetPostFilter(Model model) {
        model.addAttribute("postFilter", new PostFilter());
        return "redirect:" + BlogUrls.Posts.FULL;
    }

    @GetMapping(BlogUrls.Posts.FULL)
    public String getPosts(
            Model model,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1", required = false) int pageNumber) {

        var postFilter = (PostFilter) model.asMap().getOrDefault("postFilter", new PostFilter());
        Page<FeedRowDto> postPage = postService.findPaginated(
                PageRequest.of(pageNumber - 1, pageSize), postFilter);

        model.addAttribute("postPage", postPage);
        int totalPages = postPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "feed";
    }

    @GetMapping(BlogUrls.Posts.Editor.FULL)
    public String editPost(@RequestParam(name = "id", required = false) Long id, Model model) {
        PostEditDto postEdit = Optional.ofNullable(id)
                .map(postService::getEditablePostById)
                .orElseGet(() -> Optional.of(postService.getEmpty()))
                .orElseThrow(() -> new NotFoundException(String.format("Пост с id=%d не найден", id)));
        model.addAttribute("editPost", postEdit);
        return "editor";
    }

    @PostMapping(path = BlogUrls.Posts.Editor.FULL, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String savePost(
            @ModelAttribute(name = "editPost") @Valid PostEditDto editPost, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("editPost", editPost);
            return "editor";
        }
        postService.savePost(editPost);
        return "redirect:" + BlogUrls.Posts.FULL;
    }
}
