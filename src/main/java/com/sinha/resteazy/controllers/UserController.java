package com.sinha.resteazy.controllers;


import com.sinha.resteazy.entities.SuccessResponse;
import com.sinha.resteazy.entities.User;
import com.sinha.resteazy.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        userService.addNewUser(user);
        return new ResponseEntity<>(new SuccessResponse(user, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        User user = userService.deleteUserById(id);
        return new ResponseEntity<>(new SuccessResponse(user, HttpStatus.ACCEPTED.value()), HttpStatus.ACCEPTED);
    }

}
