package com.task.TeamManager.Repository;

import com.task.TeamManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email (username).
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the given email exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with the given name exists.
     */
    boolean existsByName(String name);
}
