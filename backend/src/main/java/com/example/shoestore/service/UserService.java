package com.example.shoestore.service;

import com.example.shoestore.entity.Cart;
import com.example.shoestore.entity.User;
import com.example.shoestore.enums.Role;
import com.example.shoestore.repository.CartRepository;
import com.example.shoestore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public User findByFirebaseId(String uid) {
        return userRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with uid: " + uid));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new RuntimeException("User not found with firebaseUid: " + firebaseUid));
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(String id, User updated) {
        User existing = findByFirebaseId(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setAvatar(updated.getAvatar());
        return userRepository.save(existing);
    }

    @Transactional
    public void setActive(String id, Boolean isActive) {
        User user = findByFirebaseId(id);
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        Cart cart = Cart.builder()
                .user(savedUser)
                .build();

        cartRepository.save(cart);

        return savedUser;
    }
}
