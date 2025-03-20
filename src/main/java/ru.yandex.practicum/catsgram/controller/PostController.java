package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        if (size <= 0) {
            throw new IllegalArgumentException("Параметр size должен быть больше нуля");
        }

        // Преобразуем Map в список для сортировки
        List<Post> sortedPosts = new ArrayList<>(posts.values());

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
            return Collections.emptyList(); // Если начальный индекс выходит за пределы списка
        }
        int toIndex = Math.min(from + size, sortedPosts.size());
        return sortedPosts.subList(from, toIndex);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Long id) {
        Post post = posts.get(id);
        if (post == null) {
            throw new NotFoundException("Пост с id = " + id + " не найден");
        }
        return post;
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        // Проверяем, что описание не пустое
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        // Проверяем существование автора поста
        Long authorId = post.getAuthorId();
        if (authorId == null) {
            throw new ConditionsNotMetException("Идентификатор автора не может быть null");
        }

        Optional<User> author = userService.findUserById(authorId);
        if (author.isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + authorId + " не найден");
        }

        // Формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}