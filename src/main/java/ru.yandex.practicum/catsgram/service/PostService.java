package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public List<Post> findAll() {
        return List.copyOf(posts.values());
    }

    public Post create(Post post) {
        long nextId = getNextId();
        post.setId(nextId);
        posts.put(nextId, post);
        return post;
    }

    public Post update(Post updatedPost) {
        if (posts.containsKey(updatedPost.getId())) {
            posts.put(updatedPost.getId(), updatedPost);
            return updatedPost;
        }
        throw new NotFoundException("Пост с id = " + updatedPost.getId() + " не найден");
    }

    private long getNextId() {
        return posts.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}