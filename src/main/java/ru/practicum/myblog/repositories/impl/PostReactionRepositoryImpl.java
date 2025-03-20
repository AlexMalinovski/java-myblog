package ru.practicum.myblog.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.practicum.myblog.constants.ReactionType;
import ru.practicum.myblog.data.PostReaction;
import ru.practicum.myblog.repositories.PostReactionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostReactionRepositoryImpl implements PostReactionRepository {
    private final JdbcTemplate jdbcTemplate;

    private record PostNumReactions(Long postId, Long cnt) {
    }

    @Override
    public Map<Long, Long> getPostsNumReactions(Set<Long> postsIds, ReactionType reactionType) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("postsIds", postsIds)
                .addValue("reactionType", reactionType.name());

        String sql = """
                select post_id, count(user_id) as cnt from posts_reactions
                group by post_id
                having type = :reactionType and post_id in (:postsIds);
                """;
        return namedParameterJdbcTemplate.query(sql, namedParameters,
                        (rs, rowNum) -> makePostsNumReactions(rs))
                .stream()
                .collect(Collectors.toMap(PostNumReactions::postId, PostNumReactions::cnt));
    }

    @Override
    public PostReaction createNewPostReaction(PostReaction reaction) {
        Map<String, Object> row = new HashMap<>();
        row.put("post_id", reaction.getPostId());
        row.put("user_id", reaction.getUserId());
        row.put("type", reaction.getType().name());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts_reactions");
        simpleJdbcInsert.execute(row);
        return reaction.toBuilder().build();
    }

    private PostNumReactions makePostsNumReactions(ResultSet rs) throws SQLException {
        return new PostNumReactions(rs.getLong("post_id"), rs.getLong("cnt"));
    }
}
