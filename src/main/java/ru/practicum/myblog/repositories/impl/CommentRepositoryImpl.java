package ru.practicum.myblog.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.practicum.myblog.data.PostComment;
import ru.practicum.myblog.repositories.CommentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    private record PostNumComments(Long postId, Long cnt) {}

    @Override
    public Map<Long, Long> getPostsNumComments(Set<Long> postsIds) {
        String inSql = String.join(",", Collections.nCopies(postsIds.size(), "?"));
        String sql = "select post_id, count(id) as cnt from post_comments group by post_id having post_id in (%s)";
        return jdbcTemplate.query(String.format(sql, inSql),
                        (rs, rowNum) -> makePostsNumComments(rs), postsIds.toArray())
                .stream()
                .collect(Collectors.toMap(PostNumComments::postId, PostNumComments::cnt));
    }

    @Override
    public PostComment save(PostComment comment) {
        return comment.getId() == null ? createNewComment(comment) : updateExistComment(comment);
    }

    @Override
    public Optional<PostComment> findById(Long id) {
        String sql = "select * from post_comments where id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makePostComment(rs), id)
                .stream()
                .findAny();
    }

    @Override
    public List<PostComment> getPostComments(long postId) {
        String sql = "select * from post_comments where post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makePostComment(rs), postId);
    }

    private PostComment makePostComment(ResultSet rs) throws SQLException {
        return PostComment.builder()
                .id(rs.getLong("id"))
                .postId(rs.getLong("post_id"))
                .body(rs.getString("body"))
                .build();
    }

    private PostComment updateExistComment(PostComment comment) {
        String sql = "update post_comments set body=? where id=?";
        jdbcTemplate.update(sql, comment.getBody(), comment.getId());
        return findById(comment.getId()).orElseThrow();
    }

    private PostComment createNewComment(PostComment comment) {
        Map<String, Object> row = new HashMap<>();
        row.put("body", comment.getBody());
        row.put("post_id", comment.getPostId());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("post_comments")
                .usingGeneratedKeyColumns("id");
        final long id = simpleJdbcInsert.executeAndReturnKey(row).longValue();

        return comment.toBuilder().id(id).build();
    }

    private PostNumComments makePostsNumComments(ResultSet rs) throws SQLException {
        return new PostNumComments(rs.getLong("post_id"), rs.getLong("cnt"));
    }
}
