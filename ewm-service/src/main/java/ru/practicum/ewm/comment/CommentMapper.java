package ru.practicum.ewm.comment;

import ru.practicum.ewm.DataFormatter;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                new CommentDto.Event(comment.getEvent().getId(), comment.getEvent().getTitle()),
                new CommentDto.User(comment.getAuthor().getId(), comment.getAuthor().getName()),
                DataFormatter.fromDateToString(comment.getCreated()),
                comment.getState()
        );
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .created(LocalDateTime.now()).build();
    }

    public static UpdateCommentDto toUpdateCommentDto(Comment comment) {
        return UpdateCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(new UpdateCommentDto.Event(comment.getEvent().getId(), comment.getEvent().getTitle()))
                .author(new UpdateCommentDto.User(comment.getAuthor().getId(), comment.getAuthor().getName()))
                .updated(DataFormatter.fromDateToString(LocalDateTime.now()))
                .state(comment.getState())
                .build();
    }
}
