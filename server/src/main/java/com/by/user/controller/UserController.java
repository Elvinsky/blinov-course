package com.by.user.controller;

import com.by.user.dto.request.LoginRequest;
import com.by.user.dto.request.SignUpRequest;
import com.by.user.dto.response.UserResponse;
import com.by.user.model.Role;
import com.by.user.model.User;
import com.by.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"*"})
public record UserController(UserService userService) {

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest, Principal principal) {
        return new ResponseEntity<>(userService.createUser(loginRequest, principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return new ResponseEntity<>(userService.createUser(signUpRequest), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/deleteMyProfile")
    public void deleteMyProfile(Principal principal) {
        userService.deleteUser(principal.getName());
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> isAdmin(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.ADMIN.name()))) {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/isBlocked")
    public ResponseEntity<Boolean> isBlocked(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.NOT_BLOCKED.name()))) {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        } else {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }

    @PostMapping("/addFriend/{email}")
    public UserResponse addFriend(Principal principal, @PathVariable("email") String email) {
        return userService.addFriendEmail(email, principal.getName());
    }

    @GetMapping("/friends")
    public List<UserResponse> getFriends(Principal principal) {
        return userService.getFriends(principal.getName());
    }
}
