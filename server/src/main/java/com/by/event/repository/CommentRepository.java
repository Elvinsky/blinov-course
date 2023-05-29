package com.by.event.repository;

import com.by.event.model.Comment;
import com.by.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByOwnerId(String ownerId);
}
