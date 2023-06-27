package com.example.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT path FROM file_index")
    List<String> findAllUsers();
    @Insert("INSERT INTO file_index (path) VALUES (#{path})")
    void createUser(String path);
    @Delete("DELETE FROM file_index")
    void deleteUser();
}
