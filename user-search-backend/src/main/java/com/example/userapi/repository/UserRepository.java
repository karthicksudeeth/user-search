package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Flux<User> findByFirstNameContainingOrLastNameContainingOrSsnContaining(String firstName, String lastName, String ssn);
    Mono<User> findByEmail(String email);
}
