package ru.practicum.ewm.event.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventShortDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    @NotBlank
    private Category category;
    @NotBlank
    private String eventDate;
    @NotNull
    private Boolean paid;
    @NotBlank
    private User initiator;
    private int confirmedRequests;
    private Long views;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Category {
        private Long id;
        @NotBlank
        private String name;
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
}
