package ru.practicum.myblog.services.mappers;

import org.mapstruct.Mapper;
import ru.practicum.myblog.data.Tag;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper(config = DefaultMapperConfig.class)
public interface TagMapper {
    default String tagToString(Tag src) {
        return src == null ? null : src.getName();
    }

    default Tag stringToTag(String name) {
        return Optional.ofNullable(name)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(String::toLowerCase)
                .map(Tag::new)
                .orElse(null);
    }

    default List<Tag> stringToTag(List<String> src) {
        if (src == null) {
            return null;
        }
        return src.stream()
                .filter(Objects::nonNull)
                .map(this::stringToTag)
                .toList();
    }

    List<String> tagToString(List<Tag> src);
}
