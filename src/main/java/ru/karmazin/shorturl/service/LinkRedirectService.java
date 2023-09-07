package ru.karmazin.shorturl.service;

import org.springframework.stereotype.Service;
import ru.karmazin.shorturl.model.LinkRedirect;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.pojo.Statistics;
import ru.karmazin.shorturl.repository.LinkRedirectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LinkRedirectService {

    private final LinkRedirectRepository linkRedirectRepository;

    public LinkRedirectService(LinkRedirectRepository linkRedirectRepository) {
        this.linkRedirectRepository = linkRedirectRepository;
    }

    public void recordLinkRedirect(Url url) {
        LinkRedirect linkRedirect = new LinkRedirect();
        linkRedirect.setTimestamp(LocalDateTime.now());
        linkRedirect.setUrl(url);
        linkRedirectRepository.save(linkRedirect);
    }

    public List<Statistics> getRedirectsByDay() {
        return linkRedirectRepository.findAllByGroupedByDay();
    }
    public List<Statistics> getRedirectsByHour() {
        return linkRedirectRepository.findAllByGroupedByHour();
    }
    public List<Statistics> getRedirectsByMinute() {
        return linkRedirectRepository.findAllByGroupedByMinute();
    }
}
