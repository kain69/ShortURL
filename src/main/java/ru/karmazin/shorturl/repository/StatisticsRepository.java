package ru.karmazin.shorturl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.karmazin.shorturl.model.LinkRedirect;
import ru.karmazin.shorturl.pojo.Statistics;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<LinkRedirect, Long> {
//    @Query("SELECT DATE_TRUNC('day', lr.timestamp) AS date_time, COUNT(*) AS count FROM LinkRedirect lr GROUP BY date_time")
    @Query(nativeQuery = true)
    List<Statistics> findAllByGroupedByDay();

    @Query(nativeQuery = true)
    List<Statistics> findAllByGroupedByHour();

    @Query(nativeQuery = true)
    List<Statistics> findAllByGroupedByMinute();
}
