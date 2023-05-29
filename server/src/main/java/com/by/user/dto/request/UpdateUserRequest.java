package com.by.user.dto.request;

import com.by.user.model.Role;

public record UpdateUserRequest(String name,
                                String email,
                                String password,
                                Role role) {
}
