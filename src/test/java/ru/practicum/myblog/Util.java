package ru.practicum.myblog;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.dto.postfeed.NewCommentDto;
import ru.practicum.myblog.dto.postfeed.PostEditDto;

import java.util.List;

@UtilityClass
public class Util {
    public NewCommentDto getNewCommentDto() {
        return new NewCommentDto(1L, "body");
    }

    public PostComment getNewPostComment(Long postId) {
        return PostComment.builder().id(1L).body("body").postId(postId).build();
    }

    public PostComment getNewPostComment() {
        return PostComment.builder().id(1L).body("body").build();
    }

    public Post getNewPost(long postId) {
        byte[] image = {1, 2};
        return Post.builder()
                .id(postId)
                .title("title")
                .body("body")
                .tags(getListTags())
                .image(image)
                .comments(List.of(getNewPostComment(postId)))
                .build();
    }

    public PostEditDto getPostEditDto(long id, MultipartFile image) {
        return new PostEditDto(id, "title", "body", image, List.of("tag1", "tag2"));
    }

    public List<Tag> getListTags() {
        return List.of(new Tag("tag1"), new Tag("tag2"));
    }
}
