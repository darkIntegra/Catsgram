package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

@Data
public class NewPostRequest {
    private long userId;
    private String content;
}