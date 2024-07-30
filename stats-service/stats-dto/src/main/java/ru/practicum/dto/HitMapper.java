package ru.practicum.dto;

import ru.practicum.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), FORMATTER))
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .ip(hit.getIp())
                .uri(hit.getUri())
                .timestamp(FORMATTER.format(hit.getTimestamp()))
                .build();
    }
}
