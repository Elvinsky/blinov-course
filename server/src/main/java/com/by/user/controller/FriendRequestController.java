package com.by.user.controller;

import com.by.user.dto.request.CreateFriendRequestRequest;
import com.by.user.model.FriendRequest;
import com.by.user.service.FriendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/friendRequests")
@Slf4j
@CrossOrigin(origins = {"*"})
public record FriendRequestController(FriendRequestService friendRequestService) {

    @GetMapping
    public ResponseEntity<List<FriendRequest>> getAllFriendRequests(Principal principal) {
        return new ResponseEntity<>(friendRequestService.getAllFriendRequests(principal.getName()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public FriendRequest getFriendRequest(@PathVariable("id") Integer id) {
        return friendRequestService.getFriendRequest(id);
    }

    @PostMapping()
    public ResponseEntity<FriendRequest> createFriendRequest(@RequestBody CreateFriendRequestRequest
                                                                     createFriendRequestRequest,
                                                             Principal principal) {
        return ResponseEntity.ok(friendRequestService
                .createFriendRequest(createFriendRequestRequest, principal.getName()));
    }

    @PostMapping("accept/{id}")
    public ResponseEntity acceptFriendRequest(@PathVariable("id") Integer id, Principal principal) {
        return ResponseEntity.ok(friendRequestService.acceptFriendRequest(id, principal.getName()));
    }

    @PostMapping("decline/{id}")
    public ResponseEntity declineFriendRequest(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(friendRequestService.declineFriendRequest(id));
    }
}
