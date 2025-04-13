package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.ImageRepository;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.mapper.ImageMapper;
import ru.yandex.practicum.catsgram.model.Image;

import java.time.Instant;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<ImageDto> getAllImages() {
        return imageRepository.findAll().stream()
                .map(ImageMapper::mapToImageDto)
                .toList();
    }

    public ImageDto createImage(NewImageRequest request) {
        Image image = new Image();
        image.setPostId(request.getPostId());
        image.setImageUrl(request.getImageUrl());
        image.setUploadedAt(Instant.now());
        return ImageMapper.mapToImageDto(imageRepository.save(image));
    }

    public List<ImageDto> getImagesByPostId(long postId) {
        return imageRepository.findByPostId(postId).stream()
                .map(ImageMapper::mapToImageDto)
                .toList();
    }
}