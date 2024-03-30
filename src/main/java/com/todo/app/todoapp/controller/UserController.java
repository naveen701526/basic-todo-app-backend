package com.todo.app.todoapp.controller;

import com.todo.app.todoapp.dto.UserLoginRequest;
import com.todo.app.todoapp.dto.UserRegisterRequest;
import com.todo.app.todoapp.dto.UserRegisterResponse;
import com.todo.app.todoapp.entities.User;
import com.todo.app.todoapp.repository.UserRepository;
import com.todo.app.todoapp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;


    @PostMapping("/user")
    public ResponseEntity<UserRegisterResponse> addNewUser(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        User oldUser =  userRepository.findByUsername(userRegisterRequest.getUsername());
        if (oldUser != null){
            throw new Exception("user with given username already exist.... ");
        }
        User savedUser = userRepository.save(new User(userRegisterRequest.getUsername(), userRegisterRequest.getEmail(), passwordEncoder.encode(userRegisterRequest.getPassword()), userRegisterRequest.getAge()));
        return new ResponseEntity<>(new UserRegisterResponse(savedUser.getId(), savedUser.getUsername()), HttpStatus.CREATED);
    }



    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(userLoginRequest.getUsername()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }



}
