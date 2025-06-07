package com.nanco.social.service.impl;

import com.nanco.social.common.exception.BusinessException;
import com.nanco.social.common.util.HtmlEscapeUtil;
import com.nanco.social.model.Comment;
import com.nanco.social.repository.CommentRepository;
import com.nanco.social.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean createComment(Comment comment, Long userId) {
        if (comment == null) {
            throw new BusinessException("留言數據不能為空");
        }
        if (userId == null) {
            throw new BusinessException("使用者ID不能為空");
        }
        if (comment.getPostId() == null) {
            throw new BusinessException("發文ID不能為空");
        }
        if (comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new BusinessException("留言內容不能為空");
        }

        // 設置使用者ID
        comment.setUserId(userId);

        // 要求：需防止SQL Injection 以及XSS 攻擊 - XSS防護
        comment.setContent(HtmlEscapeUtil.escape(comment.getContent()));

        // 創建留言
        return commentRepository.createComment(comment);
    }

    @Override
    public List<Comment> getPostComments(Long postId) {
        if (postId == null) {
            throw new BusinessException("發文ID不能為空");
        }

        return commentRepository.getPostComments(postId);
    }
}
