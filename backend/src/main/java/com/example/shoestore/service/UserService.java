package com.example.shoestore.service;

import com.example.shoestore.dto.request.RegisterRequest;
import com.example.shoestore.dto.response.UserResponse;
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

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserResponse findByFirebaseId(String uid) {
        User user = findEntityByFirebaseId(uid);
        System.out.println("Tìm thấy User trong DB: " + user.getEmail() + " với UID: " + user.getFirebaseUid());
        return UserResponse.builder()
                .id(user.getId())
                .firebaseUid(user.getFirebaseUid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .isActive(user.getIsActive() != null ? user.getIsActive() : true)
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "")
                .build();
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
    public UserResponse update(String id, UserResponse updatedDto) {
        User existing = findEntityByFirebaseId(id);

        existing.setFirstName(updatedDto.getFirstName());
        existing.setLastName(updatedDto.getLastName());
        existing.setPhone(updatedDto.getPhone());

        User savedUser = userRepository.save(existing);
        return UserResponse.builder()
                .id(savedUser.getId())
                .firebaseUid(savedUser.getFirebaseUid())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .role(savedUser.getRole() != null ? savedUser.getRole().name() : "USER")
                .isActive(savedUser.getIsActive())
                .createdAt(savedUser.getCreatedAt() != null ? savedUser.getCreatedAt().toString() : "")
                .build();
    }

    @Transactional
    public void setActive(String id, Boolean isActive) {
        User user = findEntityByFirebaseId(id);
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    @Transactional
    public User register(RegisterRequest request) {
        User user = new User();

        user.setFirebaseUid(request.getFirebaseUid());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        Cart cart = Cart.builder()
                .user(savedUser)
                .build();

        cartRepository.save(cart);

        return savedUser;
    }

    private User findEntityByFirebaseId(String uid) {
        return userRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with uid: " + uid));
    }
}
