package com.by.event.controller;

import com.by.event.model.Comment;
import com.by.event.request.CreateCommentRequest;
import com.by.event.request.UpdateCommentRequest;
import com.by.event.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/comments")
@Slf4j
@CrossOrigin(origins = {"*"})
public record CommentController(CommentService commentService) {

    @GetMapping()
    public List<Comment> getAllComments(Principal principal) {
        return commentService.getAllComments(principal.getName());
    }

    @GetMapping(path = "/{id}")
    public Comment getComment(@PathVariable("id") Integer id) {
        return commentService.getComment(id);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CreateCommentRequest createCommentRequest, Principal principal) {
        log.info("New comment is created: {}", createCommentRequest);
        return new ResponseEntity<>(commentService.createComment(createCommentRequest, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public Comment updateComment(@PathVariable("id") Integer id, @RequestBody UpdateCommentRequest updateCommentRequest,
                                 Principal principal) {
        return commentService.updateComment(id, updateCommentRequest, principal.getName());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteComment(@PathVariable("id") Integer id) {
        commentService.deleteComment(id);
    }
}
