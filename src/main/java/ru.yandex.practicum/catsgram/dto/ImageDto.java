package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ImageDto {
    private long id;
    private long postId;
    private String imageUrl;
    private Instant uploadedAt;
}