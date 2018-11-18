package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Resource
    private UserRepository userRepository;

    @Override
    public boolean createOrUpdate(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findById(long id) {
        System.out.println(userRepository.getOne(id));
        return userRepository.getOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean deleteUser(long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean updateUserPwd(User user, String oldPwd, String newPwd) {
        if (!new BCryptPasswordEncoder().matches(oldPwd, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPwd));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
