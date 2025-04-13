package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.model.Image;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class ImageRepository extends BaseRepository<Image> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM image_storage";
    private static final String FIND_BY_POST_ID_QUERY = "SELECT * FROM image_storage WHERE post_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO image_storage(post_id, image_url, uploaded_at) VALUES (?, ?, ?) RETURNING id";

    public ImageRepository(JdbcTemplate jdbc, RowMapper<Image> mapper) {
        super(jdbc, mapper);
    }

    public List<Image> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<Image> findByPostId(long postId) {
        return findMany(FIND_BY_POST_ID_QUERY, postId);
    }

    public Image save(Image image) {
        long id = insert(
                INSERT_QUERY,
                image.getPostId(),
                image.getImageUrl(),
                Timestamp.from(image.getUploadedAt())
        );
        image.setId(id);
        return image;
    }
}