package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EndpointHit {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
