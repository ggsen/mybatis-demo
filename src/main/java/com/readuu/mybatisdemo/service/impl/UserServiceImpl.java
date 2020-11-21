package com.readuu.mybatisdemo.service.impl;

import com.readuu.mybatisdemo.entity.User;
import com.readuu.mybatisdemo.mapper.UserMapper;
import com.readuu.mybatisdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
