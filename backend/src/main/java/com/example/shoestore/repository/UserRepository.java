package com.example.shoestore.repository;

import com.example.shoestore.entity.User;
import com.example.shoestore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByFirebaseUid(String firebaseUid);

    boolean existsByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);

    List<User> findByRole(Role role);

    List<User> findByIsActive(Boolean isActive);
}
