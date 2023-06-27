package com.example.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserMapperRepository {
    private final UserMapper userMapper;

    public UserMapperRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public List<String> findAllUsers() {
        return userMapper.findAllUsers();
    }
    public void createUser(String path) {
        userMapper.createUser(path);
    }
    public void deleteUser() {
        userMapper.deleteUser();
    }
}
