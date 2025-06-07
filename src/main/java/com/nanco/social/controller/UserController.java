package com.nanco.social.controller;

import com.nanco.social.common.util.JwtUtil;
import com.nanco.social.model.User;
import com.nanco.social.model.dto.ApiResponse;
import com.nanco.social.model.dto.JwtResponse;
import com.nanco.social.model.dto.LoginRequest;
import com.nanco.social.model.dto.UserRegisterRequest;
import com.nanco.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

// 要求：使用RESTful API 風格建立後端服務
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 要求：使用RESTful API 風格建立後端服務 - 用戶註冊
    @PostMapping("/register")
    public ApiResponse<JwtResponse> register(@RequestBody UserRegisterRequest request) {
        User user = userService.register(request);
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserName());
        JwtResponse response = new JwtResponse(token, user.getUserId(), user.getUserName(), user.getEmail(), user.getBiography());
        return ApiResponse.success("註冊成功", response);
    }

    // 要求：使用RESTful API 風格建立後端服務 - 用戶登入
    @PostMapping("/login")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);

        // 生成JWT令牌
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserName());

        // 創建JWT響應
        JwtResponse response = new JwtResponse(token, user.getUserId(), user.getUserName(), user.getEmail(), user.getBiography());
        return ApiResponse.success("登入成功", response);
    }

    // 使用JWT不需要伺服器端登出方法

    // 要求：使用RESTful API 風格建立後端服務 - 更新用戶資料
    @PutMapping("/update")
    public ApiResponse<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        // 確保只能更新自己的信息
        user.setUserId(userId);

        boolean result = userService.updateUser(user);
        return ApiResponse.success(result ? "更新成功" : "更新失敗", result);
    }
}
