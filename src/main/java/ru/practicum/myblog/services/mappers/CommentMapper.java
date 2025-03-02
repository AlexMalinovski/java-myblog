package ru.practicum.myblog.services.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;

@Mapper(config = DefaultMapperConfig.class)
public interface CommentMapper {
    PostComment toPostComment(NewCommentDto src, Long postId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postId", ignore = true)
    PostComment updateComment(@MappingTarget PostComment.PostCommentBuilder builder, NewCommentDto editedComment);
}
