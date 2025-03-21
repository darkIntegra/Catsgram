package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Параметр size должен быть больше нуля");
        }

        // Создаем новый изменяемый список из данных, возвращаемых PostService
        List<Post> sortedPosts = new ArrayList<>(postService.findAll());

        // Сортируем по дате создания
        if ("asc".equalsIgnoreCase(sort)) {
            sortedPosts.sort(Comparator.comparing(Post::getPostDate));
        } else if ("desc".equalsIgnoreCase(sort)) {
            sortedPosts.sort((p1, p2) -> p2.getPostDate().compareTo(p1.getPostDate()));
        } else {
            throw new IllegalArgumentException("Неверное значение параметра sort. Допустимые значения: asc, desc");
        }

        // Применяем пагинацию
        if (from >= sortedPosts.size()) {
            return List.of(); // Пустой список, если начальный индекс выходит за пределы
        }
        int toIndex = Math.min(from + size, sortedPosts.size());
        return sortedPosts.subList(from, toIndex);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Long id) {
        return postService.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост с id = " + id + " не найден"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        post.setPostDate(Instant.now());
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post updatedPost) {
        if (updatedPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        return postService.update(updatedPost);
    }
}