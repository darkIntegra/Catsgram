package ru.yandex.practicum.catsgram.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.model.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class ImageRowMapper implements RowMapper<Image> {
    @Override
    public Image mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Image image = new Image();
        image.setId(resultSet.getLong("id"));
        image.setPostId(resultSet.getLong("post_id"));
        image.setImageUrl(resultSet.getString("image_url"));

        Timestamp uploadedAt = resultSet.getTimestamp("uploaded_at");
        image.setUploadedAt(uploadedAt.toInstant());

        return image;
    }
}