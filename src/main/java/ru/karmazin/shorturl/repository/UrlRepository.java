package ru.karmazin.shorturl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.karmazin.shorturl.model.Url;
import ru.karmazin.shorturl.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByOriginalUrl(String originalUrl);
    Optional<Url> findByOriginalUrlAndUser(String originalUrl, User user);
    List<Url> findByUser(User user);
    Optional<Url> findUrlByIdAndUser(long id, User user);
    @Query("SELECT u FROM Url u ORDER BY u.countRequests DESC LIMIT 20")
    List<Url> findTop20AndOrderByCountRequestsDesc();
}
