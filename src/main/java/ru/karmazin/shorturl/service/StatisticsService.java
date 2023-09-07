package ru.karmazin.shorturl.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.LinkRedirect;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.pojo.Statistics;
import ru.karmazin.shorturl.repository.StatisticsRepository;
import ru.karmazin.shorturl.repository.UrlRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final UrlRepository urlRepository;

    public StatisticsService(StatisticsRepository statisticsRepository,
                             UrlRepository urlRepository) {
        this.statisticsRepository = statisticsRepository;
        this.urlRepository = urlRepository;
    }

    public void recordLinkRedirect(Url url) {
        LinkRedirect linkRedirect = new LinkRedirect();
        linkRedirect.setTimestamp(LocalDateTime.now());
        linkRedirect.setUrl(url);
        statisticsRepository.save(linkRedirect);
    }

    public List<Statistics> getRedirectsByDay() {
        return statisticsRepository.findAllByGroupedByDay();
    }
    public List<Statistics> getRedirectsByHour() {
        return statisticsRepository.findAllByGroupedByHour();
    }
    public List<Statistics> getRedirectsByMinute() {
        return statisticsRepository.findAllByGroupedByMinute();
    }

    @Transactional
    public List<UrlDto> getTop20Urls() {
        return urlRepository.findTop20AndOrderByCountRequestsDesc().stream().map(u -> new UrlDto(
                u.getId(),
                u.getOriginalUrl(),
                u.getShortUrl(),
                u.getCreatedDate(),
                u.getCountRequests(),
                u.getUser().getId()
        )).collect(Collectors.toList());
    }
}
