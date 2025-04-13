package ru.yandex.practicum.catsgram.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.catsgram.dto.PostDto;
import ru.yandex.practicum.catsgram.model.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostMapper {
    public static PostDto mapToPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}