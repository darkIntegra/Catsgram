package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

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

        // Проверка параметров
        if (!"asc".equalsIgnoreCase(sort) && !"desc".equalsIgnoreCase(sort)) {
            throw new ParameterNotValidException("sort", "Получено: " + sort + ". Должно быть: asc или desc");
        }
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Размер должен быть больше нуля");
        }
        if (from < 0) {
            throw new ParameterNotValidException("from", "Начало выборки должно быть положительным числом");
        }

        // Создаем новый изменяемый список из данных, возвращаемых PostService
        List<Post> sortedPosts = new ArrayList<>(postService.findAll());

        // Сортируем по дате создания
        if ("asc".equalsIgnoreCase(sort)) {
            sortedPosts.sort(Comparator.comparing(Post::getPostDate));
        } else if ("desc".equalsIgnoreCase(sort)) {
            sortedPosts.sort((p1, p2) -> p2.getPostDate().compareTo(p1.getPostDate()));
        }

        // Применяем пагинацию
        if (from >= sortedPosts.size()) {
            return List.of(); // Пустой список, если начальный индекс выходит за пределы
        }
        int toIndex = Math.min(from + size, sortedPosts.size());
        return sortedPosts.subList(from, toIndex);
    }
}