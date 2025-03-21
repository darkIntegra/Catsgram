package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Post {
    private Long id;
    private Long authorId; // Идентификатор автора
    private String description;
    private Instant postDate;
}