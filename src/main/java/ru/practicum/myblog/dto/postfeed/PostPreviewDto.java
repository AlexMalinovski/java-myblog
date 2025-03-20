package ru.practicum.myblog.dto.postfeed;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class PostPreviewDto {
    private final Long id;
    private final String title;
    private final String shortBody;
    private final String image;
}
