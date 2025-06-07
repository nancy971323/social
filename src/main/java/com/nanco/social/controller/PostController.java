package com.nanco.social.controller;

import com.nanco.social.model.Post;
import com.nanco.social.model.dto.ApiResponse;
import com.nanco.social.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// 要求：使用RESTful API 風格建立後端服務
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 要求：使用RESTful API 風格建立後端服務 - HTTP POST 創建資源
    @PostMapping("/create")
    public ApiResponse<Boolean> createPost(@RequestBody Post post, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean result = postService.createPost(post, userId);
        return ApiResponse.success(result ? "發文成功" : "發文失敗", result);
    }

    // 要求：使用RESTful API 風格建立後端服務 - HTTP GET 獲取資源列表
    @GetMapping("/list")
    public ApiResponse<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ApiResponse.success(posts);
    }

    // 要求：使用RESTful API 風格建立後端服務 - HTTP PUT 更新資源
    @PutMapping("/edit")
    public ApiResponse<Boolean> editPost(@RequestBody Post post, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean result = postService.editPost(post, userId);
        return ApiResponse.success(result ? "編輯成功" : "編輯失敗", result);
    }

    // 要求：使用RESTful API 風格建立後端服務 - HTTP DELETE 刪除資源
    @DeleteMapping("/delete/{postId}")
    public ApiResponse<Boolean> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean result = postService.deletePost(postId, userId);
        return ApiResponse.success(result ? "刪除成功" : "刪除失敗", result);
    }
}
