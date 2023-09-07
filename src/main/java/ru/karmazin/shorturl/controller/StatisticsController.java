package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.karmazin.shorturl.pojo.Statistics;
import ru.karmazin.shorturl.service.LinkRedirectService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Статистика", description = "Методы для получения статистики")
public class StatisticsController {

    private final LinkRedirectService linkRedirectService;

    public StatisticsController(LinkRedirectService linkRedirectService) {
        this.linkRedirectService = linkRedirectService;
    }

    @Operation(summary = "Статистика по дням")
    @GetMapping("/statistics/daily")
    public ResponseEntity<List<Statistics>> getDailyStatistics() {
        List<Statistics> dailyStatistics = linkRedirectService.getRedirectsByDay();
        return ResponseEntity.ok(dailyStatistics);
    }

    @Operation(summary = "Статистика по часам")
    @GetMapping("/statistics/hourly")
    public ResponseEntity<List<Statistics>> getHourlyStatistics() {
        List<Statistics> hourlyStatistics = linkRedirectService.getRedirectsByHour();
        return ResponseEntity.ok(hourlyStatistics);
    }

    @Operation(summary = "Статистика по минутам")
    @GetMapping("/statistics/minutly")
    public ResponseEntity<List<Statistics>> getMinuteStatistics() {
        List<Statistics> minuteStatistics = linkRedirectService.getRedirectsByMinute();
        return ResponseEntity.ok(minuteStatistics);
    }
}
