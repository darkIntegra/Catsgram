package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PostDto {
    private long id;
    private long userId;
    private String content;
    private Instant createdAt;
}