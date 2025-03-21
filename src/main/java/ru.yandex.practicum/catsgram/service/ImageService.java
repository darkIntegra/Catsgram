package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final Map<Long, Image> images = new HashMap<>();
    private final String imageDirectory = "uploads/";

    private final PostService postService; // Внедрение PostService

    private Path saveFile(MultipartFile file, Post post) throws IOException {
        String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                StringUtils.getFilenameExtension(file.getOriginalFilename()));

        Path uploadPath = Paths.get(imageDirectory, String.valueOf(post.getAuthorId()), post.getId().toString());
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        file.transferTo(filePath);
        return filePath;
    }

    public List<Image> saveImages(long postId, List<MultipartFile> files) {
        return files.stream()
                .map(file -> saveImage(postId, file))
                .collect(Collectors.toList());
    }

    private Image saveImage(long postId, MultipartFile file) {
        Post post = postService.findById(postId)
                .orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));

        try {
            Path filePath = saveFile(file, post);

            long imageId = getNextId();
            Image image = new Image();
            image.setId(imageId);
            image.setFilePath(filePath.toString());
            image.setPostId(postId);
            image.setOriginalFileName(file.getOriginalFilename());

            images.put(imageId, image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    public List<Image> getPostImages(long postId) {
        return images.values().stream()
                .filter(image -> image.getPostId() == postId)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        return images.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}