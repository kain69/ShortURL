package ru.karmazin.shorturl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.pojo.Statistics;
import ru.karmazin.shorturl.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Статистика", description = "Методы для получения статистики")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Operation(summary = "Статистика по дням")
    @GetMapping("/statistics/daily")
    public ResponseEntity<List<Statistics>> getDailyStatistics() {
        List<Statistics> dailyStatistics = statisticsService.getRedirectsByDay();
        return ResponseEntity.ok(dailyStatistics);
    }

    @Operation(summary = "Статистика по часам")
    @GetMapping("/statistics/hourly")
    public ResponseEntity<List<Statistics>> getHourlyStatistics() {
        List<Statistics> hourlyStatistics = statisticsService.getRedirectsByHour();
        return ResponseEntity.ok(hourlyStatistics);
    }

    @Operation(summary = "Статистика по минутам")
    @GetMapping("/statistics/minutly")
    public ResponseEntity<List<Statistics>> getMinuteStatistics() {
        List<Statistics> minuteStatistics = statisticsService.getRedirectsByMinute();
        return ResponseEntity.ok(minuteStatistics);
    }

    @Operation(summary = "Топ 20 ссылок по переходам")
    @GetMapping("/statistics/top")
    public ResponseEntity<List<UrlDto>> getTopUrl() {
        return ResponseEntity.ok(statisticsService.getTop20Urls());
    }
}
