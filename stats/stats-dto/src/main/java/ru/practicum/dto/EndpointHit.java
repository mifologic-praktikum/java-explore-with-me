package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class EndpointHit {

    Long id;
    String app;
    String uri;
    String ip;
    String timestamp;
}
