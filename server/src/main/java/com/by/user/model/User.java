package com.by.user.model;

import com.by.event.model.Comment;
import com.by.event.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
    @Id
    private String id;
    private String username;
    private String email;
    @ManyToMany
    @JoinTable(name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends;
    @OneToMany
    private List<FriendRequest> friendRequests;
//    @OneToMany
//    @JoinColumn(name = "comment_id")
//    private List<Comment> comments;
    @OneToMany
    @JoinColumn(name = "event_id")
    private List<Event> events;

    public void addFriend(User user) {
        friends.add(user);
    }

}
