package ru.karmazin.shorturl.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Statistics {
    private LocalDateTime date_time;

    private Long redirect_count;
}
