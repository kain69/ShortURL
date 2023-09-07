package ru.karmazin.shorturl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Информация о Url")
public class UrlDto {
    @Schema(description = "Оригинальная ссылка")
    private String originalUrl;
    @Schema(description = "Сокращенная ссылка")
    private String shortUrl;
    @Schema(description = "Дата создания")
    private Date createdDate;
    @Schema(description = "Количество переходов по ссылке")
    private int countRequests;
    @Schema(description = "Пользователь создавший ссылку")
    private Long userId;
}