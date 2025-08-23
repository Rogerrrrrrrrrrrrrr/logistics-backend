package com.logistics_backend.service;

import com.logistics_backend.entity.User;
import com.logistics_backend.exception.UserNotFoundException;
import com.logistics_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return this.userRepo.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepo.findById(id)
                .orElseThrow(()->new UserNotFoundException("User with " + id + " not Found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User updateUser(User user, Long id) {
        User existingUser = getUserById(id);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException("User with " + id + " not Found");
        }
        userRepo.deleteById(id);
    }
}
