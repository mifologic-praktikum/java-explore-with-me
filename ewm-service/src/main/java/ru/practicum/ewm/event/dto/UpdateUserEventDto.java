package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.enumerate.EventStateAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserEventDto {

    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private AdminUpdateEventDto.Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    private String title;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Location {
        private float lat;
        private float lon;
    }
}
