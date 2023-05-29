package com.by.security.service;

import com.by.user.model.Role;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final FirebaseAuth firebaseAuth;

    public List<Role> setUserRoles(String id, List<Role> requestedRoles) throws FirebaseAuthException {
        List<String> roles = requestedRoles
                .stream()
                .map(Enum::toString)
                .toList();

        Map<String, Object> claims = Map.of("roles", roles);

        firebaseAuth.setCustomUserClaims(id, claims);

        return getUserRoles(id);
    }

    public List<Role> addUserRoles(String id, List<Role> requestedRoles) throws FirebaseAuthException {
        requestedRoles.addAll(getUserRoles(id));
        List<String> roles = requestedRoles
                .stream()
                .map(Enum::toString)
                .toList();
        Set<String> uniqueRoles = new HashSet<>(roles);

        Map<String, Object> claims = Map.of("roles", uniqueRoles);

        firebaseAuth.setCustomUserClaims(id, claims);

        return getUserRoles(id);
    }


    public List<Role> deleteUserRoles(String id, List<Role> requestedRoles) throws FirebaseAuthException {
        List<String> roles = new ArrayList<>(getUserRoles(id)
                .stream()
                .map(Enum::toString)
                .toList());

        for (Role role : requestedRoles) {
                roles.remove(role.name());
        }

        Map<String, Object> claims = Map.of("roles", roles);

        firebaseAuth.setCustomUserClaims(id, claims);

        return getUserRoles(id);

    }


    public List<Role> getUserRoles(String id) throws FirebaseAuthException {
        String rolesString = firebaseAuth.getUser(id).getCustomClaims().get("roles").toString();
        String[] rolesArray = rolesString.replace("[", "").
                replace("]", "")
                .replace(" ", "")
                .split(",");

        List<String> rolesStringList = List.of(rolesArray);

        if (rolesStringList.size() == 1) {
            try {
                return rolesStringList.stream().map(Role::valueOf).toList();
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }
        else {
            return rolesStringList.stream().map(Role::valueOf).toList();
        }
    }
}
