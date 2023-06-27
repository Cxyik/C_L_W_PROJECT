package com.example.JDBCClass;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllUsers() {
        String sql = "SELECT * FROM file_index";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String path = "";
            path = rs.getString("path");
            System.out.println(path);
            return path;
        });
    }

    public void createUser(String path) {
        String sql = "INSERT INTO file_index (path) VALUES (?)";
        jdbcTemplate.update(sql, path);
    }



    public void deleteUser() {
        String sql = "DELETE FROM file_index";
        jdbcTemplate.update(sql);
    }
}
