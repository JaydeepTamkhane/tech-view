package com.jaydeep.tech_view.security;

import com.jaydeep.tech_view.entity.User;
import com.jaydeep.tech_view.entity.enums.Role;
import com.jaydeep.tech_view.exception.UnAuthorisedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnershipUtil {
    private final CurrentUserUtil currentUserUtil;

    public void ensureCurrentUserOwnership(User resourceOwner) {
        User currentUser = currentUserUtil.getCurrentUser();
        if (resourceOwner == null) {
            throw new IllegalArgumentException("Resource owner cannot be null");
        }
        if (!resourceOwner.equals(currentUser)) {
            throw new UnAuthorisedException(
                    "User " + currentUser.getId() + " does not own this resource"
            );
        }
    }

    public void ensureCurrentUserIsAdmin() {
        User currentUser = currentUserUtil.getCurrentUser();
        if (currentUser.getRoles() == null || !currentUser.getRoles().contains(Role.ADMIN)) {
            throw new UnAuthorisedException(
                    "User " + currentUser.getId() + " is not an admin"
            );
        }
    }

    public void ensureOwnerOrAdmin(User resourceOwner) {
        User currentUser = currentUserUtil.getCurrentUser();
        if (resourceOwner == null) {
            throw new IllegalArgumentException("Resource owner cannot be null");
        }
        boolean isOwner = resourceOwner.equals(currentUser);
        boolean isAdmin = currentUser.getRoles() != null && currentUser.getRoles().contains(Role.ADMIN);

        if (!(isOwner || isAdmin)) {
            throw new UnAuthorisedException(
                    "User " + currentUser.getId() + " is neither the owner nor an admin"
            );
        }
    }
}

