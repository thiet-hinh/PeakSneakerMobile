package com.example.shoestore.controller;

import com.google.firebase.FirebaseApp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return FirebaseApp.getInstance().getName();
    }
}
