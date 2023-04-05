package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class NewCompilationDto {

    @NotBlank
    String title;
    Boolean pinned;
    List<Long> events;
}
