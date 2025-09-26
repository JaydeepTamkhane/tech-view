package com.jaydeep.tech_view.security;

import com.jaydeep.tech_view.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        // In case of anonymous access or misconfigured authentication
        throw new IllegalStateException("No authenticated user found");
    }
}