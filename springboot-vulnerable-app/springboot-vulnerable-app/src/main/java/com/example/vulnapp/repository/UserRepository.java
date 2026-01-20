package com.example.vulnapp.repository;

import com.example.vulnapp.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private Map<Long, User> store = new ConcurrentHashMap<>();

    public User findById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("User must be authenticated");
        }
        // Only allow access if user is admin or accessing their own record
        User user = store.get(id);
        if (user == null) {
            return null;
        }
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !authentication.getName().equals(user.getUsername())) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
        return user;
    }

    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }
}
