package com.by.user.controller;

import com.by.security.service.UserManagementService;
import com.by.user.model.Role;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
public class AdminController {

    private final UserManagementService userManagementService;

    @GetMapping(path = "/getRoles/{id}")
    public List<Role> getUserRoles(@PathVariable String id) throws FirebaseAuthException {
        return userManagementService.getUserRoles(id);
    }

    @PostMapping(path = "/setRoles/{id}")
    public Object setUserRoles(
            @PathVariable String id,
            @RequestBody List<Role> requestedRoles
    ) throws FirebaseAuthException {
        return userManagementService.setUserRoles(id, requestedRoles);
    }

    @PostMapping(path = "/addRoles/{id}")
    public Object addUserRoles(
            @PathVariable String id,
            @RequestBody List<Role> requestedRoles
    ) throws FirebaseAuthException {
        return userManagementService.addUserRoles(id, requestedRoles);
    }

    @PostMapping(path = "/deleteRoles/{id}")
    public Object deleteUserRoles(
            @PathVariable String id,
            @RequestBody List<Role> requestedRoles
    ) throws FirebaseAuthException {
        return userManagementService.deleteUserRoles(id, requestedRoles);
    }

    @PostMapping(path = "/blockUser/{id}")
    public Object blockUser(@PathVariable String id) throws FirebaseAuthException {
        return userManagementService.deleteUserRoles(id, List.of(Role.NOT_BLOCKED));
    }

    @PostMapping(path = "/unblockUser/{id}")
    public Object unblockUser(@PathVariable String id) throws FirebaseAuthException {
        return userManagementService.addUserRoles(id, List.of(Role.NOT_BLOCKED));
    }

    @PostMapping(path = "/makeAdmin/{id}")
    public Object makeAdmin(@PathVariable String id) throws FirebaseAuthException {
        return userManagementService.addUserRoles(id, List.of(Role.ADMIN));
    }

    @PostMapping(path = "/unmakeAdmin/{id}")
    public Object unmakeAdmin(@PathVariable String id) throws FirebaseAuthException {
        return userManagementService.deleteUserRoles(id, List.of(Role.ADMIN));
    }

}
