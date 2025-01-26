package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstNameContainingOrLastNameContainingOrSsnContaining(String firstName, String lastName, String ssn);
    User findByEmail(String email);
}



