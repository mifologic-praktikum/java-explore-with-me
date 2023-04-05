package ru.practicum.ewm.compilation.dto;

import lombok.*;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class CompilationDto {
    private Long id;
    @NotBlank
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;

}
