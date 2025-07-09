package com.task.TeamManager.service;

import com.task.TeamManager.Repository.RoleRepository;
import com.task.TeamManager.Repository.UserRepository;
import com.task.TeamManager.model.ERole;
import com.task.TeamManager.model.Role;
import com.task.TeamManager.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user
    @Transactional
    public User registerUser(User user, Set<ERole> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roleEntities = new HashSet<>();
        for (ERole role : roles) {
            Role r = roleRepository.findByName(role)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            roleEntities.add(r);
        }

        // Correct usage of instance method
        user.setRoles(roleEntities);
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Check if a user exists by email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Check if a user exists by username (if you have a username field)
    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    // Get all users
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerNewUser(String username, String email, String rawPassword, Set<String> strRoles) {
        // Initialize a new user object
        User user = new User();
        user.setName(username);
        user.setEmail(email);

        // Hash and set the password
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Initialize an empty set to store role objects
        Set<Role> roles = new HashSet<>();

        // If strRoles is null or empty
        if (strRoles == null || strRoles.isEmpty()) {
            Role teamMemberRole = roleRepository.findByName(ERole.ROLE_TEAM_MEMBER)
                    .orElseThrow(() -> new RuntimeException("Error: Role 'TEAM_MEMBER' not found"));
            roles.add(teamMemberRole);
        } else {
            for (String roleStr : strRoles) {
                if (roleStr.equalsIgnoreCase("manager")) {
                    Role managerRole = roleRepository.findByName(ERole.ROLE_PROJECT_MANAGER)
                            .orElseThrow(() -> new RuntimeException("Error: Role 'PROJECT_MANAGER' not found"));
                    roles.add(managerRole);
                } else if (roleStr.equalsIgnoreCase("member")) {
                    Role memberRole = roleRepository.findByName(ERole.ROLE_TEAM_MEMBER)
                            .orElseThrow(() -> new RuntimeException("Error: Role 'TEAM_MEMBER' not found"));
                    roles.add(memberRole);
                } else {
                    throw new RuntimeException("Error: Role '" + roleStr + "' is not supported.");
                }
            }
        }

        // Set the roles to the user
        user.setRoles(roles);

        // Save and return the user
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // Check user existence
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with Id: " + id);
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUserRoles(Long userId, Set<String> strRoles) {
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with Id: " + userId));

        // Initialize role set
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Default role: TEAM_MEMBER
            Role teamMemberRole = roleRepository.findByName(ERole.ROLE_TEAM_MEMBER)
                    .orElseThrow(() -> new RuntimeException("Error: Role 'TEAM_MEMBER' not found"));
            roles.add(teamMemberRole);
        } else {
            for (String roleStr : strRoles) {
                switch (roleStr.toLowerCase()) {
                    case "manager":
                        Role managerRole = roleRepository.findByName(ERole.ROLE_PROJECT_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'PROJECT_MANAGER' not found"));
                        roles.add(managerRole);
                        break;
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'ADMIN' not found"));
                        roles.add(adminRole);
                        break;
                    case "member":
                        Role memberRole = roleRepository.findByName(ERole.ROLE_TEAM_MEMBER)
                                .orElseThrow(() -> new RuntimeException("Error: Role 'TEAM_MEMBER' not found"));
                        roles.add(memberRole);
                        break;
                    default:
                        throw new RuntimeException("Error: Role '" + roleStr + "' is not supported.");
                }
            }
        }

        // Assign new roles to user
        user.setRoles(roles);

        // Save and return updated user
        return userRepository.save(user);
    }



}
