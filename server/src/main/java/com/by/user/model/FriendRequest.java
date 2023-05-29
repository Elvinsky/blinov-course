package com.by.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User sender;
    @Column(name = "sender_id")
    private String senderId;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User receiver;
    @Column(name = "receiver_id")
    private String receiverId;

    private Date date;
}
