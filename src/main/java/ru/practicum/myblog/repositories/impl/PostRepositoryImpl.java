package ru.practicum.myblog.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.myblog.data.Post;
import ru.practicum.myblog.dto.postfeed.PostFilter;
import ru.practicum.myblog.repositories.PostRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Post> findById(long postId) {
        String sql = "select * from posts where id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makePost(rs), postId)
                .stream()
                .findAny();
    }

    private Post makePost(ResultSet rs) throws SQLException {
        return Post.builder()
                .id(rs.getLong("id"))
                .body(rs.getString("body"))
                .title(rs.getString("title"))
                .image(rs.getBytes("image"))
                .build();
    }

    @Override
    public List<Post> findByFilter(PostFilter filter) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("tagName", filter.getTagName());
        String sql = """
                select * from posts
                where :tagName is null or posts.id in
                    (select distinct posts_tags.post_id from posts_tags where tag_name = :tagName)
                """;
        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> makePost(rs));
    }

    @Override
    @Transactional
    public Post save(Post post) {
        return post.getId() == null ? createNewPost(post) : updateExistPost(post);
    }

    private Post updateExistPost(Post post) {
        String sql = "update posts set title=?, body=?, image=? where id=?";
        jdbcTemplate.update(sql,
                post.getTitle(),
                post.getBody(),
                post.getImage(),
                post.getId());
        return findById(post.getId()).orElseThrow();
    }


    @Override
    public void removeById(long postId) {
        String sql = "delete from posts where id = ?";
        jdbcTemplate.update(sql, postId);
    }

    private Post createNewPost(Post post) {
        Map<String, Object> row = new HashMap<>();
        row.put("title", post.getTitle());
        row.put("body", post.getBody());
        row.put("image", post.getImage());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("id");
        final long id = simpleJdbcInsert.executeAndReturnKey(row).longValue();

        return post.toBuilder().id(id).build();
    }
}
