package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ViewStats {

    private String app;
    private String uri;
    private Long hits;
}
