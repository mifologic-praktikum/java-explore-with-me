package ru.practicum.ewm.comment.dto;

import lombok.*;
import ru.practicum.ewm.comment.CommentState;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateCommentDto {
    private Long id;
    private String text;
    private Event event;
    private User author;
    private String created;
    private String updated;
    private CommentState state;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class User {
        private Long id;
        @NotBlank
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Event {
        private Long id;
        private String title;
    }
}
