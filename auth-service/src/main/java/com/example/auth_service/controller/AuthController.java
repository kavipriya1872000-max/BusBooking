package com.example.auth_service.controller;

import com.example.auth_service.config.JwtUtil;
import com.example.auth_service.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){

        // later connect DB
        if(request.getUsername().equals("admin") &&
                request.getPassword().equals("admin123")){

            return jwtUtil.generateToken(request.getUsername(),request.getRole());
        }

        throw new RuntimeException("Invalid Login");
    }
}

