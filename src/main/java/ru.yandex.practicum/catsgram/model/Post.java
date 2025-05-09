package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Post {
    private long id;
    private long userId;
    private String content;
    private Instant createdAt;
}