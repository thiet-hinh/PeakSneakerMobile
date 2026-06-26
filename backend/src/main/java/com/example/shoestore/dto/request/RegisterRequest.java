package com.example.shoestore.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String firebaseUid;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;
}
