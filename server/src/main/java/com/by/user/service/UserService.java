package com.by.user.service;

import com.by.event.model.Event;
import com.by.security.service.UserManagementService;
import com.by.user.dto.request.LoginRequest;
import com.by.user.dto.request.SignUpRequest;
import com.by.user.dto.response.UserResponse;
import com.by.user.model.FriendRequest;
import com.by.user.model.Role;
import com.by.user.model.User;
import com.by.user.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public record UserService(UserRepository userRepository, FirebaseAuth firebaseAuth,
                          UserManagementService userManagementService) {

    public UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .friends(user.getFriends() != null ? user.getFriends().stream().map(User::getId).toList() : null)
                .friendRequests(user.getFriendRequests() != null ? user.getFriendRequests().stream().map(FriendRequest::getId).toList() : null)
                .events(user.getEvents() != null ? user.getEvents().stream().map(Event::getId).toList() : null)
                .build();
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::userToUserResponse).toList();
    }

    public UserResponse addFriend(String friendId, String currentUserId) {
        User updatedUser = userRepository.findById(currentUserId).orElseThrow();
        User newFriend = userRepository.findById(friendId).orElseThrow();
        updatedUser.addFriend(newFriend);
        userRepository.save(updatedUser);

        return userToUserResponse(updatedUser);
    }

    public UserResponse addFriendEmail(String friendEmail, String currentUserId) {
        User updatedUser = userRepository.findById(currentUserId).orElseThrow();
        User newFriend = userRepository.findFirstByEmail(friendEmail).orElseThrow();
        updatedUser.addFriend(newFriend);
        userRepository.save(updatedUser);

        return userToUserResponse(updatedUser);
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<UserResponse> getFriends(String id) {
        return userRepository.findById(id).orElseThrow().getFriends().stream().map(this::userToUserResponse).toList();
    }

    public User getUserByEmail(String email) {
        return userRepository.findFirstByEmail(email).orElseThrow();
    }

    @SneakyThrows
    public void deleteUser(String id) {
        firebaseAuth.deleteUser(id);
        userRepository.deleteById(id);
    }

    @SneakyThrows
    public UserResponse createUser(SignUpRequest signUpRequest) {
        List<String> roles = Arrays.asList(Role.USER.name(), Role.NOT_BLOCKED.name());
        Map<String, Object> claims = Map.of("roles", roles);
        firebaseAuth.setCustomUserClaims(signUpRequest.id(), claims);
        User user = User.builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .id(signUpRequest.id()).build();
        return userToUserResponse(userRepository.save(user));
    }

    @SneakyThrows
    public UserResponse createUser(LoginRequest loginRequest, String id) {
        if (userRepository.findById(id).isPresent()) {
            return userToUserResponse(userRepository.findById(id).orElseThrow());
        }
        List<String> roles = userManagementService.getUserRoles(id).stream().map(Role::name).toList();
        Map<String, Object> claims = Map.of("roles", roles);
        firebaseAuth.setCustomUserClaims(id, claims);
        User user = User.builder()
                .username(loginRequest.username())
                .email(loginRequest.email())
                .id(id).build();
        return userToUserResponse(userRepository.save(user));
    }
}
