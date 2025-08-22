package com.logistics_backend.service;

import com.logistics_backend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

   public User createUser(User user);
   public User getUserById(Long id);
   List<User> getAllUsers();
   User updateUser(User user,Long id);
   void deleteUser(Long id);
}
