package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

@Data
public class NewImageRequest {
    private long postId;
    private String imageUrl;
}