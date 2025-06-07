package com.nanco.social.service;

import com.nanco.social.model.User;
import com.nanco.social.model.dto.LoginRequest;
import com.nanco.social.model.dto.UserRegisterRequest;

public interface UserService {

    /**
     * 使用者註冊
     * 
     * @param request 註冊請求
     * @return 註冊成功返回使用者信息，失敗返回null
     */
    User register(UserRegisterRequest request);

    /**
     * 使用者登入
     * 
     * @param request 登入請求
     * @return 登入成功返回使用者信息，失敗返回null
     */
    User login(LoginRequest request);

    /**
     * 更新使用者信息
     * 
     * @param user 使用者信息
     * @return 更新成功返回true，失敗返回false
     */
    boolean updateUser(User user);
}
