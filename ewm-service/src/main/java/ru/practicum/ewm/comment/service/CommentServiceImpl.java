package ru.practicum.ewm.comment.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.Comment;
import ru.practicum.ewm.comment.CommentMapper;
import ru.practicum.ewm.comment.CommentRepository;
import ru.practicum.ewm.comment.CommentState;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.enumerate.EventState;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CommentDto> getComments(int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);
        List<Comment> commentList = commentRepository.findAll(pageable).getContent();
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto changeCommentState(Long commentId, String state) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " not found")
        );
        if (state.equals("APPROVED")) {
            comment.setState(CommentState.APPROVED);
        } else if (state.equals("REJECT")) {
            comment.setState(CommentState.REJECTED);
        }
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " not found")
        );
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto createComment(NewCommentDto newCommentDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Event event = eventRepository.findById(newCommentDto.getEventId()).orElseThrow(
                () -> new NotFoundException("Event with id = " + newCommentDto.getEventId() + " not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Comment can be add only to published events");
        }
        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setState(CommentState.PENDING);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public UpdateCommentDto updateComment(CommentDto commentDto, Long userId, Long commentId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " not found")
        );
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only comment author can update comment");
        }
        if (!comment.getState().equals(CommentState.APPROVED)) {
            throw new ConflictException("Only published comment can be updated");
        }
        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }
        return CommentMapper.toUpdateCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + commentId + " not found")
        );
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only comment author can delete comment");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findAllByEventId(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " not found")
        );
        eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " not found"));
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
