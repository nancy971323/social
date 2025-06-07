package com.nanco.social.controller;

import com.nanco.social.model.Comment;
import com.nanco.social.model.dto.ApiResponse;
import com.nanco.social.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// 要求：使用RESTful API 風格建立後端服務
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 要求：使用RESTful API 風格建立後端服務 - 新增留言
    @PostMapping("/create")
    public ApiResponse<Boolean> createComment(@RequestBody Comment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        
        boolean result = commentService.createComment(comment);
        return ApiResponse.success(result ? "留言成功" : "留言失敗", result);
    }

    @GetMapping("/list/{postId}")
    public ApiResponse<List<Comment>> getPostComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getPostComments(postId);
        return ApiResponse.success(comments);
    }
}
