package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageData {
    private final byte[] data; // Массив байтов, представляющий содержимое файла
    private final String name; // Название файла
}