package ru.practicum.myblog.services.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.myblog.Util;
import ru.practicum.myblog.data.Tag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagMapperTest {

    private final TagMapper tagMapper = new TagMapperImpl();

    @Test
    void tagToString() {
        Tag tag = Util.getListTags().getFirst();

        var actual = tagMapper.tagToString(tag);

        assertEquals(tag.getName(), actual);
    }

    @Test
    void stringToTag() {
        String str = " tAg  ";

        var actual = tagMapper.stringToTag(str);

        assertEquals("tag", actual.getName());
    }

    @Test
    void testStringToTag() {
        var str = List.of(" tAg  ");

        var actual = tagMapper.stringToTag(str);

        assertEquals(1, actual.size());
        assertEquals("tag", actual.getFirst().getName());
    }

    @Test
    void tagToString_List() {
        var tag = Util.getListTags();

        var actual = tagMapper.tagToString(tag);

        assertEquals(2, actual.size());
        assertEquals(tag.getFirst().getName(), actual.getFirst());
    }
}