package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        // Проверяем наличие email
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        // Проверяем уникальность email
        for (User existingUser : users.values()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }

        // Формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        // Сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    public User update(User updatedUser) {
        // Проверяем наличие id
        if (updatedUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        // Проверяем существование пользователя
        if (!users.containsKey(updatedUser.getId())) {
            throw new NotFoundException("Пользователь с id = " + updatedUser.getId() + " не найден");
        }

        // Получаем текущего пользователя
        User currentUser = users.get(updatedUser.getId());

        // Обновляем данные, если они указаны
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            // Проверяем уникальность нового email
            for (User otherUser : users.values()) {
                if (otherUser.getId() != updatedUser.getId() &&
                        otherUser.getEmail().equals(updatedUser.getEmail())) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }
            currentUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            currentUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            currentUser.setPassword(updatedUser.getPassword());
        }

        return currentUser;
    }

    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}