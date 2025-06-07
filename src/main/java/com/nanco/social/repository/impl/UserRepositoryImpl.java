package com.nanco.social.repository.impl;

import com.nanco.social.model.User;
import com.nanco.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 要求：透過 Stored Procedure 存取資料庫
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 創建用戶
    @Override
    public boolean createUser(User user) {
        try {
            jdbcTemplate.update(
                    "CALL sp_create_user(?, ?, ?, ?)",
                    user.getUserName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 用戶登入
    // 要求：需防止SQL Injection 以及XSS 攻擊 - 使用參數化查詢防止SQL Injection
    @Override
    public User login(String phoneNumber, String password) {
        try {
            List<User> users = jdbcTemplate.query(
                    "CALL sp_user_login(?, ?)",
                    new Object[]{phoneNumber, password},
                    new UserRowMapper()
            );
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 要求：透過 Stored Procedure 存取資料庫 - 更新用戶
    @Override
    public boolean updateUser(User user) {
        try {
            jdbcTemplate.update(
                    "CALL sp_update_user_biography(?, ?)",
                    user.getUserId(),
                    user.getBiography()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setBiography(rs.getString("biography"));
            // 不返回密碼
            return user;
        }
    }
}
