package com.logistics_backend.controller;

import com.logistics_backend.entity.User;
import com.logistics_backend.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private ServiceImpl service;

    @GetMapping(value = "/api/users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser = service.getAllUsers();
        try {
            if (allUser.size() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.of(Optional.of(allUser));
    }

    @GetMapping(value = "/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        User u = service.getUserById(id);
        if (u==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(u));
    }

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody User users){
        User createUser = null;
        try {
            createUser = service.createUser(users);
            return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable("id") long id){
        try{
            service.updateUser(user,id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) {
        try {
            service.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
