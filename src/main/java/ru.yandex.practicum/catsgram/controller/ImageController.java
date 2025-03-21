package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Image> addImages(@PathVariable("postId") long postId,
                                 @RequestParam("files") List<MultipartFile> files) {
        return imageService.saveImages(postId, files);
    }

    @GetMapping
    public List<Image> getImages(@PathVariable("postId") long postId) {
        return imageService.getPostImages(postId);
    }
}