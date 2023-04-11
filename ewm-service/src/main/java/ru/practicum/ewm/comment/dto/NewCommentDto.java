package ru.practicum.ewm.comment.dto;

import lombok.*;
import ru.practicum.ewm.comment.CommentState;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCommentDto {
    private String text;
    private Long eventId;
    private Long authorId;
    private CommentState state;
}