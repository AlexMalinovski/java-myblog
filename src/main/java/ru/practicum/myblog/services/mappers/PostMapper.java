package ru.practicum.myblog.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.dto.postfeed.FeedRowDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;
import ru.practicum.myblog.dto.postfeed.PostFullDto;
import ru.practicum.myblog.dto.postfeed.PostPreviewDto;
import ru.practicum.myblog.exceptoins.BadRequestException;

import java.io.IOException;
import java.util.Base64;

@Mapper(config = DefaultMapperConfig.class, uses = {TagMapper.class})
public interface PostMapper {
    @Named("mapImageBytesToResource")
    default ByteArrayResource mapImageBytesToResource(byte[] image) {
        if (image == null) {
            return null;
        }
        return new ByteArrayResource(image);
    }

    @Named("mapImageBytesToBase64")
    default String mapImageBytesToBase64(byte[] image) {
        if (image == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(image);
    }

    @Named("shortPostBody")
    default String shortPostBody(String body) {
        if (body == null) {
            return null;
        }
        return body.split("\\n")[0];
    }

    @Named("mapMultipartToBytes")
    default byte[] mapMultipartToBytes(MultipartFile image) {
        if (image == null) {
            return null;
        }
        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Изображение повреждено или отсутствует");
        }
    }

    @Mapping(target = "image", source = "src.image", qualifiedByName = "mapImageBytesToBase64")
    PostFullDto toPostFullDto(Post src, Long numLikes);

    @Mapping(target = "shortBody", source = "body", qualifiedByName = "shortPostBody")
    @Mapping(target = "image", source = "image", qualifiedByName = "mapImageBytesToBase64")
    PostPreviewDto toPostPreviewDto(Post src);

    @Mapping(target = "image", source = "image", qualifiedByName = "mapMultipartToBytes")
    Post toPost(PostEditDto src);

    @Mapping(target = "image", ignore = true)
    PostEditDto toPostEditDto(Post src);

    @Mapping(target = "postPreview", source = "src")
    FeedRowDto toFeedRowDto(Post src, Long numLikes, Long numComments);
}
