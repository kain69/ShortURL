package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.karmazin.shorturl.dto.UrlCreateDto;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.model.User;
import ru.karmazin.shorturl.pojo.MessageResponse;
import ru.karmazin.shorturl.service.UrlService;
import ru.karmazin.shorturl.service.UserDetailsServiceImpl;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
@Tag(name = "Ссылка", description = "Методы для работы с ссылками")
public class UrlController {
    private final UrlService urlService;
    private final UserDetailsServiceImpl userDetailsService;

    public UrlController(UrlService urlService,
                         UserDetailsServiceImpl userDetailsService) {
        this.urlService = urlService;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Получение информации о всех ссылках")
    @GetMapping("url")
    public List<UrlDto> getAll() {
        return urlService.getAll();
    }

    @Operation(summary = "Получение информации о ссылке по id")
    @GetMapping("url/{id}")
    public ResponseEntity<?> getUrl(@PathVariable int id) {
        Optional<Url> optionalUrl = urlService.getUrlById(id);
        if(urlService.getUrlById(id).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Url not found"));
        }
        Url url = optionalUrl.get();
        return ResponseEntity.ok(new UrlDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests(),
                url.getUser().getId()
        ));
    }

    @Operation(summary = "Создание короткой ссылки для пользователя")
    @PostMapping("/user/{userId}/url")
    public ResponseEntity<?> createShortUrl(@PathVariable Long userId,
                                 @RequestBody UrlCreateDto request) {
        Optional<User> user = userDetailsService.getUserById(userId);
        if (user.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        UrlDto shortUrl = urlService.createShortUrl(request, user.get());
        return ResponseEntity.ok(shortUrl);
    }

    @Operation(summary = "Получить ссылки у пользователя по его id")
    @GetMapping("/user/{userId}/urls")
    public ResponseEntity<?> getUrlsForUser(@PathVariable Long userId) {
        Optional<User> user = userDetailsService.getUserById(userId);
        if (user.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        List<UrlDto> urls = urlService.getUrlsForUser(user.get());
        return ResponseEntity.ok(urls);
    }

    @Operation(summary = "Получить ссылку по id у пользователя по id")
    @GetMapping("/user/{userId}/url/{urlId}")
    public ResponseEntity<?> getUrlForUser(@PathVariable Long userId,
                                                @PathVariable Long urlId) {
        Optional<User> user = userDetailsService.getUserById(userId);
        if (user.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        Optional<Url> optionalUrl = urlService.getUrlByIdForUser(user.get(), urlId);
        if (optionalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Url not found"));
        }
        Url url = optionalUrl.get();
        return ResponseEntity.ok(new UrlDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests(),
                url.getUser().getId()
        ));
    }

    @Operation(summary = "Удалить ссылку у пользователя")
    @DeleteMapping("/user/{userId}/url/{urlId}")
    public ResponseEntity<?> deleteUrlForUser(@PathVariable Long userId,
                                                       @PathVariable Long urlId) {
        Optional<User> user = userDetailsService.getUserById(userId);
        if (user.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        Optional<Url> optionalUrl = urlService.getUrlByIdForUser(user.get(), urlId);
        if (optionalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Url not found"));
        }

        Url url = optionalUrl.get();
        urlService.delete(url.getId());
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Редирект по короткой ссылке")
    @GetMapping("{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        String url;
        try {
            url = urlService.redirectToOriginalUrl(shortUrl);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
