package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

public interface UserService
{
    boolean createOrUpdate(User user);
    User findById(long id);
    User findByUsername(String username);
    boolean deleteUser(long id);
    boolean updateUserPwd(User user,String oldPwd,String newPwd);
    List<User> findAll();
}
