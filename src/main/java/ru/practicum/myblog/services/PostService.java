package ru.practicum.myblog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.myblog.dto.postfeed.FeedRowDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.dto.postfeed.PostFullDto;

import java.util.Optional;

public interface PostService {
    Page<FeedRowDto> findPaginated(Pageable pageable, PostFilter filter);

    void savePost(PostEditDto postDto);

    Optional<PostEditDto> getEditablePostById(long id);

    PostEditDto getEmpty();

    PostFullDto getPostFullDto(long postId);

    void removePost(long postId);
}
