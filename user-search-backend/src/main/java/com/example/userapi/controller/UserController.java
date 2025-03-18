package com.example.userapi.controller;

import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Logger instance to log the controller activities
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Endpoint to import users from an external source
    @PostMapping("/import")
    public Mono<String> importUsers() {
        logger.info("Starting user import process...");
        return userService.saveUsersFromJson()
                .doOnTerminate(() -> logger.info("User import process completed."));
    }

    // Endpoint to fetch all users
    @GetMapping
    public Flux<User> getAllUsers() {
        logger.info("Fetching all users...");
        return userService.findAllUsers()
                .doOnTerminate(() -> logger.info("Fetched all users."));
    }

    // Endpoint to search for users based on a query (first name, last name, or SSN)
    @GetMapping("/search")
    public Flux<User> searchUsers(@RequestParam String query) {
        logger.info("Searching users with query: {}", query);
        return userService.searchUsers(query)
                .doOnTerminate(() -> logger.info("Search completed for query: {}", query));
    }

    // Endpoint to find a user either by ID or by email
    @GetMapping("/find")
    public Mono<User> findUser(@RequestParam(required = false) Long id, @RequestParam(required = false) String email) {
        // Log which method is being used for the search (ID or email)
        if (id != null) {
            logger.info("Finding user with ID: {}", id);
        } else if (email != null) {
            logger.info("Finding user with email: {}", email);
        }
        return userService.findUserByIdOrEmail(id, email)
                .doOnTerminate(() -> {
                    // Log the result of the search (ID or email)
                    if (id != null) {
                        logger.info("User with ID: {} found", id);
                    } else if (email != null) {
                        logger.info("User with email: {} found", email);
                    }
                });
    }

    // Endpoint to update an existing user's information
    @PutMapping("/{id}")
    public ResponseEntity<Mono<User>> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        return new ResponseEntity<>(userService.updateUser(id, updatedUser)
                .doOnTerminate(() -> logger.info("User with ID: {} updated", id)), HttpStatus.OK);
    }

    // Endpoint to delete a user by their ID
    @DeleteMapping("/{id}")
    public Mono<String> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        return userService.deleteUser(id)
                .doOnTerminate(() -> logger.info("User with ID: {} deleted", id));
    }
}
