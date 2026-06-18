package com.example.shoestore.repository;

import com.example.shoestore.entity.Users;
import com.example.shoestore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByFirebaseUid(String firebaseUid);

    boolean existsByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);

    List<Users> findByRole(Role role);

    List<Users> findByIsActive(Boolean isActive);
}
