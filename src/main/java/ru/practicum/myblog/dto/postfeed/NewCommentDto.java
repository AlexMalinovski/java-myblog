package ru.practicum.myblog.dto.postfeed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCommentDto(
        Long id,

        @NotBlank
        @Size(min = 3, max = 1024)
        String body
) {
        public static final NewCommentDto EMPTY = new NewCommentDto(null, null);
}
