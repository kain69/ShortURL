package ru.karmazin.shorturl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO для создания URL")
public class UrlCreateDto {
    @Schema(description = "Оригинальная ссылка")
    private String originalUrl;
}
