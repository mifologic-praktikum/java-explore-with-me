package ru.practicum.ewm.comment.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getComments(@RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get all comments");
        return commentService.getComments(from, size);
    }


    @PatchMapping("/{commentId}")
    public CommentDto changeCommentState(@PathVariable Long commentId, @RequestParam String state) {
        log.info("Change state for comment with id=" + commentId + ", new state=" + state);
        return commentService.changeCommentState(commentId, state);
    }

}
