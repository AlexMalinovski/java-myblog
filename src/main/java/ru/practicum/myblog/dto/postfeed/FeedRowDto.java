package ru.practicum.myblog.dto.postfeed;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder(toBuilder = true)
@Getter
public class FeedRowDto {
    private final PostPreviewDto postPreview;
    private final String numComments;
    private final String numLikes;
    private final Set<String> tags;
}
