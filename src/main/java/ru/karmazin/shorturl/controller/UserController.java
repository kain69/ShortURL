package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.karmazin.shorturl.dto.UserDto;
import ru.karmazin.shorturl.model.User;
import ru.karmazin.shorturl.pojo.MessageResponse;
import ru.karmazin.shorturl.service.UserDetailsServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api/")
@Tag(name = "Пользователь", description = "Методы для работы с пользователями")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;

    public UserController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Получение информации о пользователе по id")
    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
        Optional<User> optionalUser = userDetailsService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        User user = optionalUser.get();
        return ResponseEntity.ok(new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRoles(),
                user.getUrls()
        ));
    }
}
