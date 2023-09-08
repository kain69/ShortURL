package ru.karmazin.shorturl.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karmazin.shorturl.dto.UrlCreateDto;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.model.User;
import ru.karmazin.shorturl.repository.UrlRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlShortening urlShortening;
    private final StatisticsService statisticsService;

    public UrlService(UrlRepository urlRepository,
                      UrlShortening urlShortening, StatisticsService statisticsService) {
        this.urlRepository = urlRepository;
        this.urlShortening = urlShortening;
        this.statisticsService = statisticsService;
    }

    public List<UrlDto> getUrlsForUser(User user) {
        return urlRepository.findByUser(user).stream().map(url -> new UrlDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests(),
                url.getUser().getId()
        )).collect(Collectors.toList());
    }

    public Optional<Url> getUrlByIdForUser(User user, Long urlId) {
        return urlRepository.findUrlByIdAndUser(urlId, user);
    }

    public Optional<Url> getUrlById(long id) {
        return urlRepository.findById(id);
    }

    public List<UrlDto> getAll() {
        List<Url> urls = urlRepository.findAll();
        return urls.stream().map(url -> new UrlDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests(),
                url.getUser().getId()
        )).collect(Collectors.toList());
    }

    @Transactional
    public UrlDto createShortUrl(UrlCreateDto urlCreateDto, User user) {
        Url url;
        Optional<Url> optionalUrl = urlRepository.findByOriginalUrlAndUser(urlCreateDto.getOriginalUrl(), user);
        url = optionalUrl.orElseGet(() -> createUrl(urlCreateDto, user));

        return new UrlDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests(),
                url.getUser().getId()
        );
    }

    @Transactional
    public String redirectToOriginalUrl(String shortUrl) {
        var id = urlShortening.shortURLtoID(shortUrl);
        var url = urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ShortUrl not found: " + shortUrl));
        url.setCountRequests(url.getCountRequests() + 1);
        statisticsService.recordLinkRedirect(url);
        return url.getOriginalUrl();
    }

    @Transactional
    public void delete(Long id) {
        urlRepository.delete(this.getUrlById(id).orElseThrow(EntityNotFoundException::new));
    }

    private Url createUrl(UrlCreateDto urlCreateDto, User user) {
        var url = new Url();
        url.setOriginalUrl(urlCreateDto.getOriginalUrl());
        url.setCreatedDate(new Date());
        url.setCountRequests(0);
        url.setUser(user);

        var entity = urlRepository.save(url);

        var shortUrl = urlShortening.idToShortURL(entity.getId());
        url.setShortUrl(shortUrl);
        return url;
    }
}
