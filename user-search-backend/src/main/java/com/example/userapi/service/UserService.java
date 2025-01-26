package com.example.userapi.service;

import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USERS_API_URL = "https://dummyjson.com/users";

    public void saveUsersFromJson() throws IOException {
        // Fetch JSON from the API
        String json = restTemplate.getForObject(USERS_API_URL, String.class);

        // Deserialize JSON to extract the "users" array
        List<User> users = objectMapper.readValue(
                objectMapper.readTree(json).get("users").toString(),
                new TypeReference<List<User>>() {}
        );

        // Save all users to the database
        userRepository.saveAll(users);
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrSsnContaining(query, query, query);
    }

    public User findUserByIdOrEmail(Long id, String email) {
        if (id != null) {
            return userRepository.findById(id).orElse(null);
        } else if (email != null) {
            return userRepository.findByEmail(email);
        }
        return null;
    }
}


