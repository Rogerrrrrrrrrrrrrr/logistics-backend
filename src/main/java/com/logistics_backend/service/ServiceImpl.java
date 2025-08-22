package com.logistics_backend.service;

import com.logistics_backend.entity.User;
import com.logistics_backend.exception.UserNotFoundException;
import com.logistics_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
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
        User user1 = getUserById(id);
        user1.setName(user1.getName());
        user1.setEmail(user1.getEmail());
        user1.setPhoneNumber(user.getPhoneNumber());

        return userRepo.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User with " + id + " not Found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
