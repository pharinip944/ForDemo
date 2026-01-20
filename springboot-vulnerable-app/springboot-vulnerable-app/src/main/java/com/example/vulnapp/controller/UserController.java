package com.example.vulnapp.controller;

import com.example.vulnapp.model.User;
import com.example.vulnapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id, Authentication authentication) {
        // Ensure the authenticated user can only access their own data or has admin role
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
            !authentication.getName().equals(userService.getById(id).getUsername())) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied");
        }
        return userService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }
}
