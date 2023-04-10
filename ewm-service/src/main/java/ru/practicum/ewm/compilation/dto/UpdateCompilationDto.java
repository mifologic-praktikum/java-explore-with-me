package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateCompilationDto {

    String title;
    Boolean pinned;
    List<Long> events;
}
