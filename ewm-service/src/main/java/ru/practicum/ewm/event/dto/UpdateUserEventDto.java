package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.enumerate.EventStateAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserEventDto {

    String annotation;
    Long category;
    String description;
    String eventDate;
    AdminUpdateEventDto.Location location;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    EventStateAction stateAction;
    String title;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Location {
        float lat;
        float lon;
    }
}
