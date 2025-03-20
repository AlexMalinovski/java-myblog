package ru.practicum.myblog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.myblog.configs.WebConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public abstract class AbstractIntegrationTest {
    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    protected void setUp() {
        jdbcTemplate.execute("DELETE FROM post_comments");
        jdbcTemplate.execute("DELETE FROM posts_reactions");
        jdbcTemplate.execute("DELETE FROM posts_tags");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM posts");
    }

}
