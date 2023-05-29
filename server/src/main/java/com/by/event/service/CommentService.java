package com.by.event.service;

import com.by.event.model.Comment;
import com.by.event.repository.CommentRepository;
import com.by.event.repository.EventRepository;
import com.by.event.request.CreateCommentRequest;
import com.by.event.request.UpdateCommentRequest;
import com.by.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public record CommentService(CommentRepository commentRepository,
                             UserRepository userRepository,
                             EventRepository eventRepository) {


    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public Comment createComment(CreateCommentRequest createCommentRequest, String ownerId) {
        Comment comment = Comment.builder()
                .commentText(createCommentRequest.commentText())
                .date(formatter.parse(createCommentRequest.date()))
                .owner(userRepository.findById(ownerId).orElseThrow())
                .ownerId(userRepository.findById(ownerId).orElseThrow().getId())
                .event(eventRepository.findById(createCommentRequest.eventId()).orElseThrow())
                .eventId(createCommentRequest.eventId())
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments(String ownerId) {
        return commentRepository.findAllByOwnerId(ownerId);
    }

    public Comment getComment(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @SneakyThrows
    public Comment updateComment(Integer id, UpdateCommentRequest updateCommentRequest, String ownerId) {
        Comment updatedComment = Comment.builder()
                .id(id)
                .commentText(updateCommentRequest.commentText())
                .date(formatter.parse(updateCommentRequest.date()))
                .owner(userRepository.findById(ownerId).orElseThrow())
                .event(eventRepository.findById(updateCommentRequest.eventId()).orElseThrow())
                .build();
        commentRepository.save(updatedComment);
        return updatedComment;
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
