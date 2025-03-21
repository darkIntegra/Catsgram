package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Data
@EqualsAndHashCode(of = { "email" }) // Уникальность по email
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;
}