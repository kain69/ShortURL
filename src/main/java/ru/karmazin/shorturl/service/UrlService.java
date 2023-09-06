package ru.karmazin.shorturl.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karmazin.shorturl.dto.UrlCreateDto;
import ru.karmazin.shorturl.dto.UrlDto;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.repository.UrlRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlShortening urlShortening;

    public UrlService(UrlRepository urlRepository, UrlShortening urlShortening) {
        this.urlRepository = urlRepository;
        this.urlShortening = urlShortening;
    }

    public Url getUrl(long id){
        return urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не существует короткой ссылки с id: " + id));
    }

    public List<Url> getAll() {
        return urlRepository.findAll();
    }

    @Transactional
    public UrlDto createShortUrl(UrlCreateDto urlCreateDto) {
        Url url;
        Optional<Url> optionalUrl = urlRepository.findByOriginalUrl(urlCreateDto.getOriginalUrl());
        url = optionalUrl.orElseGet(() -> createUrl(urlCreateDto));

        return new UrlDto(
                url.getOriginalUrl(),
                url.getShortUrl(),
                url.getCreatedDate(),
                url.getCountRequests()
        );
    }

    @Transactional
    public String redirectToOriginalUrl(String shortUrl) {
        var id = urlShortening.shortURLtoID(shortUrl);
        var entity = urlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Не существует короткой ссылки: " + shortUrl));
        entity.setCountRequests(entity.getCountRequests() + 1);
        return entity.getOriginalUrl();
    }

    private Url createUrl(UrlCreateDto urlCreateDto) {
        var url = new Url();
        url.setOriginalUrl(urlCreateDto.getOriginalUrl());
        url.setCreatedDate(new Date());
        url.setCountRequests(0);

        var entity = urlRepository.save(url);

        var shortUrl = urlShortening.idToShortURL(entity.getId());
        url.setShortUrl(shortUrl);
        return url;
    }

    @Transactional
    public void delete(int id) {
        urlRepository.delete(this.getUrl(id));
    }
}
