package ru.practicum.myblog.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReactionType {
    LIKE("Лайк");

    private final String description;
}
