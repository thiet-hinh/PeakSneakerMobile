package com.example.shoestore.controller;

import com.example.shoestore.dto.request.RegisterRequest;
import com.example.shoestore.dto.request.UserProfileRequest;
import com.example.shoestore.dto.response.UserProfileResponse;
import com.example.shoestore.dto.response.UserResponse;
import com.example.shoestore.entity.User;
import com.example.shoestore.enums.Role;
import com.example.shoestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/{uid}")
    public ResponseEntity<UserResponse> getByUid(@PathVariable("uid") String uid) {
        return ResponseEntity.ok(userService.findByFirebaseId(uid));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(userService.register(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(String uid, @RequestBody UserResponse user) {
        return ResponseEntity.ok(userService.update(uid, user));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable Integer userId, @RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Integer userId) {
        byte[] avatar = userService.getAvatar(userId);
        if (avatar == null || avatar.length == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(avatar.length).body(avatar);
    }

    @PutMapping(
            value = "/{userId}/avatar",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Void> updateAvatar(
            @PathVariable Integer userId,
            @RequestBody byte[] avatar) {
        userService.updateAvatar(userId, avatar);
        return ResponseEntity.ok().build();
    }

}
