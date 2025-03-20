package ru.practicum.myblog.dto.postfeed;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class PostFullDto {
    private final Long id;
    private final String title;
    private final String body;
    private final String image;
    private final String numLikes;
    private final List<String> tags;
    private final List<PostCommentDto> comments;
}
