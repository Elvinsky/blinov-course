package com.by.user.service;

import com.by.user.dto.request.CreateFriendRequestRequest;
import com.by.user.model.FriendRequest;
import com.by.user.repository.FriendRequestRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public record FriendRequestService(FriendRequestRepository friendRequestRepository,
                                   UserService userService) {
    public FriendRequest createFriendRequest(CreateFriendRequestRequest createFriendRequestRequest, String senderId) {
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(userService.getUser(senderId))
                .senderId(userService.getUser(senderId).getId())
                .receiver(userService.getUserByEmail(createFriendRequestRequest.receiverEmail()))
                .receiverId(userService.getUserByEmail(createFriendRequestRequest.receiverEmail()).getId())
                .date(new Date())
                .build();
        return friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequest> getAllFriendRequests(String receiverId) {
        return friendRequestRepository.findAllByReceiverId(receiverId);
    }

    public FriendRequest getFriendRequest(Integer id) {
        return friendRequestRepository.findById(id).orElse(null);
    }

    public boolean acceptFriendRequest(Integer id, String currentUserID) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        if (friendRequest == null) {
            return false;
        } else  {
            userService.addFriend(friendRequest.getSender().getId(), currentUserID);
            friendRequestRepository.deleteById(id);
            return true;
        }
    }

    public boolean declineFriendRequest(Integer id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        if (friendRequest == null) {
            return false;
        } else  {
            friendRequestRepository.deleteById(id);
            return true;
        }
    }
}
