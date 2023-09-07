package ru.karmazin.shorturl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.karmazin.shorturl.pojo.Statistics;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SqlResultSetMapping(
        name = "StatisticsMapping",
        classes = {
                @ConstructorResult(
                        targetClass = Statistics.class,
                        columns = {
                                @ColumnResult(name = "date_time", type = LocalDateTime.class),
                                @ColumnResult(name = "redirect_count", type = Long.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "LinkRedirect.findAllByGroupedByDay",
        query = "SELECT DATE_TRUNC('day', timestamp) as date_time, " +
                "COUNT(*) AS redirect_count " +
                "FROM link_redirect GROUP BY date_time",
        resultSetMapping = "StatisticsMapping"
)
@NamedNativeQuery(
        name = "LinkRedirect.findAllByGroupedByHour",
        query = "SELECT DATE_TRUNC('hour', timestamp) as date_time, " +
                "COUNT(*) AS redirect_count " +
                "FROM link_redirect GROUP BY date_time",
        resultSetMapping = "StatisticsMapping"
)
@NamedNativeQuery(
        name = "LinkRedirect.findAllByGroupedByMinute",
        query = "SELECT DATE_TRUNC('minute', timestamp) as date_time, " +
                "COUNT(*) AS redirect_count " +
                "FROM link_redirect GROUP BY date_time",
        resultSetMapping = "StatisticsMapping"
)
public class LinkRedirect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "url_id")
    private Url url;
}
