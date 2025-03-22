package ru.practicum.myblog.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.data.PostReaction;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.repositories.CommentRepository;
import ru.practicum.myblog.repositories.PostReactionRepository;
import ru.practicum.myblog.repositories.PostRepository;
import ru.practicum.myblog.repositories.TagRepository;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Bean(name = "dbInitializer")
    ApplicationRunner dbInitializer() {
        return (arguments) -> {
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            var post1 = postRepository.save(Post.builder().title("Заголовок").body("тело сообщения").build());
            var post2 = postRepository.save(Post.builder().title("title2").body("body2").build());
            var post3 = postRepository.save(Post.builder().title("title3").body("body3").build());

            tagRepository.attachPostTag(post1.getId(), tag1);
            tagRepository.attachPostTag(post1.getId(), tag2);
            tagRepository.attachPostTag(post2.getId(), tag1);

            var like1 = PostReaction.builder().postId(post1.getId()).userId(1L).type(ReactionType.LIKE).build();
            var like2 = PostReaction.builder().postId(post1.getId()).userId(2L).type(ReactionType.LIKE).build();
            postReactionRepository.createNewPostReaction(like1);
            postReactionRepository.createNewPostReaction(like2);

            PostComment comment1 = PostComment.builder().postId(post1.getId()).body("comment 1 body").build();
            PostComment comment2 = PostComment.builder().postId(post1.getId()).body("comment 2 body").build();
            PostComment comment3 = PostComment.builder().postId(post1.getId()).body("comment 3 body").build();
            commentRepository.save(comment1);
            commentRepository.save(comment2);
            commentRepository.save(comment3);
        };
    }

}
