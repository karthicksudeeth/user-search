package com.example.userapi.controller;

import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/import")
    public String importUsers() {
        try {
            userService.saveUsersFromJson();
            return "Users imported successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import users: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query);
    }

    @GetMapping("/find")
    public User findUser(@RequestParam(required = false) Long id, @RequestParam(required = false) String email) {
        return userService.findUserByIdOrEmail(id, email);
    }
}
