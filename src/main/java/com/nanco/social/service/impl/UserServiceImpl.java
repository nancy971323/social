package com.nanco.social.service.impl;

import com.nanco.social.common.exception.BusinessException;
import com.nanco.social.common.util.HtmlEscapeUtil;
import com.nanco.social.model.User;
import com.nanco.social.model.dto.LoginRequest;
import com.nanco.social.model.dto.UserRegisterRequest;
import com.nanco.social.repository.UserRepository;
import com.nanco.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(UserRegisterRequest request) {
        if (request == null) {
            throw new BusinessException("註冊請求不能為空");
        }

        // 基本數據驗證
        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new BusinessException("使用者名稱不能為空");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BusinessException("密碼不能為空");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            throw new BusinessException("手機號碼不能為空");
        }

        // 要求：需防止SQL Injection 以及XSS 攻擊 - 轉換請求數據為使用者對象，並進行XSS防護
        User user = new User();
        user.setUserName(HtmlEscapeUtil.escape(request.getUserName()));
        user.setEmail(HtmlEscapeUtil.escape(request.getEmail()));
        // 要求：Password 密碼請加鹽(salt)並經雜湊(Hash)後儲存，避免明碼外洩 - 密碼在存儲過程中雜湊
        user.setPassword(request.getPassword()); // 密碼不需轉義，會被哈希
        user.setBiography(HtmlEscapeUtil.escape(request.getBiography()));
        user.setPhoneNumber(request.getPhoneNumber()); // 手機號碼不需轉義，假設只包含數字

        // 創建使用者
        boolean result = userRepository.createUser(user);

        if (result) {
            // 創建成功，返回登入信息
            return userRepository.login(user.getPhoneNumber(), request.getPassword());
        } else {
            throw new BusinessException("註冊失敗，可能是使用者名稱或手機號碼已存在");
        }
    }

    @Override
    public User login(LoginRequest request) {
        if (request == null) {
            throw new BusinessException("登入請求不能為空");
        }

        // 基本數據驗證
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            throw new BusinessException("手機號碼不能為空");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BusinessException("密碼不能為空");
        }

        // 要求：Password 密碼請加鹽(salt)並經雜湊(Hash)後儲存，避免明碼外洩 - 進行登入驗證（密碼在存儲過程中雜湊比對）
        User user = userRepository.login(request.getPhoneNumber(), request.getPassword());

        if (user == null) {
            throw new BusinessException("登入失敗，手機號碼或密碼錯誤");
        }

        return user;
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new BusinessException("使用者數據不完整");
        }

        return userRepository.updateUser(user);
    }
}
