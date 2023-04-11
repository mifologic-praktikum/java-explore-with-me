package ru.practicum.ewm.comment.service;


import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getComments(int from, int size);

    CommentDto changeCommentState(Long commentId, String state);

    CommentDto getCommentById(Long commentId);

    CommentDto createComment(NewCommentDto newCommentDto, Long userId);

    UpdateCommentDto updateComment(CommentDto commentDto, Long userId, Long commentId);

    void deleteComment(Long userId, Long commentId);

    List<CommentDto> findAllByEventId(Long userId, Long eventId);
}
