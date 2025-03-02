package ru.practicum.myblog.dto.postfeed;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
public record PostEditDto(
        Long id,

        @NotBlank
        @Size(min = 5, max = 256)
        String title,

        @NotBlank
        String body,
        MultipartFile image,

        @Valid
        List<@Pattern(regexp = "^\\w{0,30}$") String> tags) {
}
