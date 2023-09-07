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
    public List<Url> getAll() {
        return urlService.getAll();
    }

    @Operation(summary = "Получение информации о ссылке по id")
    @GetMapping("url/{id}")
    public Url getUrl(@PathVariable int id) {
        return urlService.getUrlById(id);
    }

    @Operation(summary = "Создание короткой ссылки для пользователя")
    @PostMapping("/user/{userId}/url")
    public UrlDto createShortUrl(@PathVariable Long userId,
                                 @RequestBody UrlCreateDto request) {
        User user = userDetailsService.getUserById(userId);
        return urlService.createShortUrl(request, user);
    }

    @Operation(summary = "Получить ссылки у пользователя по его id")
    @GetMapping("/user/{userId}/urls")
    public ResponseEntity<List<UrlDto>> getUrlsForUser(@PathVariable Long userId) {
        User user = userDetailsService.getUserById(userId);

        if (user != null) {
            List<UrlDto> urls = urlService.getUrlsForUser(user);
            return ResponseEntity.ok(urls);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить ссылку по id у пользователя по id")
    @GetMapping("/user/{userId}/url/{urlId}")
    public ResponseEntity<UrlDto> getUrlForUser(@PathVariable Long userId,
                                                @PathVariable Long urlId) {
        User user = userDetailsService.getUserById(userId);

        if (user != null) {
            Optional<Url> optionalUrl = urlService.getUrlByIdForUser(user, urlId);
            if (optionalUrl.isPresent()) {
                Url url = optionalUrl.get();
                return ResponseEntity.ok(new UrlDto(
                        url.getOriginalUrl(),
                        url.getShortUrl(),
                        url.getCreatedDate(),
                        url.getCountRequests(),
                        url.getUser().getId()
                ));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Удалить ссылку у пользователя")
    @DeleteMapping("/user/{userId}/url/{urlId}")
    public ResponseEntity<HttpStatus> deleteUrlForUser(@PathVariable Long userId,
                                                       @PathVariable Long urlId) {
        User user = userDetailsService.getUserById(userId);

        if (user != null) {
            Optional<Url> optionalUrl = urlService.getUrlByIdForUser(user, urlId);
            if (optionalUrl.isPresent()) {
                Url url = optionalUrl.get();
                urlService.delete(url.getId());
                return ResponseEntity.ok(HttpStatus.ACCEPTED);
            }
        }
        return ResponseEntity.badRequest().build();
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
