package edu.jhu.eventservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.jhu.eventservice.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUserId(Integer userId);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
