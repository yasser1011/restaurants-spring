package com.example.backend.controller;

import com.example.backend.models.User;
import com.example.backend.service.UserLoginRequest;
import com.example.backend.service.UserLoginResponse;
import com.example.backend.service.UserRegisterRequest;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    UserLoginResponse registerUser(@RequestBody UserRegisterRequest user) throws Exception {
        if(!user.getPassword().equals(user.getConfirmPassword()))
            throw new Exception("passwords do not match");

        return userService.saveUser(new User(user.getUsername(), user.getPassword()));
    }

    @GetMapping(path = "/getUser")
    HashMap<String, Object> getUserByToken(){
        User user = userService.getUserByToken();

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("user", user);
        return userMap;
    }

    @GetMapping(path = "/admin")
    String adminTest(){
        return "hey admin";
    }

    @GetMapping(path = "/home")
    String homeTest(){
        return "hey home";
    }

    @GetMapping(path = "/user")
    String userTest(){
        return "hey user";
    }

    //getUser route
}
