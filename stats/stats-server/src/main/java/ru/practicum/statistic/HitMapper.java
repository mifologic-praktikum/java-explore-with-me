package ru.practicum.statistic;

import ru.practicum.dto.EndpointHit;
import ru.practicum.statistic.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    public static Hit toHit(EndpointHit hitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(hitDto.getTimestamp(), formatter);
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                timestamp
        );

    }
}
