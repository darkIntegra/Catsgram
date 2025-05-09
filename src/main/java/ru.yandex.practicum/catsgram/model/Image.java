package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Image {
    private long id;
    private long postId;
    private String imageUrl;
    private Instant uploadedAt;
}