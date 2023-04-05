package ru.practicum.ewm.event.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotBlank
    private String description;
    @NotNull
    private Long category;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class LocationDto {
        private float lat;
        private float lon;
    }
}
