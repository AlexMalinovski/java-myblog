package ru.practicum.myblog.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.practicum.myblog.data.Tag;
import ru.practicum.myblog.repositories.TagRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Tag> getAvailableTags() {
        String sql = "select * from tags";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeTag(rs));
    }

    private Tag makeTag(ResultSet rs) throws SQLException {
        return new Tag(rs.getString("name"));
    }

    private Tag makePostsTagsTag(ResultSet rs) throws SQLException {
        return new Tag(rs.getString("tag_name"));
    }

    @Override
    public Tag save(Tag tag) {
        Map<String, Object> row = new HashMap<>();
        row.put("name", tag.getName());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("tags");
        simpleJdbcInsert.execute(row);

        return new Tag(tag.getName());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        String sql = "select * from tags where name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeTag(rs), name)
                .stream()
                .findAny();
    }

    @Override
    public List<Tag> findPostTags(long postId) {
        String sql = "select * from posts_tags where post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makePostsTagsTag(rs), postId);
    }

    @Override
    public void attachPostTag(long postId, Tag tag) {
        Map<String, Object> row = new HashMap<>();
        row.put("post_id", postId);
        row.put("tag_name", tag.getName());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts_tags");
        simpleJdbcInsert.execute(row);
    }

    @Override
    public void detachPostTags(Long postId) {
        String sql = "delete from posts_tags where post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

}
