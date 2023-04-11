package ru.practicum.ewm.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class PrivateCommentController {

    private final CommentService commentService;

    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody NewCommentDto newCommentDto, @PathVariable Long userId) {
        log.info("Create new comment, creator id=" + userId);
        return commentService.createComment(newCommentDto, userId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public UpdateCommentDto updateComment(@RequestBody CommentDto commentDto, @PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Update comment with id=" + commentId);
        return commentService.updateComment(commentDto, userId, commentId);
    }


    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("Delete comment with id=" + commentId);
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/{userId}/comments/{eventId}")
    public List<CommentDto> getEventWithComment(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Get info about comments for event with id = " + eventId);
        return commentService.findAllByEventId(userId, eventId);
    }
}
