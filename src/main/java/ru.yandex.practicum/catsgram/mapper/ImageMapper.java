package ru.yandex.practicum.catsgram.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.model.Image;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageMapper {
    public static ImageDto mapToImageDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setPostId(image.getPostId());
        dto.setImageUrl(image.getImageUrl());
        dto.setUploadedAt(image.getUploadedAt());
        return dto;
    }
}