package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.enumerate.EventState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class EventFullDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotNull
    private Category category;
    @NotNull
    private Boolean paid;
    @NotBlank
    private String eventDate;
    @NotNull
    private User initiator;
    private String description;
    private int participantLimit;
    private EventState state;
    private String createdOn;
    @NotNull
    private Location location;
    private Boolean requestModeration;
    private int confirmedRequests;
    private String publishedOn;
    private int views;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Category {
        Long id;
        @NotBlank
        String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        @NotNull
        private Long id;
        @NotBlank
        private String name;
    }

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
