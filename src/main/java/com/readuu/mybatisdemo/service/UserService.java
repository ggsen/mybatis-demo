package com.readuu.mybatisdemo.service;

import com.readuu.mybatisdemo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
}
