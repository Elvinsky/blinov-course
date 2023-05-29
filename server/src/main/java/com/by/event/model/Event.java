package com.by.event.model;

import com.by.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;
    @Column(name = "owner_id")
    private String ownerId;

    private Integer participants;
    @ManyToMany
    @JoinTable(name = "event_subscribers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<User> subscribers;
    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;
    private boolean isFavourite;
}
