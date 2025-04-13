package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // Получить все посты
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // Создать новый пост
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestBody NewPostRequest request) {
        return postService.createPost(request);
    }

    // Получить пост по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPostById(@PathVariable long id) {
        return postService.getPostById(id);
    }
}