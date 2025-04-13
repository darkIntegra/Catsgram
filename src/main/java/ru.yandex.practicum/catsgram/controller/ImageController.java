package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.ImageDto;
import ru.yandex.practicum.catsgram.dto.NewImageRequest;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    // Получить все изображения
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ImageDto> getAllImages() {
        return imageService.getAllImages();
    }

    // Создать новое изображение
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDto createImage(@RequestBody NewImageRequest request) {
        return imageService.createImage(request);
    }

    // Получить изображения для конкретного поста
    @GetMapping("/post/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ImageDto> getImagesByPostId(@PathVariable long postId) {
        return imageService.getImagesByPostId(postId);
    }
}