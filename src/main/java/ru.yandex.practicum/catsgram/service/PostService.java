package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostMapper::mapToPostDto)
                .toList();
    }

    public PostDto createPost(NewPostRequest request) {
        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setContent(request.getContent());
        post.setCreatedAt(Instant.now());
        return PostMapper.mapToPostDto(postRepository.save(post));
    }

    public PostDto getPostById(long id) {
        return postRepository.findById(id)
                .map(PostMapper::mapToPostDto)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}