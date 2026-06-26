package com.example.shoestore.controller;

import com.example.shoestore.dto.request.RegisterRequest;
import com.example.shoestore.dto.response.UserResponse;
import com.example.shoestore.entity.User;
import com.example.shoestore.enums.Role;
import com.example.shoestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsersController {

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

}
