package ru.practicum.myblog.dto.postfeed;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class PostCommentDto {
    private final Long id;
    private final String body;
}
