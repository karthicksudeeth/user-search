package com.example.userapi.service;

import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    // Logger instance to log the service layer activities
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final WebClient webClient;

    private static final String USERS_API_URL = "https://dummyjson.com/us";

    // Constructor for injecting WebClient.Builder to create the WebClient instance
    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(USERS_API_URL).build();
    }

    // Method to import users from an external JSON API and save them to the database
    public Mono<String> saveUsersFromJson() {
        logger.info("Starting the process to import users from external API...");
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        // Parse the JSON response to a list of User objects
                        List<User> users = objectMapper.readValue(
                                objectMapper.readTree(json).get("users").toString(),
                                new TypeReference<List<User>>() {}
                        );
                        // Save the users to the database and return a success message
                        return userRepository.saveAll(Flux.fromIterable(users))
                                .then(Mono.just("Users imported successfully!"));
                    } catch (Exception e) {
                        // Log the error and return a failure message
                        logger.error("Error occurred while importing users: {}", e.getMessage());
                        return Mono.error(new RuntimeException("Failed to import users: " + e.getMessage()));
                    }
                });
    }

    // Method to fetch all users from the database
    public Flux<User> findAllUsers() {
        logger.info("Fetching all users from the database...");
        return userRepository.findAll()
                .doOnTerminate(() -> logger.info("Fetched all users from the database."));
    }

    // Method to search users by first name, last name, or SSN
    public Flux<User> searchUsers(String query) {
        logger.info("Searching for users with query: {}", query);
        return userRepository.findByFirstNameContainingOrLastNameContainingOrSsnContaining(query, query, query)
                .switchIfEmpty(Flux.error(new UserNotFoundException("No users found matching query: " + query)))
                .doOnTerminate(() -> logger.info("Search completed for query: {}", query));
    }

    // Method to find a user by their ID or email
    public Mono<User> findUserByIdOrEmail(Long id, String email) {
        if (id != null) {
            logger.info("Looking for user with ID: {}", id);
            return userRepository.findById(id)
                    .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with ID: " + id)))
                    .doOnTerminate(() -> logger.info("User with ID: {} found.", id));
        } else if (email != null) {
            logger.info("Looking for user with email: {}", email);
            return userRepository.findByEmail(email)
                    .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)))
                    .doOnTerminate(() -> logger.info("User with email: {} found.", email));
        }
        return Mono.error(new IllegalArgumentException("Either ID or email must be provided"));
    }

    // Method to update an existing user's information
    public Mono<User> updateUser(Long id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with ID: " + id)))
                .flatMap(existingUser -> {
                    // Set the existing ID to the updated user object
                    updatedUser.setId(existingUser.getId());
                    return userRepository.save(updatedUser);
                })
                .doOnTerminate(() -> logger.info("User with ID: {} updated successfully", id));
    }

    // Method to delete a user by their ID
    public Mono<String> deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with ID: " + id)))
                .flatMap(user -> userRepository.delete(user).then(Mono.just("User deleted successfully")))
                .doOnTerminate(() -> logger.info("User with ID: {} deleted successfully", id));
    }
}
