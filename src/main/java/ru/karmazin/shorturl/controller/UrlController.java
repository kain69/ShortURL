package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.karmazin.shorturl.dto.UrlCreateDto;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.service.UrlService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/")
@Tag(name = "Ссылка", description = "Методы для работы с ссылками")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Получение информации о всех ссылках")
    @GetMapping("url")
    public List<Url> getAll(){
        return urlService.getAll();
    }

    @Operation(summary = "Получение информации о ссылке по id")
    @GetMapping("url/{id}")
    public Url getUrl(@PathVariable int id) {
        return urlService.getUrlById(id);
    }

    // TODO: После создания нового URL индексы увеличиваются на 50, исправить
    @Operation(summary = "Создание короткой ссылки")
    @PostMapping("url")
    public UrlDto createShortUrl(@RequestBody UrlCreateDto request) {
        return urlService.createShortUrl(request);
    }

    @Operation(summary = "Удаление короткой ссылки")
    @DeleteMapping("url/{id}")
    public ResponseEntity<HttpStatus> deleteUrl(@PathVariable("id") int id) {
        urlService.delete(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Редирект по короткой ссылке")
    @GetMapping("{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        var url = urlService.redirectToOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
